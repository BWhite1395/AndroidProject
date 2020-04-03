package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    double lon = -123.116226;
    double lat = 49.246292;

    ArrayList<DogPark> dogParkArrayList = new ArrayList<>();


    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Authentication.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;

        if (Authentication.mAuth.getCurrentUser() == null) {
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign In");
            }
        } else{
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Profile");
                Authentication.user = Authentication.mAuth.getCurrentUser();
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ProfileButton) {
            Authentication.onClickSignIn(MainActivity.this, optionsMenu);
        }

        return super.onOptionsItemSelected(item);
    }
  
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                DogPark park = (DogPark) marker.getTag();
                Intent i = new Intent(MainActivity.this, ParkInfoActivity.class);
                assert park != null;
                i.putExtra("address", park.getAddress());
                startActivity(i);
                return false;
            }
        });

        JsonHandle jh = new JsonHandle();
        jh.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class JsonHandle extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            String jsonString;
            try {
                jsonString = new BufferedReader(new InputStreamReader(getAssets().open("dog-off-leash-parks.json"))).readLine();
                jsonString = "{\"dogParkList\":" + jsonString + "}";
                Gson gson = new Gson();
                DogParkList parksList = gson.fromJson(jsonString, DogParkList.class);
                dogParkArrayList = parksList.getParks();
                for (DogPark dp : dogParkArrayList) {
                    try {
                        List<Address> address = new Geocoder(MainActivity.this).getFromLocationName(dp.getAddress(), 5);
                        assert address != null;
                        Address loc = address.get(0);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                                // change title to include number of dogs
                                .title(dp.getAddress()));
                        marker.setTag(dp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            FirebaseDatabase.getInstance().getReference().child("parks").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for  (DogPark dp : dogParkArrayList) {
//                        FirebaseDatabase.getInstance().getReference().child("parks").child(dp.getAddress()).setValue(null);
//                        FirebaseDatabase.getInstance().getReference().child("parks").child(dp.getAddress()).child("dog_list").setValue(null);
//                        for (DataSnapshot s : dataSnapshot.getChildren()) {
//                            if (!s.child(dp.getAddress()).exists()) {
//                                // if the park is not in the database, add it
//                                FirebaseDatabase.getInstance().getReference().child("parks").child(dp.getAddress()).setValue("null");
//                                FirebaseDatabase.getInstance().getReference().child("parks").child(dp.getAddress()).child("dog_list").setValue("null");
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = Authentication.mAuth.getCurrentUser();
        Authentication.updateUI(optionsMenu);
    }


}




