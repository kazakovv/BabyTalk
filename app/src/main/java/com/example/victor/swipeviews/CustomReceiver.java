package com.example.victor.swipeviews;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;




/**
 * Tova e class koito da obrabotva pristigashtite push notifications
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Vic", "Custom receiver onReceive started");
        if (intent == null) {
            Log.d("Vic", "onReceive started but intent is empty");
        } else {
            // izvlichame infoto

                String message = intent.getExtras().getString(ParseConstants.KEY_ALERT);
                showNotification(context,"You got a kiss", message);


        }
    }
    private void showNotification(Context context, String title, String message) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Main.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_person)
                        .setContentTitle(title)
                        .setContentText(message);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


    }

    }
