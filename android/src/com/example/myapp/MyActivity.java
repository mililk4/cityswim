package com.example.myapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyActivity extends Activity {

    private static final String TAG = "CitySwim";
    public static final String SWIM_DATA = "http://williampietri.com/cityswim/v1/swims.json";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting onCreate");
        setContentView(R.layout.main);
        Log.i(TAG, "contentView set");
        new LoadSwimsTask().execute();
        Log.i(TAG, "task called");
    }




    private class LoadSwimsTask extends AsyncTask<String,Integer,Swim[]> {

        @Override
        protected Swim[] doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            try {
                final Swim[] swims = loadSwims();
                Log.i(TAG, "loaded swims: " + swims.length );
                return swims;
            } catch (IOException e) {
                Log.e(TAG, "IO issues", e);
                return new Swim[0];
            }
        }

        @Override
        protected void onPostExecute(Swim[] swims) {
            Log.i(TAG, "starting OnPostExecute");

            fillSwims((TableLayout) findViewById(R.id.table), swims);
            Log.i(TAG, "finished OnPostExecute");

        }

        private void fillSwims(TableLayout table, Swim[] swims) {
            table.removeAllViews();
            for (Swim swim : swims) {
                if (!swim.isOver()) {
                    TableRow row = new TableRow(getApplicationContext());
                    row.addView(newColumn(swim.getPoolName(), swim.isRunning()));
                    row.addView(newColumn(swim.getDayLabel(), swim.isRunning()));
                    row.addView(newColumn(swim.getStartLabel(), swim.isRunning()));
                    row.addView(newColumn(swim.getEndLabel(), swim.isRunning()));
                    table.addView(row);
                }
            }
        }

        private TextView newColumn(String poolName, boolean bold) {
            final TextView column = new TextView(getApplicationContext());
            column.setText(poolName);
            column.setTextSize(18);
            column.setPadding(5, 5, 5, 5);
            if (bold) {
                column.setTypeface(null, Typeface.BOLD);
            }
            return column;
        }

        public Swim[] loadSwims() throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(SWIM_DATA).openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String pageContents = inhaleData(in);
                return jsonToSwims(pageContents);
            } catch (JSONException e) {
                Log.e(TAG,"parse failure", e);
                return new Swim[0];
            } finally {
                urlConnection.disconnect();
            }
        }

        private Swim[] jsonToSwims(String pageContents) throws JSONException {
            JSONObject json = new JSONObject(pageContents);
            JSONArray swimsJson = (JSONArray) json.get("swims");
            Swim[] swims = new Swim[swimsJson.length()];
            for (int i = 0; i < swimsJson.length(); i++) {
                JSONObject swimJson = (JSONObject) swimsJson.get(i);
                swims[i] = new Swim(swimJson.getString("pool_name"),
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

}
