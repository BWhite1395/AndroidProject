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

    FirebaseAuth mAuth;
    MenuItem signButton;
    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;

        if (mAuth.getCurrentUser() == null) {
            //signButton.setTitle("Sign In");
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign In");
            }
        } else{
            //signButton.setTitle("Sign Out");
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign Out");
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ProfileButton) {
            onClickSignIn();
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

        }
    }
    public void updateUI() {
        if (mAuth.getCurrentUser() == null) {
            //signButton.setTitle("Sign In");
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign In");
            }
        } else{
            //signButton.setTitle(R.string.signout);
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign Out");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI();
    }

    public void onClickSignIn() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            signOut();
        }else{
            Context context = this;
            LinearLayout layout = new LinearLayout(context);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.signin);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText newemail = new EditText(this);
            newemail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            newemail.setHint(R.string.email);
            final EditText newpassword = new EditText(this);
            newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newpassword.setHint(R.string.pass);

            layout.addView(newemail);
            layout.addView(newpassword);
            builder.setView(layout);

            builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String email = newemail.getText().toString();
                    final String password = newpassword.getText().toString();
                    if(email.trim().equals("") || password.trim().equals("")) {
                        Toast.makeText(MainActivity.this, "Sign In failed.",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        signIn(email, password);

                    }
                }
            });
            builder.setNeutralButton("Create Account", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String email = newemail.getText().toString();
                    final String password = newpassword.getText().toString();
                    createAccount(email, password);
                    updateUI();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

    }
}




