package com.example.btholmes.scavenger11.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.btholmes.scavenger11.login.LoginActivity;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by btholmes on 11/15/17.
 */

public class BasicActivity extends AppCompatActivity {


    protected FirebaseUser user;
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener firebaseAuthListner;
    protected DatabaseReference mFirebaseDatabaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Override method
     */
    protected void setUpAuthListener(){
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    checkNewUser();
                }
            }
        };
        auth.addAuthStateListener(firebaseAuthListner);

    }

    /**
     * Check if the facebook user has already been added to Firebase Database, if they have, go to main, else, add them.
     */
    private void checkNewUser(){
        user = auth.getCurrentUser();
        mFirebaseDatabaseReference.child("userList").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            /**
                             * Username already exists
                             */
                            goMainScreen();
                        } else {
                            /**
                             * Username does not exist
                             */
                            User newUser;
                            if(user.getDisplayName() != null){
                                newUser = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getEmail(), user.getDisplayName());
                            }else{
                                newUser = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getEmail());
//                        updateUserDisplayName(FirebaseAuth.getInstance().getCurrentUser(), user.getEmail());
                            }
                            addUserToDB(newUser);
                            addPhotoUrl(user);
                            goMainScreen();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
    /**
     * Adds User object to Firebase
     *
     * @param userToAdd
     */
    protected void addUserToDB(User userToAdd){
        mFirebaseDatabaseReference.child("userList").child(userToAdd.getUid()).setValue(userToAdd);
    }

    protected void addPhotoUrl(FirebaseUser user){
        String defaultPic = "http://vvcexpl.com/wordpress/wp-content/uploads/2013/09/profile-default-male.png";
        String url = null;
        if(user.getPhotoUrl() != null){
            url = user.getPhotoUrl().toString();
        }
        if(url != null){
            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(url);
        }else{
            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(defaultPic);
//            updateFirebasePhotoURL(user);
        }
    }

    protected void createUserWithEmailAndPassword(String email, String password, OnCompleteListener callback){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(BasicActivity.this, callback);
    }

    protected void goToLogin(){
        Intent i = new Intent(BasicActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    protected void goMainScreen(){
        Intent i = new Intent(BasicActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
