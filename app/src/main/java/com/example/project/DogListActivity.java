package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DogListActivity extends AppCompatActivity {
    /**
     * Displays the list of user's dogs to add to the current park.
     */

    ListView lv;
    Dog currdog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);

        Authentication.user = Authentication.mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").child(Authentication.user.getUid()).child("dog_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Add dogs to from database to arraylist.

                Authentication.dogs = new ArrayList<>();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Authentication.dogs.add(new Dog(Objects.requireNonNull(s.child("name").getValue()).toString(),
                            Objects.requireNonNull(s.child("owner").getValue()).toString(),
                            Objects.requireNonNull(s.child("breed").getValue()).toString(),
                            Objects.requireNonNull(s.child("info").getValue()).toString(),
                            Objects.requireNonNull(s.child("image_url").getValue()).toString()));
                }

                // Populate listview with dog adapter.

                lv = findViewById(R.id.doglist);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // dog clicked
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        currdog = (Dog) parent.getItemAtPosition(position);
                        FirebaseDatabase.getInstance().getReference().child("parks")
                                .child(Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("address")))
                                .child("dog_list").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot s : dataSnapshot.getChildren()) {
                                    if (Objects.requireNonNull(s.child("name").getValue()).toString().equals(currdog.getName())
                                            && Objects.requireNonNull(s.child("breed").getValue()).toString().equals(currdog.getBreed())
                                            && Objects.requireNonNull(s.child("info").getValue()).toString().equals(currdog.getInfo())
                                            && Objects.requireNonNull(s.child("image_url").getValue()).toString().equals(currdog.getImage_url())
                                            && Objects.requireNonNull(s.child("owner").getValue()).toString().equals(currdog.getOwner())) {
                                        Toast.makeText(DogListActivity.this, "Dog Already Added!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                dataSnapshot.getRef().child(currdog.getName()).setValue(currdog);
                                Toast.makeText(DogListActivity.this, "Added " + currdog.getName()
                                        + " to " + ((getIntent().getExtras()).getString("address")), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                lv.setAdapter(new DogAdapter(DogListActivity.this, Authentication.dogs));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
