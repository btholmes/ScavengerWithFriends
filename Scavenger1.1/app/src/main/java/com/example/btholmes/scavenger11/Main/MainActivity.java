package com.example.btholmes.scavenger11.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.btholmes.scavenger11.application.DrawerActivity;
import com.example.btholmes.scavenger11.tools.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;

public class MainActivity extends DrawerActivity {


    /**
     * This activity will keep track of which viewpager page was open in on pause and on resume
     */
    private SharedPreferences vpPref;
    private int viewPagerPage;

    private String imageURL;

    Utility utility = Utility.getInstance(this);
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        vpPref = getPreferences(MODE_PRIVATE);
//        viewPagerPage = vpPref.getInt("viewPagerPage", 0);
//
//        viewPager.setCurrentItem(viewPagerPage);
//        auth.addAuthStateListener(firebaseAuthListner);
//        viewPager.getAdapter().notifyDataSetChanged();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        int index = viewPager.getCurrentItem();
//        getPreferences(MODE_PRIVATE).edit().putInt("viewPagerPage", index).commit();
//    }


}

