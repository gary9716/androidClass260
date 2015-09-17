package com.example.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView textView;
    /* geo point double array */
    private double[] geoPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String storeInfo = intent.getStringExtra("store_info");
        String menu = intent.getStringExtra("menu");

        textView.setText(note + "," + storeInfo + "," + menu);
        /*call load geo point function*/
        loadGeoPoint(storeInfo);
    }

    /* declare load geo point function */
    private void loadGeoPoint(String storeInfo){
        String geoQueryUrl = Utils.getGeoQueryUrl(storeInfo);
        Utils.NetworkTask task = new Utils.NetworkTask();
        task.setCallback(new Utils.NetworkTask.Callback(){
            @Override
            public void done(byte[] fetchResult) {
                String jsonString = new String(fetchResult);
                geoPoint = Utils.getGeoPoint(jsonString);
                textView.setText("lat: " + geoPoint[0]
                        + ", lng: " + geoPoint[1]);
            }
        });
        task.execute(geoQueryUrl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
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
}
