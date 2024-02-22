package com.example.talkingclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Intent to open activity when notification is clicked
        Intent i = new Intent(context, NotificationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Putting Notification Here
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TalkingClock1.0") // this channel id should remain same as in Add_alarm.java
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm is Ringing")
                .setContentText("Press to see the Activity")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        // Check if the app has the required permission
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            // Handle the lack of permission here, for example by logging an error or returning early
            return;
        }

        notificationManagerCompat.notify(123, builder.build());
    }
}
