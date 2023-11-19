package com.example.onlinesupertmarket.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.onlinesupertmarket.R;

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
        SharedPreferences sharedPreferences = getSharedPreferences("user_data_wp", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedSpoonacularPassword = sharedPreferences.getString("spoonacularPassword", "");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = userName.getText().toString().trim();
                String enteredPassword = passWord.getText().toString().trim();
                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(WelcomePage.this, "Username and password must not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WelcomePage.this, MenuPageNavActivity.class);
                    startActivity(intent);
                }
            }
        });
        registerTextView=findViewById(R.id.registerText);
        registerTextView.setOnClickListener(v -> {
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
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("user_data_wp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}