package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditDogActivity extends AppCompatActivity {
    /**
     * Edit a user's dog information.
     */

    EditText dogBreed, dogInfo, dogImage;

    String name, breed, info, image, uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog);

        setupButtons();
    }

    void setupButtons() {
        dogBreed = findViewById(R.id.breedText);
        dogInfo = findViewById(R.id.infoText);
        dogImage = findViewById(R.id.imageText);
        TextView dogName = findViewById(R.id.dognameView);

        dogName.setText(getIntent().getStringExtra("name"));
        dogBreed.setText(getIntent().getStringExtra("breed"));
        dogInfo.setText(getIntent().getStringExtra("info"));
        dogImage.setText(getIntent().getStringExtra("image"));

        uuid = getIntent().getStringExtra("uuid");

        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("dog_list")
                        .child(Objects.requireNonNull(getIntent().getStringExtra("name")))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot s = dataSnapshot;
                        name = getIntent().getStringExtra("name");
                        breed = dogBreed.getText().toString();
                        info = dogInfo.getText().toString();
                        image = dogImage.getText().toString();

                        // assigns default values
                        if (info.equals("")) {
                            info = "No Info";
                        }

                        if (image.equals("")) {
                            image = "No Image";
                        }

                        // get the right dog from the database and update it's info
                        if(Objects.requireNonNull(s.child("name").getValue()).toString().equals(getIntent().getStringExtra("name"))
                            && Objects.requireNonNull(s.child("breed").getValue()).toString().equals(getIntent().getStringExtra("breed"))
                            && Objects.requireNonNull(s.child("info").getValue()).toString().equals(getIntent().getStringExtra("info"))
                            && Objects.requireNonNull(s.child("image_url").getValue()).toString().equals(getIntent().getStringExtra("image"))) {

                            if (!name.equals("") && !breed.equals("")) {
                                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("dog_list").child(name);
                                df.child("breed").setValue(breed);
                                df.child("info").setValue(info);
                                df.child("image_url").setValue(image);
                                Toast.makeText(EditDogActivity.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(EditDogActivity.this, ProfileActivity.class));
                            } else {
                                Toast.makeText(EditDogActivity.this, "Invalid Inputs", Toast.LENGTH_SHORT).show();
                                finish();
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

        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("dog_list").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {

                            // get the right dog from the database and delete it
                            if(Objects.requireNonNull(s.child("name").getValue()).toString().equals(getIntent().getStringExtra("name"))
                                    && Objects.requireNonNull(s.child("breed").getValue()).toString().equals(getIntent().getStringExtra("breed"))
                                    && Objects.requireNonNull(s.child("info").getValue()).toString().equals(getIntent().getStringExtra("info"))
                                    && Objects.requireNonNull(s.child("image_url").getValue()).toString().equals(getIntent().getStringExtra("image"))) {
                                name = getIntent().getStringExtra("name");
                                FirebaseDatabase.getInstance().getReference().child("users").child(uuid).child("dog_list").child(name).removeValue();
                                Toast.makeText(EditDogActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EditDogActivity.this, ProfileActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });



    }
}
