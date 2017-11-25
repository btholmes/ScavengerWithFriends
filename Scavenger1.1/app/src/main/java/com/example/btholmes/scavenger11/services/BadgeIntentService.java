package com.example.btholmes.scavenger11.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class BadgeIntentService extends IntentService {

    private int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
//            /**
//             * Update shared prefs with new badgecount
//             */
//            int badgeCount = intent.getIntExtra("badgeCount", 0);
//            getSharedPreferences("Notifications", MODE_PRIVATE).edit().putInt("badgeCount", badgeCount).apply();
//
//            mNotificationManager.cancel(notificationId);
//            notificationId++;
//
//            Notification.Builder builder = new Notification.Builder(getApplicationContext())
//                .setContentTitle("")
//                .setContentText("")
//                .setSmallIcon(R.mipmap.ic_launcher);
//            Notification notification = builder.build();
//            ShortcutBadger.applyNotification(getApplicationContext(), notification, badgeCount);
//            mNotificationManager.notify(notificationId, notification);
        }
    }
}
