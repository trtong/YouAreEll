package message;

import com.github.tomakehurst.wiremock.WireMockServer;

import org.apache.http.HttpResponse;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import shellutilities.YouAreEll;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class MessageControllerTest {
    private WireMockServer wireMockServer;
    private YouAreEll urlClient;
    private MessageController mc;
    private final String GIT_NAME = "dodogitname";

    @BeforeSuite
    public void beforeAll() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        urlClient = new YouAreEll("http://localhost:" + wireMockServer.port());
        mc = new MessageController(new String[] {"messages", GIT_NAME});
        mc.setUrl(urlClient);

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
    public void testGetMessagesSuccess() throws IOException {
        String response = new Message("", "", "dodogitname", "", "Hello test")
                .toString().replace("\n", "");

        wireMockServer.stubFor(
                get(urlEqualTo("/messages"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = mc.get_messages();

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(YouAreEll.readCalls(output), response);
    }
    @Test
    public void testGetMessageToId() throws IOException {

        String response = new Message("hashcode here", "today 11-20-2018", GIT_NAME, "", "Hello test")
                .toString().replace("\n", "");

        wireMockServer.stubFor(
                get(urlEqualTo("/ids/" + GIT_NAME + "/messages"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = mc.getMessageToId(GIT_NAME);

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(YouAreEll.readCalls(output), response);
    }

    @Test
    public void testGetMessagesToAndFrom() throws IOException {

        String response = new Message("hashcode here", "today 11-20-2018", GIT_NAME, "moo", "Moo moo moo cow")
                .toString().replace("\n", "");

        wireMockServer.stubFor(
                get(urlEqualTo("/ids/" + GIT_NAME + "/from/" + "moo"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = mc.get_messages_from(GIT_NAME, "moo");

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(YouAreEll.readCalls(output), response);
    }

}
