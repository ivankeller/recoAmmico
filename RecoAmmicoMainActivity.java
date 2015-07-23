package com.example.ivan.recoammico;

/**
 * A very simple activity for recommendation service testing purpose
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.util.Log;

import java.util.List;


public class RecoAmmicoMainActivity extends Activity {

    public static final String DB_PATH = "/data/data/com.example.ivan.recoammico/files/logfiles/ammico-dbh.sqlite";
    public static final String LIBRE_TOUR_PATH = "/data/data/com.example.ivan.recoammico/files/tours/tour-373.xml";
    private static final String DECOUVERTE_TOUR_PATH = "/data/data/com.example.ivan.recoammico/files/tours/tour-334.xml";
    public static final int MAX_NB_RECO = 3;
    public static final String VISITE_ID = "2";
    // for debugging:
    private static final String TAG = "reco";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_ammico_main);
        // launch recommendation service
        Intent intent = new Intent(this, RecommendationService.class);
        intent.putExtra("dbPath", DB_PATH);
        intent.putExtra("poisContentPath", LIBRE_TOUR_PATH);
        intent.putExtra("tourPath", DECOUVERTE_TOUR_PATH);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reco_ammico_main, menu);
        return true;
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

    public void buttonClick(View view)
    {
        List<Integer> recommendedPOIs = RecommendationService.getRecommendation(VISITE_ID, MAX_NB_RECO);
        Log.i(TAG, "Recommended POIs:");
        for (int idpoi : recommendedPOIs) {
            Log.i(TAG, Integer.toString(idpoi));
        }
    }
}
