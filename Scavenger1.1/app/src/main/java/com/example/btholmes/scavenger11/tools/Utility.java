package com.example.btholmes.scavenger11.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.example.btholmes.scavenger11.application.ScavengerActivity;
import com.example.btholmes.scavenger11.model.Friend;
import com.example.btholmes.scavenger11.model.Game;
import com.example.btholmes.scavenger11.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Utility extends ScavengerActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static ArrayList<Game> gameList;
    public static ArrayList<Friend> friendList;
    public static Context ctx;
    public static Utility utility;
    public static User currentUser;


    public static Utility getInstance(Context context){
        return utility == null ? new Utility(context) : utility;
    }


    public Utility (Context context){
        ctx = context;
        gameList = new ArrayList<>();
        friendList = new ArrayList<>();
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Utility.currentUser = currentUser;
    }

    /**
     * This will return null if user has no games list in Firebase
     * @param callback
     */
    public static void populateGames(final gameCallback callback) {

//
//        final Query ref = mFirebaseDatabaseReference.child("userList").child(currentUser.getUid()).child("games");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<HashMap<String, Game>> t = new GenericTypeIndicator<HashMap<String, Game>>() {
//                    @Override
//                    public int hashCode() {
//                        return super.hashCode();
//                    }
//                };
//                gameList = new ArrayList<>(dataSnapshot.getValue(t).values());
//                callback.onSuccess(gameList);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });

    }

    public interface getUserCallback{
        void onSuccess(User currentUser);
        void onError();
    }

    /**
     * This is called by main activity to retrieve User from firebase, then save the User to realm. Happens in OnCreate
     * @param callback
     */
    public static void getFirebaseUser(final getUserCallback callback){
        //
        final Query ref = mFirebaseDatabaseReference.child("userList").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               User person = dataSnapshot.getValue(User.class);
               currentUser = person;
               callback.onSuccess(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    /**
     * This function will get an arrayList of all friends from the database
     * @param callback
     */
    public static void populateFriends(final friendsCallback callback){


    }






    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
