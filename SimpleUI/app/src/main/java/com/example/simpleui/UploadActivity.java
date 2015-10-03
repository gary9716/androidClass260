package com.example.simpleui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        final EditText studentIDEditText = (EditText)findViewById(R.id.studentIDEditText);
        final EditText studentEmailEditText = (EditText)findViewById(R.id.studentEmailEditText);
        final Button uploadBtn = ((Button) findViewById(R.id.uploadButton));
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBtn.setClickable(false);
                ParseObject obj = new ParseObject(ProjectConfig.HW4ParseClassName);
                obj.put(ProjectConfig.studentIDKey, studentIDEditText.getText().toString());
                obj.put(ProjectConfig.studentEmailKey, studentEmailEditText.getText().toString());
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) { //successfully saved
                            Toast.makeText(UploadActivity.this,
                                    "upload successfully",
                                    Toast.LENGTH_LONG).show();
                        } else { //failed
                            Toast.makeText(UploadActivity.this,
                                    e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        uploadBtn.setClickable(true);
                    }
                });
            }
        });
    }

}
