package com.scissor.cityswim.app;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import java.io.IOException;


public class SwimListActivity extends ActionBarActivity {

    public static final String TAG = "CitySwim";
    private SwimTableController tableController;
    private SwimDataFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting onCreate");
        setContentView(R.layout.activity_swim_list);
        Log.i(TAG, "contentView set");
        tableController = new SwimTableController(getApplicationContext(), (TableLayout) findViewById(R.id.table));
        Log.i(TAG, "tableContoller created");
        FragmentManager fm = getFragmentManager();
        dataFragment = (SwimDataFragment) fm.findFragmentByTag("swimdata");
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new SwimDataFragment();
            fm.beginTransaction().add(dataFragment, "swimdata").commit();
            new LoadSwimsTask(dataFragment).execute();
        } else {
            tableController.updateContents(dataFragment.getSwims());
        }
        Log.i(TAG, "task called");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_swim_list, menu);
//        return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadSwimsTask extends AsyncTask<String, Integer, Swim[]> {

        private final SwimDataFragment saveTo;

        public LoadSwimsTask(SwimDataFragment saveTo) {

            this.saveTo = saveTo;
        }

        @Override
        protected Swim[] doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            try {
                final Swim[] swims = new SwimLoader().loadSwims();
                Log.i(TAG, "loaded swims: " + swims.length);
                saveTo.setSwims(swims);
                return swims;
            } catch (IOException e) {
                Log.e(TAG, "IO issues", e);
                return new Swim[0];
            }
        }

        @Override
        protected void onPostExecute(Swim[] swims) {
            Log.i(TAG, "starting OnPostExecute");
            tableController.updateContents(dataFragment.getSwims());
            Log.i(TAG, "finished OnPostExecute");
        }



    }
}
