import java.io.IOException;

import io.netty.handler.codec.http.DefaultHttpRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.*;
import java.lang.*;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import sun.rmi.runtime.Log;

public class ES {

    private static final String ES_URL = "http://localhost:9200/cui2/tweet";



    ES(){

    }

    public HttpPost getHttpPost(String uri, String query) throws IOException{
        HttpPost request = new HttpPost(uri);
        StringEntity params = new StringEntity(query);
        request.setEntity(params);
        request.setHeader("Accept", "application/json");
        request.setHeader("content-type", "application/json");
        return request;
    }

    public List<Map<String, Object>> search(String query) throws IOException{
        Map<String, List<Map<String, Object>>> model = new HashMap<String,List<Map<String, Object>>>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = getHttpPost(ES_URL + "/_search", query);
            HttpResponse  response = httpClient.execute(request);
            String s = getStringFromInputStream(response.getEntity().getContent());
            int x = s.indexOf("{");
            JSONObject json = new JSONObject(s.substring(x));
            JSONArray arr = json.getJSONObject("hits").getJSONArray("hits");
            String fin = "";
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < arr.length(); i++) {
                // convert JSON string to Map
                fin = arr.getJSONObject(i).getJSONObject("_source").toString();
                map = mapper.readValue(fin, Map.class);
                map.put("id_tweet",  arr.getJSONObject(i).getString("_id"));
                list.add(map);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }


    public Map<String, List<Map<String, Object>>> get_alltweet() throws IOException{
        String query = "{\"query\":{\"match_all\": {}}, \"sort\": { \"date\": { \"order\": \"desc\" }}}";
        Map<String, List<Map<String, Object>>> model = new HashMap<String,List<Map<String, Object>>>();
        List<Map<String, Object>> List = search(query);
        model.put("tweets", List);
        return model;
    }

    public Map<String, List<Map<String, Object>>> get_idtweet(String id) throws IOException{
        Map<String, List<Map<String, Object>>> model = new HashMap<String,List<Map<String, Object>>>();

        List<Map<String, Object>> List = search("{\"query\":{\"match\": {\"id_user\" : \""+id+"\"}}, \"sort\": { \"date\": { \"order\": \"desc\" }}}");
        model.put("tweets", List);
        return model;

    }

    public Map<String, List<Map<String, Object>>> get_usertimeline(User user) throws IOException{
        List<Map<String, Object>> finalList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> model = new HashMap<String,List<Map<String, Object>>>();
        for (int i = 0; i < user.getFollows().size(); i++) {
            String id = Integer.toString(user.getFollows().get(i));
            List<Map<String, Object>> newList = search("{\"query\":{\"match\": {\"id_user\" : \""+id+"\"}}, \"sort\": { \"date\": { \"order\": \"desc\" }}}");
            Stream.of(newList).forEach(finalList::addAll);
        }
        model.put("tweets", finalList);
        return model;

    }


    public Boolean tweet(String s, User u) throws IOException{
        Map<String, Object> model = parseTweet(s);
        String query = "{\"username\": \""+ u.getFirst_name() +" "+ u.getLast_name()
                + "\", \"id_user\": "+ u.getId()
                + ",\"tweet\": \"" + s
                + "\",\"hashtags\": " + model.get("tags")
                + ",\"date\": \"" + model.get("date").toString() + "\"}";
        /*System.out.println(query);*/
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = getHttpPost(ES_URL, query);
/*            StringEntity params = new StringEntity(query);
            Header[] headers = request.getAllHeaders();
            System.out.println(request.toString());
            for (Header header : headers) {
                System.out.println(header.getName() + ": " + header.getValue());
            }*/
            HttpResponse  response = httpClient.execute(request);
            String rep = getStringFromInputStream(response.getEntity().getContent());
            /*System.out.println(response.getStatusLine().getStatusCode());*/
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.close();
        }
        return false;
    }
    public Boolean DELETE(String id) throws IOException{
        URL url = null;
        try {
            url = new URL(ES_URL + "/" + id);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");
            System.out.println(httpURLConnection.getResponseCode());
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return true;
    }

    public Map<String, Object> parseTweet(String s){
        Map<String, Object> model = new HashMap<String, Object>();
        String tag = parseTag(s);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        model.put("tags", tag);
        model.put("date", sdf.format(cal.getTime()));
        return model;
    }

    public String parseTag(String s){
        ArrayList<String> tag = new ArrayList <>();
        Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
        Matcher mat = MY_PATTERN.matcher(s);
        while (mat.find()) {
            tag.add(mat.group(1));
        }
        String hash = "[";
        for (int i = 0; i < tag.size(); i++){
            hash += "\"" + tag.get(i) + "\"";
            hash += i + 1 < tag.size() ? "," : "";
        }
        hash += "]";
        return hash;
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
