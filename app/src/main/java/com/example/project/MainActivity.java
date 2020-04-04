package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * Contains the map with clickable markers at each dog park.
     */

    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    double lon = -123.116226;
    double lat = 49.246292;

    // Arraylist of parks.
    ArrayList<DogPark> dogParkArrayList = new ArrayList<>();

    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the map.
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

        // When marker is clicked, go to the park's info page.
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
            // Add the markers to the map by reading address of the parks.
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
                                .title(dp.getAddress()));
                        marker.setTag(dp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Authentication.updateUI(optionsMenu);
    }


}




