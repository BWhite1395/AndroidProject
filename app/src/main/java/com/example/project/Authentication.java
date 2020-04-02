package com.example.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class Authentication {

    static FirebaseAuth mAuth;
    private static DatabaseReference ref;
    static FirebaseUser user;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static Menu optionsMenu;

    static ArrayList<Dog> dogs = new ArrayList<>();

    static void updateUI(Menu optionsMenu) {
        if (mAuth.getCurrentUser() == null) {
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Sign In");
            }
        } else{
            if(optionsMenu != null) {
                optionsMenu.getItem(0).setTitle("Profile");
            }
        }
    }

    private static String email;
    private static String password;

    private static void createAccount(final String email, String password) {
        Authentication.email = email;
        Authentication.password = password;
        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        user = mAuth.getCurrentUser();
                        ref = FirebaseDatabase.getInstance().getReference();
                        assert user != null;
                        Profile p = new Profile(user.getUid(), email);
                        ref.child("users").child(user.getUid()).setValue(p);
                        signIn(Authentication.email, Authentication.password);
                        Authentication.email = null;
                        Authentication.password = null;
                    } else {
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(optionsMenu);
                    }
                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
                }
            });
            // [END create_user_with_email]
        } else {
            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Successfully Logged In",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(optionsMenu);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(optionsMenu);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    static void signOut() {
        mAuth.signOut();
        updateUI(optionsMenu);
    }

    static void onClickSignIn(Context c, Menu m) {
        Authentication.context = c;
        Authentication.optionsMenu = m;
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            //Authentication.signOut();
            Intent i = new Intent(context, ProfileActivity.class);
            context.startActivity(i);

        }else{
            LinearLayout layout = new LinearLayout(context);
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.signin);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText newemail = new EditText(context);
            newemail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            newemail.setHint(R.string.email);
            final EditText newpassword = new EditText(context);
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
                        Toast.makeText(context, "Sign In failed.",
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
                    Authentication.createAccount(email, password);
                    updateUI(optionsMenu);
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
