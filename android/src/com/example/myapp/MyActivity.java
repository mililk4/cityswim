package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;

import java.io.*;

public class MyActivity extends Activity  {

    public static final String TAG = "CitySwim";
    private SwimTableController tableController;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting onCreate");
        setContentView(R.layout.main);
        Log.i(TAG, "contentView set");
        tableController = new SwimTableController(getApplicationContext(), (TableLayout) findViewById(R.id.table));
        Log.i(TAG, "tableContoller allocated");
        new LoadSwimsTask().execute();
        Log.i(TAG, "task called");
    }


    private class LoadSwimsTask extends AsyncTask<String, Integer, Swim[]> {

        @Override
        protected Swim[] doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            try {
                final Swim[] swims = new SwimLoader().loadSwims();
                Log.i(TAG, "loaded swims: " + swims.length);
                return swims;
            } catch (IOException e) {
                Log.e(TAG, "IO issues", e);
                return new Swim[0];
            }
        }

        @Override
        protected void onPostExecute(Swim[] swims) {
            Log.i(TAG, "starting OnPostExecute");
            tableController.updateContents(swims);
            Log.i(TAG, "finished OnPostExecute");

        }



    }

}
