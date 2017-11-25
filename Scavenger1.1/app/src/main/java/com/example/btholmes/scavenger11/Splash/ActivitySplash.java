package com.example.btholmes.scavenger11.splash;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.activities.BasicActivity;
import com.example.btholmes.scavenger11.data.Tools;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySplash extends BasicActivity {


    /**
     * Checks if user is logged in, if they are it gets their information and goes to Main, otherwise goes to login
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindLogo();

        setUpAuthListener();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            goMainScreen();
        }else{
            goToLogin();
        }

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }



//    private void verifySavedinDatabase(){
//        user = auth.getCurrentUser();
//
//        goMainScreen();
//
//    }

//protected void checkUserNull(){
//    if (user == null) {
//        startActivity(new Intent(ActivitySplash.this, LoginActivity.class));
//        finish();
//    }
//    else{
//        addUserToDB(user);
//        addPhotoUrl(user);
//
//        final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid());
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                if(dataSnapshot.getKey().equals("pushNotifications")){
//                    startService(new Intent(ScavengerActivity.this, NotificationListener.class));
////        startService(new Intent(this, photoChangeService.class));
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//}

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
