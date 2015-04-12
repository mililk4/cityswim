package com.example.myapp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class SwimLoader {
    public static final String SWIM_DATA = "http://williampietri.com/cityswim/v1/swims.json";


    public Swim[] loadSwims() throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(SWIM_DATA).openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String pageContents = inhaleData(in);
            return extractSwims(pageContents);
        } catch (JSONException e) {
            Log.e(MyActivity.TAG, "parse failure", e);
            return new Swim[0];
        } finally {
            urlConnection.disconnect();
        }
    }

    private Swim[] extractSwims(String pageContents) throws JSONException {
        JSONObject json = new JSONObject(pageContents);
        Pool[] pools = jsonToPools((JSONArray) json.get("pools"));
        return jsonToSwims(pools, (JSONArray) json.get("swims"));
    }

    private Pool[] jsonToPools(JSONArray poolsJson) throws JSONException {
        Pool[] pools = new Pool[poolsJson.length()];
        for (int i = 0; i < poolsJson.length(); i++) {
            JSONObject poolJson = (JSONObject) poolsJson.get(i);
            pools[i] = new Pool(poolJson.getString("pool_name"),
                    poolJson.getDouble("latitude"),
                    poolJson.getDouble("longitude"));

        }
        return pools;
    }

    private Swim[] jsonToSwims(Pool[] pools, JSONArray swimsJson) throws JSONException {
        HashMap<String, Pool> poolsByName = new HashMap<>();
        for (Pool pool : pools) {
            poolsByName.put(pool.getName(), pool);
        }
        Swim[] swims = new Swim[swimsJson.length()];
        for (int i = 0; i < swimsJson.length(); i++) {
            JSONObject swimJson = (JSONObject) swimsJson.get(i);
            swims[i] = new Swim(poolsByName.get(swimJson.getString("pool_name")),
                    swimJson.getString("day_label"),
                    swimJson.getString("start_label"),
                    swimJson.getString("end_label"),
                    swimJson.getLong("start"),
                    swimJson.getLong("end")
            );
        }
        return swims;
    }

    private String inhaleData(InputStream in) throws IOException {
        BufferedReader bReader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);
        StringBuilder sBuilder = new StringBuilder();

        String line;
        while ((line = bReader.readLine()) != null) {
            sBuilder.append(line);
            sBuilder.append("\n");
        }

        in.close();
        return sBuilder.toString();
    }

}
