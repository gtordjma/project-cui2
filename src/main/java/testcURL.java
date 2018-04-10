import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
import java.lang.*;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class testcURL {


        public static void main(String[] args) throws IOException {
           System.out.println(sendData());
        }


        public static String sendData() throws IOException {
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
                return getStringFromInputStream(response.getEntity().getContent());

// handle response here...
            } catch (Exception ex) {
                // handle exception here
            } finally {
                httpClient.close();
            }
            return "";

/*            // curl_init and url
            URL url = new URL( "\"http://localhost:9200/gtordjma/tutoriels/_search\" -H 'Content-Type: application/json' -d' {\"query\":{\"match_all\": {}}}'");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // CURLOPT_POST
            con.setRequestMethod("POST");

// CURLOPT_FOLLOWLOCATION
            con.setInstanceFollowRedirects(true);

            String postData = "my_data_for_posting";
            con.setRequestProperty("Content-length",
                    String.valueOf(postData.length()));

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

// "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            System.out.println("Response    (Code):" + code);
            System.out.println("Response (Message):" + con.getResponseMessage());

// read the response
            DataInputStream input = new DataInputStream(con.getInputStream());
            int c;
            StringBuilder resultBuf = new StringBuilder();
            while ((c = input.read()) != -1) {
                resultBuf.append((char) c);
            }
            input.close();

            return resultBuf.toString();*/
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
