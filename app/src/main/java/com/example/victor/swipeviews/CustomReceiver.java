package com.example.victor.swipeviews;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import java.util.Iterator;
import android.content.Context;
import android.content.Intent;
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
        Toast.makeText(context, "push received", Toast.LENGTH_LONG).show();
        Log.d("Vic", "Custom receiver onReceive started");
        showNotification(context);
        if(intent == null) {
        Log.d("Vic","onReceive started but intent is empty");
        } else {
        Log.d("Vic","onReceive started and intent is not null");
            Toast.makeText(context, "intent received", Toast.LENGTH_LONG).show();
            String msg = intent.getStringExtra("message");
            Toast.makeText(context, "The message is " + msg, Toast.LENGTH_LONG).show();

        }
    }

    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Main.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_action_person)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());


    }

    }
