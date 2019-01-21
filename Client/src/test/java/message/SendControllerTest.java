package message;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.testng.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import id.Id;
import org.apache.http.HttpResponse;
import org.testng.annotations.*;
import shellutilities.YouAreEll;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class SendControllerTest {
    private WireMockServer wireMockServer;
    private YouAreEll urlClient;
    private SendController sd;
    private final String FROM_ID = "dodo";
    private final String TO_ID = "meow";

    @BeforeSuite
    public void beforeAll() throws IOException {

        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        urlClient = new YouAreEll("http://localhost:" + wireMockServer.port());

        sd = new SendController(new String[]{"ids", "dodoname", "dodogit"});
        sd.setUrl(urlClient);

    }

    @AfterSuite
    public void afterAll() {
        wireMockServer.shutdown();
    }

    @AfterTest
    public void beforeEach() {
        wireMockServer.resetAll();
    }

    @Test
    public void testSendToWorldSuccess() throws IOException {

        String response = new Message("hashcode here", "today 11-20-2018", FROM_ID, "", "Hello test")
                .toString().replace("\n", "");

        wireMockServer.stubFor(
                post(urlEqualTo("/ids/" + FROM_ID + "/messages"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = sd.postMessage(FROM_ID, response);

        Message result = MessageMapper.returnObject(YouAreEll.readCalls(output));

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(result.toid, "");

    }


    @Test
    public void testSendToIdSuccess() throws IOException {
        String response = new Message("hashcode here", "today 11-20-2018", FROM_ID, TO_ID, "Hello test")
                .toString().replace("\n", "");

        wireMockServer.stubFor(
                post(urlEqualTo("/ids/" + FROM_ID + "/messages"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = sd.postMessage(FROM_ID, response);

        Message result = MessageMapper.returnObject(YouAreEll.readCalls(output));

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(result.toid, TO_ID);

    }


    // Test for with to id, without toid, success and fail
}

