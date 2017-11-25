package com.example.btholmes.scavenger11.pushNotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;

import com.example.btholmes.scavenger11.R;
import com.example.btholmes.scavenger11.main.MainActivity;
import com.example.btholmes.scavenger11.model.PushNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class NotificationListener extends Service {

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseUser currentUser;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser  = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){
            final Query ref = mFirebaseDatabaseReference.child("userList").child(currentUser.getUid()).child("notification");

            if(ref != null){
                ref.addListenerForSingleValueEvent(new ValueEventListener() {

                    //This method is called whenever we change the value in firebase
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        PushNotification msg = snapshot.getValue(PushNotification.class);
                        if (msg == null || msg.getMsg().equals("none")){
                            return;
                        }
                        showNotification(msg);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void showNotification(PushNotification msg){
        //Creating a notification
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        try{
            builder.setLargeIcon(MediaStore.Images.Media.getBitmap(this.getContentResolver(), android.net.Uri.parse(msg.getSenderPhotoURL())));
        }catch (Exception e){
            e.printStackTrace();
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }
        builder.setContentTitle(msg.getTitle());
        builder.setContentText(msg.getMsg());
        builder.setContentInfo(msg.getSenderDisplayName() + msg.getSenderUID() + msg.getRecipientDisplayName() + msg.getRecipientUID());
        builder.setAutoCancel(true);
        //Vibration
        builder.setVibrate(new long[] { 1000, 1000 });
        //LED
        builder.setLights(Color.RED, 3000, 3000);
        //Ton
        /**
         * Below gets and sets default notification sounds
         */
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(random.nextInt(), builder.build());
//        PushNotification updateMsg = new PushNotification("none");
        mFirebaseDatabaseReference.child("userList").child(currentUser.getUid()).child("notification").removeValue();

    }
}