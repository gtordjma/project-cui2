import java.io.IOException;
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
import java.util.*;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ES {

    private static final String ES_URL = "http://localhost:9200/cui2/tweet";



    ES(){

    }

    public Map<String, List<Map<String, Object>>> get_alltweet() throws IOException{
        JSONObject js = new JSONObject();
        js.put("query", "\"match_all\": {}");
        js.put("sort", "\"date\": {\"order\": \"desc\"}");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(ES_URL + "/_search");
            StringEntity params = new StringEntity("{\"query\":{\"match_all\": {}}, \"sort\": { \"date\": { \"order\": \"desc\" }}}");
            request.setEntity(params);
            request.setHeader("Accept", "application/json");
            request.setHeader("content-type", "application/json");




            Header[] headers = request.getAllHeaders();


            System.out.println(request.toString());
            for (Header header : headers) {
                System.out.println(header.getName() + ": " + header.getValue());
            }


            HttpResponse  response = httpClient.execute(request);
            String s = getStringFromInputStream(response.getEntity().getContent());
            System.out.println(s);
            int x = s.indexOf("{");
            JSONObject json = new JSONObject(s.substring(x));
            JSONArray arr = json.getJSONObject("hits").getJSONArray("hits");
            Map<String, Integer> res  = new HashMap<>();
            Map<String, List<Map<String, Object>>> lst = new HashMap<String,List<Map<String, Object>>>();
            Map<String, Object> map = new HashMap<String, Object> ();
            List<Map<String, Object>> list = new ArrayList<>();
            String fin = "";
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < arr.length(); i++) {


                // convert JSON string to Map
                fin = arr.getJSONObject(i).getJSONObject("_source").toString();
                map = mapper.readValue(fin, Map.class);
/*                for(String key:map.keySet()){
                    System.out.println("treeMap: [key: "+key+" , value: "+map.get(key)+"]");
                }*/
                list.add(map);
        /*        System.out.println(fin);*/


            }


            lst.put("tweets", list);
/*            Iterator<String> itr = lst.keySet().iterator();
            while (itr.hasNext()) {
                System.out.println(itr.next().intern());
            }*/
            return lst;
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            httpClient.close();
        }
        return null;
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
