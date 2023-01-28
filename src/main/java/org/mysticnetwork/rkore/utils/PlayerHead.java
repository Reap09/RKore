package org.mysticnetwork.rkore.utils;

import java.io.IOException;
import java.io.InputStreamReader;;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class PlayerHead {

    public String getPlayerHeadUrl(String playerName) {
        String playerHeadUrl = "";
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "KushStaffLogger");
            connection.setDoOutput(true);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(new InputStreamReader(connection.getInputStream()));
            String playerUuid = json.get("id").toString();
            playerHeadUrl = "https://crafatar.com/avatars/" + playerUuid + "?overlay=head";
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return playerHeadUrl;
    }
}
