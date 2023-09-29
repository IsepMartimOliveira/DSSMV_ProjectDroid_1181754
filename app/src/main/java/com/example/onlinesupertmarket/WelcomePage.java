package com.example.onlinesupertmarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomePage extends AppCompatActivity {
    Button button;
    TextView registerTextView ;
    EditText userName;
    EditText passWord;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        userName=findViewById(R.id.username);
        passWord=findViewById(R.id.password);
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedSpoonacularPassword = sharedPreferences.getString("spoonacularPassword", "");

        String username = getIntent().getStringExtra("username");
        String spoonacularPassword = getIntent().getStringExtra("spoonacularPassword");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(WelcomePage.this, MenuPage.class);
                startActivity(intent);
            }
        });
        registerTextView=findViewById(R.id.registerText);
        registerTextView.setOnClickListener(v -> {
            // Create an Intent to navigate to the new activity
            Intent intent = new Intent(WelcomePage.this, registerPage.class); // Replace with your activity names
            startActivity(intent);
        });
        if (!savedUsername.isEmpty()) {
            userName.setText(savedUsername);
        }

        if (!savedSpoonacularPassword.isEmpty()) {
            passWord.setText(savedSpoonacularPassword);
        }
    }
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}