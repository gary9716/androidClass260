package com.example.simpleui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private CheckBox hide;
    private ListView history;
    private Spinner storeInfo;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        String[] data = result.split("\n");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);

        history.setAdapter(adapter);
    }

    public void submit(View view) {
        String text = inputText.getText().toString();
        if (hide.isChecked()) {
            text = "*********";
        }
        inputText.setText("");
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        Utils.writeFile(this, "history.txt", text + "\n");
        loadHistory();
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
