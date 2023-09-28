package com.example.onlinesupertmarket;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomePage extends AppCompatActivity {
    Button button;
    TextView registerTextView ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(WelcomePage.this, registerPage.class);
                startActivity(intent);
            }
        });*/
        registerTextView=findViewById(R.id.registerText);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the new activity
                Intent intent = new Intent(WelcomePage.this, registerPage.class); // Replace with your activity names
                startActivity(intent);
            }
        });

    }
}