package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddDogActivity extends AppCompatActivity {
    /**
     * User fills  in the prompts and adds a new dog to their profile.
     */
    EditText dogName, dogBreed, dogInfo, dogImage;

    String uuid;
    String name, owner, breed, info, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog);
        uuid = getIntent().getStringExtra("owner");
        setupButtons();
    }

    void setupButtons() {
        dogName = findViewById(R.id.dognameText);
        dogBreed = findViewById(R.id.breedText);
        dogInfo = findViewById(R.id.infoText);
        dogImage = findViewById(R.id.imageText);

        // User confirms adding a new dog.
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = dogName.getText().toString();
                breed = dogBreed.getText().toString();
                info = dogInfo.getText().toString();
                image = dogImage.getText().toString();

                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            if(Authentication.user.getUid().equals(uuid)) {
                                owner = Objects.requireNonNull(s.child("username").getValue()).toString();

                                // Assigns null values
                                if (info.equals("")) {
                                    info = "No Info";
                                }

                                if(image.equals("")) {
                                    image = "No Image";
                                }

                                // Check input is valid before submitting.
                                if (!name.equals("") && !breed.equals("")) {
                                    FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("dog_list").child(name)
                                            .setValue(new Dog(name, owner, breed, info, image));
                                    Toast.makeText(AddDogActivity.this, "Dog Added", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddDogActivity.this, ProfileActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(AddDogActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
