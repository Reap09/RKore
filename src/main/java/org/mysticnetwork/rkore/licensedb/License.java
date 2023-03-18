package org.mysticnetwork.rkore.licensedb;

import lombok.Getter;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class License {

    private final String licenseKey;
    private final String product;
    private final String version;

    private int statusCode;
    private String discordName;
    private String discordID;
    private String statusMsg;
    private boolean valid;

    public License(String licenseKey, String product, String version) {
        this.licenseKey = licenseKey;
        this.product = product;
        this.version = version;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"license\": \""+ licenseKey +"\",\n    \"product\": \""+ product +"\",\n    \"version\": \""+ version +"\"\n}");
        Request request = new Request.Builder()
                .url("http://<ip>:<port>/api/client")
                .method("POST", body)
                .addHeader("Authorization", "<api-key>")
                .build();
        Response response = null;
        String data = null;
        try {
            response = client.newCall(request).execute();
            data = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject obj = new JSONObject(data);

        if (!obj.has("status_msg") || !obj.has("status_id")) return;

        statusCode = obj.getInt("status_code");
        statusMsg = obj.getString("status_msg");

        if (!obj.has("status_overview")) return;

        discordName = obj.getString("discord_tag");
        discordID = obj.getString("discord_id");

        valid = true;
    }
}