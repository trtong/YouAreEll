package shellutilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class YouAreEll {
    public HttpClient client;
    private final String BASE_URL;
    private Console c;

    public YouAreEll() {
        this.BASE_URL = "http://zipcode.rocks:8085"; // Default URL
        this.client = HttpClientBuilder.create().build();
        c = new Console();
    }

    public YouAreEll(String baseUrl) {
        this.BASE_URL = baseUrl;
        this.client = HttpClientBuilder.create().build();
        c = new Console();
    }

    public HttpResponse MakeURLCall(String mainUrl, RequestType req, String jsonPayload) {
        try {
            switch (req) {
                case GET:
                    return getCall(BASE_URL + mainUrl);

                case POST:
                    HttpPost postReq = new HttpPost(BASE_URL + mainUrl);
                    return makeCall(postReq, jsonPayload);

                case PUT:
                    HttpPut putReq = new HttpPut(BASE_URL + mainUrl);
                    return makeCall(putReq, jsonPayload);
                default:
                    return null;
            }
        } catch (IOException e) {
            c.print("Error occured, try again.");
            return null;
        }
    }

    private HttpResponse makeCall(HttpEntityEnclosingRequestBase requestBase, String payload) throws IOException {

            requestBase.setEntity(new StringEntity(payload));
            return client.execute(requestBase);
    }

    private HttpResponse getCall(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return client.execute(request);
    }

    public static String readCalls(HttpResponse response) {
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseString = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                responseString.append(line);
            }

            return responseString.toString();

        } catch (IOException e) {
            return ("An unexpected IO error occurred: " + e.getMessage());
        }
    }


}
