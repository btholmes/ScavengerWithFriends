package com.example.btholmes.scavenger11.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.btholmes.scavenger11.login.LoginActivity;
import com.example.btholmes.scavenger11.model.RealmUser;
import com.example.btholmes.scavenger11.model.User;
import com.example.btholmes.scavenger11.tools.Utility;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;

/**
 * Created by btholmes on 11/11/17.
 */

/**
 * This just creates instances for the Firebase User, makes code a little cleaner in the main activity
 */
public class FirebaseUserActivity extends AppCompatActivity {


    protected User currentFirebaseUser;
    public static FirebaseAuth auth;
    public static FirebaseUser user;
    public static DatabaseReference mFirebaseDatabaseReference;
    public static FirebaseAuth.AuthStateListener firebaseAuthListner;
    protected FirebaseAnalytics mFirebaseAnalytics;
    public static Utility utility;
    public Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user  = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) goToLogin();
        else {
            setUpAuthListener();


            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            realm = Realm.getDefaultInstance();


            utility = Utility.getInstance(this);

            setFirebaseUser(new setUserCallback() {
                @Override
                public void onSuccess() {
                    Log.e("FirebaseUserSuccess", "Successfully retrieved current user from Firebase");

                }

                @Override
                public void onError() {
                    Log.e("FirebaseUserError", "Error getting current Firebase user from Firebase in main");
                }
            });

            /**
             * Start Badge Count Service, it extends intentService, and runs on a separate thread?
             */
//        startService(new Intent(ScavengerActivity.this, BadgeIntentService.class).putExtra("badgeCount", badgeCount));

            /**
             * Start Notification Service, it extends service and runs on the main thread
             */
//            startService(new Intent(FirebaseUserActivity.this, NotificationListener.class));

        }
    }


    public interface setUserCallback{
        void onSuccess();
        void onError();
    }

    protected void goToLogin(){
        Intent i = new Intent(FirebaseUserActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



    /**
     * Other instances override this method for def
     */
    protected void setUpAuthListener(){
        //SO user doesn't have to log in everytime
        firebaseAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    goToLogin();
                }
            }
        };
        auth.addAuthStateListener(firebaseAuthListner);
    }


    protected void setFirebaseUser(final setUserCallback finishedCallback){
        if(currentFirebaseUser == null){
            utility.getFirebaseUser(new Utility.getUserCallback() {
                @Override
                public void onSuccess(User currentUser) {
                    currentFirebaseUser = currentUser;
                    finishedCallback.onSuccess();
                    saveUserToRealm(currentUser);
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    protected void saveUserToRealm(final User currentFirebaseUser){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RealmUser.class).equalTo("uid", user.getUid()).findAll().deleteAllFromRealm();

                RealmUser obj = realm.createObject(RealmUser.class, currentFirebaseUser.getUid());
                obj.setDisplayName(currentFirebaseUser.getDisplayName());
//                obj.setUid(currentFirebaseUser.getUid());
                obj.setEmail(currentFirebaseUser.getEmail());
                obj.setPhotoUrl(currentFirebaseUser.getPhotoUrl());
                obj.setUserToken(currentFirebaseUser.getUserToken());
                obj.setWins(currentFirebaseUser.getWins());
                obj.setLosses(currentFirebaseUser.getLosses());
                realm.copyToRealmOrUpdate(obj);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("Success", "User saved to Realm successfully");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Error", "Could not save user to realm...");
            }
        });
    }




}
