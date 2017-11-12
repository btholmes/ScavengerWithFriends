package com.example.btholmes.scavenger11.application;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.btholmes.scavenger11.model.Analytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by btholmes on 11/11/17.
 */

/**
 * This activity handles all the analytics
 */
public class ScavengerActivity extends FirebaseUserActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    /**
     * Firebase and Google Anlytics
     */
    protected FirebaseAnalytics mFirebaseAnalytics;
    protected GoogleApiClient googleApiClient;
    protected Analytics analytics;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    protected void logFirebaseEvent(String event , Bundle bundle){

        mFirebaseAnalytics.setUserProperty("displayName", user.getDisplayName());
        mFirebaseAnalytics.setUserProperty("UID", user.getUid());

        mFirebaseAnalytics.logEvent(event, bundle);
    }



    protected GoogleApiClient getGoogleApiClient(){
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }

    protected Analytics getAnalytics() {
        return analytics;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
