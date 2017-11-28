package com.example.btholmes.scavenger11.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.Analytics;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.model.PushNotification;
import com.example.btholmes.scavenger11.model.User;
import com.example.btholmes.scavenger11.tools.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import me.leolin.shortcutbadger.ShortcutBadger;

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
    protected GoogleApiClient googleApiClient;
    protected Analytics analytics;
    protected int badgeCount = 0;
    protected SharedPreferences preferences;
    protected final String SHARED_PREFERENCES_NOTIFICATIONS_FILE = "Notifications";
    protected final String NEW_GAME_NOTIFICATION = "NewGameNotif";
    protected final String NEW_CHAT_NOTIFICATION = "NewChatNotif";
    protected final String NEW_NOTIFICATION_NOTIFICATION= "NewNotifNotif";
    protected final String BADGE_COUNT = "badgeCount";

    protected Realm realm;
    protected Utility utility;



    private static final String USERLIST_CHILD = "userList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
//        realm = Realm.getDefaultInstance();

        preferences = getSharedPreferences(SHARED_PREFERENCES_NOTIFICATIONS_FILE, MODE_PRIVATE);
        badgeCount = preferences.getInt(BADGE_COUNT, 0);



    }




//    /**
//     * Sets display name as email for the time being
//     * @param user
//     */
//    protected void updateUserDisplayName(FirebaseUser user, String displayName){
//        if(user.getDisplayName() == null){
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(displayName)
//                    .build();
//            user.updateProfile(profileUpdates);
//            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("displayName").setValue(displayName);
//        }
//    }







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



    /**
     * Callback ensures that a valid user object is returned from firebase, if it isn't, then user doesn't exist in database.
     */
    public interface getUserCallback{
        void onSuccess(User currentUser);
        void onError();
    }

    protected void getUserFromFirebase(final getUserCallback callback){
        DatabaseReference ref = mFirebaseDatabaseReference.child("userList");

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
                    callback.onSuccess(currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    protected void verifyUserUrlAndDisplayName(FirebaseUser user){
//
//        if(user!= null){
//            if(user.getDisplayName() == null){
//                updateUserDisplayName(user, user.getEmail());
//            }
//            if(user.getPhotoUrl() == null){
//                updateFirebasePhotoURL(user);
//            }
//            goMainScreen();
//        }else{
//            Toast.makeText(this, "Something went wrong in LoginActivity VerifyUserURL", Toast.LENGTH_SHORT).show();
//        }
//    }


//    protected void updateFirebasePhotoURL(FirebaseUser user){
//        if(user != null){
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setPhotoUri(android.net.Uri.parse(defaultPic))
//                    .build();
//            user.updateProfile(profileUpdates);
//            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(defaultPic);
//        }else{
//            goToLogin();
//        }
//    }






    protected void addUserToDB(FirebaseUser userToAdd){
        mFirebaseDatabaseReference.child("userList").child(userToAdd.getUid()).child("uid").setValue(userToAdd.getUid());
    }

    /**
     * Below code got moved to its own service
     */
//    protected void addTokenToDB(){
//        String tkn = FirebaseInstanceId.getInstance().getToken();
//        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("userToken").setValue(tkn);
//    }



    protected void goMainScreen(){
        Intent i = new Intent(ScavengerActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


    protected int getBadgeCount(){
        return getSharedPreferences(SHARED_PREFERENCES_NOTIFICATIONS_FILE, MODE_PRIVATE).getInt(BADGE_COUNT, 0);
    }

    protected void setBadgeCountStartUp(Context context, int count){
        setBadgeCount(context, count);
    }

    protected void setBadgeCountPreferences(int count){
        preferences.edit().putInt(BADGE_COUNT, count).apply();
    }

    protected void removeBadgeCount(Context context){
        ShortcutBadger.removeCount(context); //for 1.1.4+
        setBadgeCountPreferences(0);


//        ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3
    }
    protected void setBadgeCount(Context context, int badgeCount){
        ShortcutBadger.applyCount(context, badgeCount); //for 1.1.4+
        setBadgeCountPreferences(0);
//        ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3

    }

    protected int getGameNotifications(){
        return getSharedPreferences(SHARED_PREFERENCES_NOTIFICATIONS_FILE, MODE_PRIVATE).getInt(NEW_GAME_NOTIFICATION, 0);
    }
    protected int getMessagesNotifications(){
        return getSharedPreferences(SHARED_PREFERENCES_NOTIFICATIONS_FILE, MODE_PRIVATE).getInt(NEW_CHAT_NOTIFICATION, 0);
    }
    protected int getNotificationNotifications(){
        return getSharedPreferences(SHARED_PREFERENCES_NOTIFICATIONS_FILE, MODE_PRIVATE).getInt(NEW_NOTIFICATION_NOTIFICATION, 0);
    }

    protected void setGameNotifications(int count){
        preferences.edit().putInt(NEW_GAME_NOTIFICATION, count).apply();
    }

    protected void setMessageNotifications(int count){
        preferences.edit().putInt(NEW_CHAT_NOTIFICATION, count).apply();
    }

    protected void setNotificationNotifications(int count){
        preferences.edit().putInt(NEW_NOTIFICATION_NOTIFICATION, count).apply();
    }

    protected void createPushNotification(User friend){

        String challengerName = currentFirebaseUser.getDisplayName();
        String userUID = currentFirebaseUser.getUid();
        String userDisplayName = currentFirebaseUser.getDisplayName();
        String friendUID = friend.getUid();
        String friendDisplayName = friend.getDisplayName();
        String url = currentFirebaseUser.getPhotoUrl();

        PushNotification msg = new PushNotification(
                "Youve Been Challenged!" ,
                challengerName + " Challenged you",
                userUID,
                userDisplayName,
                friendUID,
                friendDisplayName,
                url);

        mFirebaseDatabaseReference.child("userList").child(friend.getUid()).child("notification").setValue(msg);
    }

    /**
     * This will return a random list of words
     * @return
     */
    protected List<String> getWordList(){
        return new ArrayList<String>(Arrays.asList("Lion", "Coffee", "Peace"));
    }

    protected Game createGame(User friend){
        return new Game(user.getUid(), friend.getUid(), getWordList());
    }

    protected void addGame(Game game, User friend){
        mFirebaseDatabaseReference.child("userList").child(friend.getUid()).child("games").child(game.getGameID()).setValue(game);
        mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("games").child(game.getGameID()).setValue(game);
    }




}
