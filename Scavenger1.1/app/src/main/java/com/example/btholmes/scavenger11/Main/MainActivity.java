package com.example.btholmes.scavenger11.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.btholmes.scavenger11.application.DrawerActivity;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends DrawerActivity {


    /**
     * This activity will keep track of which viewpager page was open in on pause and on resume
     */
    private SharedPreferences vpPref;
    private int viewPagerPage;

    private String imageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        checkUserNull();
        if(user == null) return;

        /**
         * Setting badge count
         */
//        setBadgeCount(getApplicationContext(), 2);

        int count = getBadgeCount();
        setBadgeCount(this, count);
        checkFirebaseToken();
        Log.e("MainActivity : " , "Token is " + FirebaseInstanceId.getInstance().getToken());


    }




    private void checkFirebaseToken(){
        String token = getSharedPreferences("token", MODE_PRIVATE).getString("token", "false");
        if(token.equals("false")){
            try{
                FirebaseInstanceId.getInstance().deleteInstanceId();
                getSharedPreferences("token", MODE_PRIVATE).edit().putString("token", "").apply();
                FirebaseInstanceId.getInstance().getToken();
            }catch (Exception e){
                Log.e("fBaseToken", "Exception while trying to delete instance of FirebaseInstanceId");
                e.printStackTrace();
            }
        }else{
            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("userToken").setValue(token);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        vpPref = getSharedPreferences("viewPagerPage", MODE_PRIVATE);
        viewPagerPage = vpPref.getInt("viewPagerPage", 0);

        viewPager.setCurrentItem(viewPagerPage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int index = viewPager.getCurrentItem();
        getSharedPreferences("viewPagerPage", MODE_PRIVATE).edit().putInt("viewPagerPage", index).commit();
    }



}

