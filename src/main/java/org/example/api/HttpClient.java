package org.example.api;

import okhttp3.*;
import org.assignment1.models.Entry;
import org.json.JSONObject;

import java.io.IOException;

public class HttpClient {

    private final String userId = "a338083d-1742-421a-be40-5978848942df";
    OkHttpClient client;

    public HttpClient(OkHttpClient client) {
        this.client = client;
    }

    public boolean addAlert(Entry alert) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alertType", 6)
                .put("heading", alert.getHeading())
                .put("description", alert.getDescription())
                .put("url", alert.getUrl())
                .put("imageUrl", alert.getImageUrl())
                .put("postedBy", userId)
                .put("priceInCents", Integer.parseInt(alert.getPriceInCents()));

        RequestBody jsonBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/Alert")
                .addHeader("Content-Type", "application/json")
                .post(jsonBody)
                .build();

        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            return response.code() == 201;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean purgeAlerts() {
        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/Alert?userId=" + userId)
                .delete()
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.code() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean purgeAlerts() {
        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/EventsLog/" + userId)
                .delete()
                .build();

        try {
            Response response = client.newCall(request).execute();

            return response.code() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
