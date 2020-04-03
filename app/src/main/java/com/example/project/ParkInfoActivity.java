package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class ParkInfoActivity extends AppCompatActivity {

    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_info);

        String parkAddress = Objects.requireNonNull(getIntent().getExtras()).getString("address");

        TextView parkNameView = findViewById(R.id.parkNameView);
        parkNameView.setText(parkAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_park_info, menu);
        optionsMenu = menu;

        if (Authentication.mAuth.getCurrentUser() == null) {
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign In");
            }
        } else{
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Profile");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ProfileButton) {
            Authentication.onClickSignIn(ParkInfoActivity.this, optionsMenu);
        }

        return super.onOptionsItemSelected(item);
    }
}
