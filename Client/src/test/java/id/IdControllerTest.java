package id;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.testng.Assert.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.testng.annotations.*;
import shellutilities.YouAreEll;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class IdControllerTest {
    private WireMockServer wireMockServer;
    private YouAreEll urlClient;
    private IdController ic;

    @BeforeSuite
    public void beforeAll() throws IOException {

        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        urlClient = new YouAreEll("http://localhost:" + wireMockServer.port());

        ic = new IdController(new String[] {"ids", "dodoname", "dodogit"});
        ic.setUrl(urlClient);

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
    public void getIdsSuccess() {
        String response = new Id("userId", "name", "githubId").toString().replace("\n", "");

            wireMockServer.stubFor(
            get(urlEqualTo("/ids"))
                .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = ic.get_ids();

        assertEquals(output.getStatusLine().getStatusCode(), 200);
        assertEquals(YouAreEll.readCalls(output), response);
    }

    @Test
    public void testGetIdFail() {

        wireMockServer.stubFor(
                get(urlEqualTo("/ids"))
                        .willReturn(aResponse().withStatus(404))
        );

        HttpResponse result = ic.get_ids();

        assertEquals(result.getStatusLine().getStatusCode(), 404);
    }

    @Test
    public void testIdCreator() {

        Id testId = new Id("", "dodoname", "dodogit");

        Id newId = ic.idCreator();

        assertEquals(testId.github, newId.github);
        assertEquals(testId.name, newId.name);
    }

    @Test
    public void testPost_Id() {
        String response = new Id("userId", "name", "githubId").toString().replace("\n", "");

        wireMockServer.stubFor(
                post(urlEqualTo("/ids"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );

        HttpResponse output = ic.post_id(response);
        assertEquals(YouAreEll.readCalls(output), response);

    }

    @Test
    public void testPut_IdSuccess() {
        String response = new Id("userId", "name", "githubId").toString().replace("\n", "");
        wireMockServer.stubFor(
                put(urlEqualTo("/ids"))
                        .willReturn(aResponse().withStatus(200).withBody(response))
        );
        HttpResponse output = ic.put_id(response);

        assertEquals(YouAreEll.readCalls(output), response);
    }

    @Test
    public void testPut_IdFail404() {
        // Trying to pass in an empty payload...
        String response = new Id("", "", "").toString().replace("\n", "");
        wireMockServer.stubFor(
                put(urlEqualTo("/ids"))
                        .willReturn(aResponse().withStatus(404).withBody(response))
        );

        HttpResponse output = ic.put_id(response);
        assertEquals(output.getStatusLine().getStatusCode(), 404);
    }
}
