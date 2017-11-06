package com.example.btholmes.scavenger11.pushNotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
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


//Class extending service as it is a service that will run in background
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

        final Query ref = mFirebaseDatabaseReference.child("userList").child(currentUser.getUid()).child("pushNotifications");
        ref.addValueEventListener(new ValueEventListener() {

            //This method is called whenever we change the value in firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    PushNotification msg = snapshot.getValue(PushNotification.class);
                    if (msg.getMsg().equals("none")){
                        return;
                    }
                    showNotification(msg);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return START_STICKY;
    }


    private void showNotification(PushNotification msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(msg.getTitle());
        builder.setContentText(msg.getMsg());
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        PushNotification updateMsg = new PushNotification("none");
        mFirebaseDatabaseReference.child("userList").child(currentUser.getUid()).child("pushNotifications").setValue(updateMsg);

    }
}