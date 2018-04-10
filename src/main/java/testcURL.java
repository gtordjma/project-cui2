import java.io.IOException;
import org.json.*;
import java.lang.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class testcURL {


        public static void main(String[] args) throws IOException {
            JSONObject json = sendData();
            JSONArray arr = json.getJSONObject("hits").getJSONArray("hits");
            Map<String, Integer> res  = new HashMap<>();
            for (int i = 0; i < arr.length(); i++) {
                System.out.println(arr.getJSONObject(i).getJSONObject("_source"));
/*                String type = arr.getJSONObject(i).getString();
                Integer amount = arr.getJSONObject(i).getInt("amount");
                res.put(type, amount);*/
            }
/*            Set set = res.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
                System.out.println(mentry.getValue());
            }*/
        }


        public static JSONObject sendData() throws IOException {
            JSONObject json = new JSONObject();
            json.put("query", "\"match_all\": {}");

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            try {
                HttpPost request = new HttpPost("http://localhost:9200/gtordjma/tutoriels/_search");
                StringEntity params = new StringEntity(json.toString());
                params.getContent();
                request.addHeader("content-type", "application/json");
                /*System.out.println(getStringFromInputStream(params.getContent()));*/
                HttpResponse  response = httpClient.execute(request);
                return new JSONObject(getStringFromInputStream(response.getEntity().getContent()));

// handle response here...
            } catch (Exception ex) {
                // handle exception here
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
