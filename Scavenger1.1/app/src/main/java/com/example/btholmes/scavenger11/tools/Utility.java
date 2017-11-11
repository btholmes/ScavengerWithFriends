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

import com.example.btholmes.scavenger11.model.Game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static DatabaseReference mFirebaseDatabaseReference;
    public static FirebaseUser currentUser;
    public static ArrayList<Game> gameList;
    public static Context ctx;
    public static Utility utility;


    public static Utility getInstance(Context context){
        return utility == null ? new Utility(context) : utility;
    }


    public Utility (Context context){
        ctx = context;
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        gameList = new ArrayList<>();
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


    public static void addPhotoUrl(FirebaseUser user){
        String url = null;
        if(user.getPhotoUrl() != null){
            url = user.getPhotoUrl().toString();
        }
        if(url != null){
            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(url);
        }else{
            String defaultPic = "http://vvcexpl.com/wordpress/wp-content/uploads/2013/09/profile-default-male.png";
            mFirebaseDatabaseReference.child("userList").child(user.getUid()).child("photoUrl").setValue(defaultPic);
        }
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
