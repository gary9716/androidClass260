package com.example.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_DRINK_MENU = 1;
    private EditText inputText;
    private CheckBox hide;
    private ListView history;
    private Spinner storeInfo;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String drinkMenuResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "N5ytfpTopfdCCmIqCgZZk5PDjkUiGudm1UaygmOv", "TH9KM8xYSN4nFD7GIxGDrppNsXVN1exKrm0v6KuA");

        sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();

        inputText = (EditText) findViewById(R.id.editText);
        inputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                editor.putString("input", inputText.getText().toString());
                editor.commit();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        inputText.setText(sp.getString("input", ""));

        hide = (CheckBox) findViewById(R.id.checkBox);
        hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hide", isChecked);
                editor.commit();
            }
        });

        hide.setChecked(sp.getBoolean("hide", false));

        history = (ListView) findViewById(R.id.history);
        storeInfo = (Spinner) findViewById(R.id.spinner);

        loadHistory();
        loadStoreInfo();
    }

    private void loadStoreInfo() {
        String[] data = getResources().getStringArray(R.array.store_info);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, data);
        storeInfo.setAdapter(adapter);
    }

    private void loadHistory() {
        String result = Utils.readFile(this, "history.txt");
        String[] rawData = result.split("\n");

        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < rawData.length; i++) {
            try {
                JSONObject object = new JSONObject(rawData[i]);
                String note = object.getString("note");
                String storeInfo = object.getString("store_info");
                JSONArray menu = object.getJSONArray("menu");

                Map<String, String> item = new HashMap<>();
                item.put("note", note);
                item.put("store_info", storeInfo);
                item.put("sum", "5");

                data.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] from = new String[]{"note", "store_info", "sum"};
        int[] to = new int[] {R.id.note, R.id.store_info, R.id.sum};
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listivew_item, from, to);

        history.setAdapter(adapter);
    }

    private void saveOrder() {
        ParseObject object = new ParseObject("Order");
        object.put("note", inputText.getText().toString());
        object.put("store_info", (String) storeInfo.getSelectedItem());
        if (drinkMenuResult != null) {
            try {
                object.put("menu", new JSONArray(drinkMenuResult));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        object.saveInBackground();
    }

    public void submit(View view) {
        String text = inputText.getText().toString();
        if (hide.isChecked()) {
            text = "*********";
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        saveOrder();
        loadHistory();

        inputText.setText("");
        drinkMenuResult = null;
    }

    public void goToDrinkMenu(View view) {
        String storeInfoString = (String) storeInfo.getSelectedItem();

        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        intent.putExtra("store_info", storeInfoString);
        startActivityForResult(intent, REQUEST_DRINK_MENU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DRINK_MENU) {
            if (resultCode == RESULT_OK) {
                drinkMenuResult = data.getStringExtra("result");
                Log.d("debug", drinkMenuResult);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
