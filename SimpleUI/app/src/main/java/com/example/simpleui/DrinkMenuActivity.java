package com.example.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 {
    "note": "hello world",
    "store_info": "南機場",
    "menu" : [
        {
            "name": "black tea",
            "l": 2,
            "m": 0
        },
        {
            "name": "tea",
            "l": 5,
            "m": 1
        }
    ]
 }
 */

public class DrinkMenuActivity extends AppCompatActivity {

    private TextView storeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        storeInfo = (TextView) findViewById(R.id.storeInfo);

        String storeInfoStr = getIntent().getStringExtra("store_info");
        storeInfo.setText(storeInfoStr);



    }

    public void add(View view) {
        Button button = (Button) view;
        int count = Integer.parseInt(button.getText().toString());
        button.setText(String.valueOf(count + 1));

    }

    private JSONArray getValue() {

        JSONArray result = new JSONArray();
        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        int len = root.getChildCount();
        for(int i = 1; i < len - 1; i++) {

            LinearLayout ll = (LinearLayout) root.getChildAt(i);
            String name = ((TextView)ll.getChildAt(0)).getText().toString();
            int l = Integer.valueOf(((Button) ll.getChildAt(1)).getText().toString());
            int m = Integer.valueOf(((Button) ll.getChildAt(2)).getText().toString());

            try {
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("l", l);
                object.put("m", m);
                result.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public void done(View view) {
        Intent data = new Intent();
        data.putExtra("result", getValue().toString());

        setResult(RESULT_OK, data);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drink_menu, menu);
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
