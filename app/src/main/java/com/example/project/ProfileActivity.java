package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    EditText usernametv;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usernametv = findViewById(R.id.username);
        loadinfo();

        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authentication.signOut();
                Toast.makeText(ProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findViewById(R.id.add_dog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, AddDogActivity.class);
                i.putExtra("owner", Authentication.user.getUid());
                startActivity(i);
            }
        });

        usernametv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Authentication.user = Authentication.mAuth.getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            if(Authentication.user.getUid().equals(Objects.requireNonNull(s.child("uuid").getValue()).toString())) {
                                s.child("username").getRef().setValue(usernametv.getText().toString());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void loadinfo() {
        // get username
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Authentication.user = Authentication.mAuth.getCurrentUser();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if(Authentication.user.getUid().equals(Objects.requireNonNull(s.child("uuid").getValue()).toString())) {
                        usernametv.setText(Objects.requireNonNull(s.child("username").getValue()).toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // get dogs
        FirebaseDatabase.getInstance().getReference().child("users").child(Authentication.user.getUid()).child("dog_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Authentication.dogs = new ArrayList<>();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Authentication.dogs.add(new Dog(Objects.requireNonNull(s.child("name").getValue()).toString(),
                            Objects.requireNonNull(s.child("owner").getValue()).toString(),
                            Objects.requireNonNull(s.child("breed").getValue()).toString(),
                            Objects.requireNonNull(s.child("info").getValue()).toString(),
                            Objects.requireNonNull(s.child("image_url").getValue()).toString()));
                }

                lv = findViewById(R.id.dogListView);
                lv.setAdapter(new DogAdapter(ProfileActivity.this, Authentication.dogs));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
