package com.example.victor.swipeviews;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;


public class BabyTalkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "q8kuy8UbICvlNOFsPLNCAMTTFyrcLbmC1jNKLddU", "ApdaaWEaZSNdob8maokZyxRHJlQA6BoOfWdh9y3L");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        PushService.setDefaultPushCallback(this, Main.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
