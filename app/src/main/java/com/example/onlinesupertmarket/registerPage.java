package com.example.onlinesupertmarket;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class registerPage extends AppCompatActivity {
EditText editText;
    TextView registerTextView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        registerTextView=findViewById(R.id.signIn);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the new activity
                Intent intent = new Intent(registerPage.this, WelcomePage.class); // Replace with your activity names
                startActivity(intent);
            }
        });

    }
}