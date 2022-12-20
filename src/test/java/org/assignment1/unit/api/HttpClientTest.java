package org.assignment1.unit.api;


import okhttp3.*;
import org.assignment1.api.HttpClient;
import org.assignment1.models.Entry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.IOException;

public class HttpClientTest {

    @Mock
    private OkHttpClient httpClient;
    HttpClient client;

    Entry alert;

    Request request;

    @BeforeEach
    public void setup() {
        httpClient = Mockito.mock(OkHttpClient.class);
        this.client = new HttpClient(httpClient);
        alert = new Entry("6", "testHeading", "testDescription", "testUrl", "testImageUrl", "100");
        RequestBody formBody = new FormBody.Builder()
                .add("alertType", alert.getType())
                .add("heading", alert.getHeading())
                .add("description", alert.getDescription())
                .add("url", alert.getUrl())
                .add("imageUrl", alert.getImageUrl())
                .add("postedBy", "a338083d-1742-421a-be40-5978848942df")
                .add("priceInCents", alert.getPriceInCents())
                .build();

        request = new Request.Builder()
                .url("https://api.marketalertum.com/Alert")
                .post(formBody)
                .build();
    }

    @AfterEach
    public void teardown() {
        this.httpClient = null;
        this.client = null;
    }

    @Test
    public void addAlertSuccess() throws IOException {
        Response resp = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(201).message("")
                .build();

        Call mockCall = Mockito.mock(Call.class);

        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenReturn(resp);

        boolean flag = client.addAlert(alert);
        Assertions.assertTrue(flag);
    }

    @Test
    public void addAlertError() throws IOException {
        Response resp = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(415).message("")
                .build();

        Call mockCall = Mockito.mock(Call.class);

        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenReturn(resp);

        boolean flag = client.addAlert(alert);
        Assertions.assertFalse(flag);
    }

    @Test
    public void addAlertException() throws IOException {
        Call mockCall = Mockito.mock(Call.class);
        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenThrow(new IOException());

        boolean flag = client.addAlert(alert);
        Assertions.assertFalse(flag);
    }

    @Test
    public void purgeAlertsException() throws IOException {
        Call mockCall = Mockito.mock(Call.class);
        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenThrow(new IOException());

        boolean flag = client.purgeAlerts();
        Assertions.assertFalse(flag);
    }

    @Test
    public void purgeAlertsError() throws IOException {
        Response resp = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(415).message("")
                .build();

        Call mockCall = Mockito.mock(Call.class);

        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenReturn(resp);

        boolean flag = client.purgeAlerts();
        Assertions.assertFalse(flag);
    }

    @Test
    public void purgeAlertsSuccess() throws IOException {
        Response resp = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("")
                .build();

        Call mockCall = Mockito.mock(Call.class);

        Mockito.when(
                httpClient.newCall(Mockito.any())
        ).thenReturn(mockCall);
        Mockito.when(
                mockCall.execute()
        ).thenReturn(resp);

        boolean flag = client.purgeAlerts();
        Assertions.assertTrue(flag);
    }
}
