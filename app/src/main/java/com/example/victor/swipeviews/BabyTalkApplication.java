package com.example.victor.swipeviews;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class BabyTalkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "q8kuy8UbICvlNOFsPLNCAMTTFyrcLbmC1jNKLddU", "ApdaaWEaZSNdob8maokZyxRHJlQA6BoOfWdh9y3L");

        //instalaciata vrazva device sas application. Tova se pravi za da mozhe da se poluchavat push notifications
        //posle moga da napisha query, koiato da tarsi po parseuser ustroistvoto
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER, ParseUser.getCurrentUser());
        installation.saveInBackground();
    }
}
