package com.example.btholmes.scavenger11.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by btholmes on 11/11/17.
 */

/**
 * This just creates instances for the Firebase User, makes code a little cleaner in the main activity
 */
public class FirebaseUserActivity extends AppCompatActivity {


    protected FirebaseAuth auth;
    protected FirebaseUser user;
    protected DatabaseReference mFirebaseDatabaseReference;
    protected FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user  = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }



}
