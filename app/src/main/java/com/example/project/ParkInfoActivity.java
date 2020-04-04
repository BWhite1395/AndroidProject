package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class ParkInfoActivity extends AppCompatActivity {
    /**
     * Displays the park name, image, and list of dogs.
     */

    Menu optionsMenu;
    ListView lv;
    String parkAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_info);

        parkAddress = Objects.requireNonNull(getIntent().getExtras()).getString("address");

        TextView parkNameView = findViewById(R.id.parkNameView);
        parkNameView.setText(parkAddress);

        /*
        Button to add dog to park:
                If user is logged in open list of their dogs.
                If user is not logged in, prompt them to log in.
         */
        findViewById(R.id.add_to_park_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Authentication.mAuth.getCurrentUser() == null) {
                    if (optionsMenu != null) {
                        Authentication.onClickSignIn(ParkInfoActivity.this, optionsMenu);
                    } else {
                        Toast.makeText(ParkInfoActivity.this, "Must Be Logged In", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Intent i = new Intent(ParkInfoActivity.this, DogListActivity.class);
                    i.putExtra("address", parkAddress);
                    startActivity(i);
                }

            }
        });


        // Add dogs from database into an arraylist.

        FirebaseDatabase.getInstance().getReference().child("parks").child(parkAddress).child("dog_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Dog> dog_list = new ArrayList<>();

                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    dog_list.add(new Dog(Objects.requireNonNull(s.child("name").getValue()).toString(),
                            Objects.requireNonNull(s.child("owner").getValue()).toString(),
                            Objects.requireNonNull(s.child("breed").getValue()).toString(),
                            Objects.requireNonNull(s.child("info").getValue()).toString(),
                            Objects.requireNonNull(s.child("image_url").getValue()).toString()));
                }

                // Set the adapter to show the arraylist in the listview.
                lv = findViewById(R.id.dogListView);
                lv.setAdapter(new DogAdapterPark(ParkInfoActivity.this, dog_list));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
