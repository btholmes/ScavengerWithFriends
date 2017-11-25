package com.example.btholmes.scavenger11.pushNotifications;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FireIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        preferences.edit().putString("token", tkn).apply();

    }
}
