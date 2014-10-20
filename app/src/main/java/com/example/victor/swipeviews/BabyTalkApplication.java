package com.example.victor.swipeviews;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Victor on 19/10/2014.
 */
public class BabyTalkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "q8kuy8UbICvlNOFsPLNCAMTTFyrcLbmC1jNKLddU", "ApdaaWEaZSNdob8maokZyxRHJlQA6BoOfWdh9y3L");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}
