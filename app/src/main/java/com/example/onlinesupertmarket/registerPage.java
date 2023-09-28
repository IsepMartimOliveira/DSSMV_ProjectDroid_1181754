package com.example.onlinesupertmarket;

import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class registerPage extends AppCompatActivity {
EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        String title="Username";
        EditText et = (EditText) findViewById(R.id.username);
        et.setText(et.getText() + title);
    }
}