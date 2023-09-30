package com.example.onlinesupertmarket;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MenuPage extends AppCompatActivity {
    NavigationView navigationView;
    View hederView;
    TextView username;

    ImageView shop;
    ImageView recepie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        navigationView = findViewById(R.id.nav_view);
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        hederView=navigationView.getHeaderView(0);
        username=hederView.findViewById(R.id.nav_header_usename);
        String greetingUsername = "Hello, " + savedUsername+" !";
        username.setText(greetingUsername);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle menu item selection here
                int id = item.getItemId();
                if (id == R.id.nav_item1) {
                } else if (id == R.id.nav_item2) {
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        shop=findViewById(R.id.shoop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}