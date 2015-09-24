package com.example.simpleui;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class OrderDetailActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private TextView textView;
    private WebView webView;
    private ImageView imageView;
    private ProgressDialog progressDialog;

    /* google map & mapFragment */
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    /* geo point double array */
    private double[] geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView);
        imageView = (ImageView) findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String storeInfo = intent.getStringExtra("store_info");
        String menu = intent.getStringExtra("menu");

        textView.setText(note + "," + storeInfo + "," + menu);
        /*call load geo point function*/
        loadGeoPoint(storeInfo);

    }

    private void loadWebView(double lat, double lng) {
        webView.loadUrl(Utils.getStaticMapURL(lat, lng));
    }

    private void loadImageView(double lat, double lng) {

        String url = Utils.getStaticMapURL(lat, lng);
        Utils.NetworkTask task = new Utils.NetworkTask();
        task.setCallback(new Utils.NetworkTask.Callback() {
            @Override
            public void done(byte[] fetchResult) {

                Bitmap bm = BitmapFactory.decodeByteArray(fetchResult, 0, fetchResult.length);
                imageView.setImageBitmap(bm);

                progressDialog.dismiss();
            }
        });
        task.execute(url);
    }

    /* declare load geo point function */
    private void loadGeoPoint(final String storeInfo) {
        String geoQueryUrl = Utils.getGeoQueryUrl(storeInfo);
        Utils.NetworkTask task = new Utils.NetworkTask();
        task.setCallback(new Utils.NetworkTask.Callback() {
            @Override
            public void done(byte[] fetchResult) {
                String jsonString = new String(fetchResult);
                geoPoint = Utils.getGeoPoint(jsonString);
                textView.setText("lat: " + geoPoint[0]
                        + ", lng: " + geoPoint[1]);

//                loadWebView(geoPoint[0], geoPoint[1]);
//                loadImageView(geoPoint[0], geoPoint[1]);
                loadGoogleMap();
            }
        });
        task.execute(geoQueryUrl);
    }

    private void loadGoogleMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(geoPoint != null){
            setUpGoogleMap();
        }
    }

    private void setUpGoogleMap() {
        String[] storeInfo = getIntent().getStringExtra("store_info")
                .split(" ");
        String place = storeInfo[0];
        String address = storeInfo[1];
        LatLng store = new LatLng(geoPoint[0], geoPoint[1]);
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(store, 5));
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions()
            .position(store)
            .title(place)
            .snippet(address));
    }
}
