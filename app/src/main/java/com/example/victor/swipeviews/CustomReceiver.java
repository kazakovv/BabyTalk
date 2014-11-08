package com.example.victor.swipeviews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Tova e class koito da obrabotva pristigashtite push notifications
 */
public class CustomReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "push received", Toast.LENGTH_LONG).show();

    }



}
