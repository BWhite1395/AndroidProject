package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParkInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_info);
    }

    public void onClickDogs(View view) {
        Intent i = new Intent(this, DogPageActivity.class);
        startActivity(i);
    }
}
