package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ParkInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_info);

        String parkAddress = getIntent().getExtras().getString("address");

        TextView parkNameView = findViewById(R.id.parkNameView);
        parkNameView.setText(parkAddress);
    }

    public void onClickDogs(View view) {
        Intent i = new Intent(this, DogPageActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_park_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.DogsButton) {
            startActivity(new Intent(this, ParkDogsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
