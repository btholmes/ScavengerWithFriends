package com.example.btholmes.scavenger11.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.data.Tools;
import com.example.btholmes.scavenger11.login.LoginActivity;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActivitySplash extends AppCompatActivity {

    /**
     * Checks if user is logged in, if they are it gets their information and goes to Main
     * @param savedInstanceState
     */

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener fAuthStateListener;
    DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindLogo();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        fAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    goToLogin();
                }else{
                    getUserFromFirebase(new getUserCallback() {
                        @Override
                        public void onSuccess() {
                            Log.e("user", user.getDisplayName() + " : " + user.getEmail() + " : " + user.getUid());
                            goMainScreen();
                        }

                        @Override
                        public void onError() {
                            goToLogin();
                        }
                    });
                }
            }
        };
        auth.addAuthStateListener(fAuthStateListener);
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    /**
     * Callback ensures that a valid user object is returned from firebase, if it isn't, then user doesn't exist in database.
     */
    public interface getUserCallback{
        public void onSuccess( );
        public void onError();
    }

    private void getUserFromFirebase(final getUserCallback callback){
        DatabaseReference ref = mFirebaseDatabaseReference.child("userList");

//        ref.equalTo(user .getUid()).

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> userSpecs = new GenericTypeIndicator<HashMap<String, String>>(){};
                GenericTypeIndicator<User> user = new GenericTypeIndicator<User>(){};

//                HashMap<String, String> currentUser = dataSnapshot.getValue(userSpecs);
                User currentUser = dataSnapshot.getValue(user);
                if(currentUser == null){
                    callback.onError();
                }else{
                    callback.onSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void goToLogin(){
        Intent i = new Intent(ActivitySplash.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void goMainScreen(){
        Intent i = new Intent(ActivitySplash.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void bindLogo(){
        // Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.splash);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(700);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(700);
        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                splash.startAnimation(animation2);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        splash.startAnimation(animation1);
    }
}
