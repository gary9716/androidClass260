package com.example.simpleui;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.security.auth.callback.Callback;

/**
 * Created by ggm on 8/31/15.
 */
public class Utils {

//    final static String GEO_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=";

    public static void writeFile(Context context, String fileName, String text) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(text.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer);
            fis.close();

            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static byte[] uriToBytes(Context context, Uri uri) {

        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getPhotoUri() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (dir.exists() == false) {
            dir.mkdirs();
        }

        File file = new File(dir, "simpleui_photo.png");
        return Uri.fromFile(file);
    }

    public static byte[] fetchToByte(String urlString){
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private final static String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static String getGeoQueryUrl(String address){
        try {
            return GEO_URL + URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getGeoPoint(String jsonString){
        try {
            JSONObject object = new JSONObject(jsonString);
            String status = object.getString("status");
            if(status.equals("OK")){
                JSONObject location = object.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");
                return new double[]{lat, lng};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class NetworkTask extends AsyncTask<String, Void, byte[]> {
        private Callback callback;

        public void  setCallback(Callback callback){
            this.callback = callback;
        }
        @Override
        protected byte[] doInBackground(String... params) {
            String url = params[0];
            return Utils.fetchToByte(url);
        }

        @Override
        protected void onPostExecute(byte[] fetchResult){
            callback.done(fetchResult);
        }
        interface Callback {
            void done(byte[] fetchResult);
        }


    }

    /* fetchUrlToByte function
    getGeoQueryUrl
    getGeoPoint
    NetworkTask class
     */

    private final static String STATIC_MAP_URL = "https://maps.googleapis.com/maps/api/staticmap?";

    public static String getStaticMapURL(double lat, double lng) {
        return STATIC_MAP_URL + "center=" + lat + "," + lng +
                "&zoom=15&size=600x300&markers=color:blue|" + lat + "," + lng;
    }

    public static String getDrinkSum(JSONArray menu) {
        //TODO homework2
        return "0";
    }
}
