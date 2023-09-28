package com.example.onlinesupertmarket;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;


public class registerPage extends AppCompatActivity {
    TextView registerTextView;
    EditText username;
    EditText firstName;
    EditText lastName;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        registerTextView = findViewById(R.id.signIn);
        username = findViewById(R.id.username);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the new activity
                Intent intent = new Intent(registerPage.this, WelcomePage.class); // Replace with your activity names
                startActivity(intent);
            }
        });

    }

    public void onRegisterClick(View view) {
        try {
            isBlank();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void isBlank() {
        boolean hasError = false;

        if (TextUtils.isEmpty(username.getText().toString())) {
            setError(username, true);
            hasError = true;
        } else {
            setError(username, false);
        }

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            setError(firstName, true);
            hasError = true;
        } else {
            setError(firstName, false);
        }

        if (TextUtils.isEmpty(lastName.getText().toString())) {
            setError(lastName, true);
            hasError = true;
        } else {
            setError(lastName, false);
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            setError(email, true);
            hasError = true;
        } else {
            setError(email, false);
        }

        if (hasError) {
            throw new IllegalArgumentException("One or more fields are empty");
        }
    }

    private void setError(EditText editText, boolean isError) {
        if (isError) {
            editText.setBackgroundResource(R.drawable.edittext_error_background);
            editText.setBackground(null);
        }
    }
}

