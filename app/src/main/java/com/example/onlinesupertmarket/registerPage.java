package com.example.onlinesupertmarket;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.onlinesupertmarket.DTO.UserDTO;
import com.example.onlinesupertmarket.DTO.UserRequest;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.User;
import com.example.onlinesupertmarket.Network.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

import static com.example.onlinesupertmarket.Utils.Utils.*;


public class registerPage extends AppCompatActivity {
    TextView registerTextView;
    EditText username;
    EditText firstName;
    EditText lastName;
    EditText email;
    Button userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        registerTextView = findViewById(R.id.signIn);
        username = findViewById(R.id.username);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        userRegister=findViewById(R.id.registerButton);

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(registerPage.this, WelcomePage.class);
            startActivity(intent);

        });

      userRegister.setOnClickListener(view -> {
          if (!hasEmptyFields()) {
              makePostRequest();
          } else {
              Toast.makeText(registerPage.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
          }
      });
    }



    public boolean hasEmptyFields() {
        boolean hasEmptyField = false;

        if (TextUtils.isEmpty(username.getText().toString())) {
            setError(username, true);
            hasEmptyField = true;
        } else {
            setError(username, false);
        }

        if (TextUtils.isEmpty(firstName.getText().toString())) {
            setError(firstName, true);
            hasEmptyField = true;
        } else {
            setError(firstName, false);
        }

        if (TextUtils.isEmpty(lastName.getText().toString())) {
            setError(lastName, true);
            hasEmptyField = true;
        } else {
            setError(lastName, false);
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            setError(email, true);
            hasEmptyField = true;
        } else {
            setError(email, false);
        }

        return hasEmptyField;
    }

    public void makePostRequest() {
        String usernameEditText = username.getText().toString();
        String firstNameEditText = firstName.getText().toString();
        String lastNameEditText = lastName.getText().toString();
        String emailEditText = email.getText().toString();

        UserRequest userRequest=new UserRequest(usernameEditText, firstNameEditText, lastNameEditText, emailEditText);


        String jsonBody = Convert.convertToJson(userRequest);

        String postUrl = apiUrl+userCreate+api_key;


        HttpClient.postRequest(postUrl, jsonBody, new Callback() {
            @Override
            public void onFailure(Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(registerPage.this, "Account failed creation", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    UserDTO userResponse=Convert.convertFromJson(responseBody,UserDTO.class);

                    DTOMapper<UserDTO, User> userDTOMapper = new DTOMapper<>(userDTO -> {
                        String name = userDTO.getUsername();
                        String hash = userDTO.getHash();
                        String password = userDTO.getSpoonacularPassword();
                        return new User(name, password, hash);
                    });
                    User user = userDTOMapper.map(userResponse);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            saveUserCredentials(user.getUsername(), user.getHash());
                            saveUserForWelcomePage(user.getUsername(),user.getSpoonacularPassword());

                            Toast.makeText(registerPage.this, "Account created with success", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    final String errorResponse = response.body().string();
                    runOnUiThread(() -> Toast.makeText(registerPage.this, "Account failed creation\n" + errorResponse, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }



    private void setError(EditText editText, boolean isError) {
        if (isError) {
            editText.setBackgroundResource(R.drawable.edittext_error_background);
        } else {
            editText.setBackground(null);
        }
    }
    private void saveUserCredentials(String username, String hash) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("hash", hash);
        editor.apply();
    }

    private void saveUserForWelcomePage(String username,String password){
        SharedPreferences sharedPreferences = getSharedPreferences("user_data_wp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("spoonacularPassword",password);
        editor.apply();
    }

}

