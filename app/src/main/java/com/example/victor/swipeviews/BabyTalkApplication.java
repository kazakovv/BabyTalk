package com.example.victor.swipeviews;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;


public class BabyTalkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "q8kuy8UbICvlNOFsPLNCAMTTFyrcLbmC1jNKLddU", "ApdaaWEaZSNdob8maokZyxRHJlQA6BoOfWdh9y3L");

        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
