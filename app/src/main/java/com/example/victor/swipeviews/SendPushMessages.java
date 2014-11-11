package com.example.victor.swipeviews;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

/**
 * Created by Victor on 11/11/2014.
 */
public class SendPushMessages {
    protected void sendPush(ParseUser recepient, String senderMessageText, String typeOfMessage,
                            String message) {

        // Create our Installation query
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo(ParseConstants.KEY_USER,
                recepient);

        //dobaviame dopalinetelnata info kam JSON object
        JSONObject obj;
        try {
            obj = new JSONObject();

            obj.put(ParseConstants.KEY_PUSH_MESSAGE, message); //tova e osnovnoto saobshtenie

            //zadava tipa push
            if(typeOfMessage.equals(ParseConstants.TYPE_PUSH_KISS)) {
                obj.put(ParseConstants.KEY_ACTION, ParseConstants.TYPE_PUSH_KISS);

            } else if (typeOfMessage.equals(ParseConstants.TYPE_PUSH_MESSAGE)) {
                obj.put(ParseConstants.KEY_ACTION, ParseConstants.TYPE_PUSH_MESSAGE);
                obj.put(ParseConstants.KEY_PUSH_MESSAGE, message);

            } else if (typeOfMessage.equals(ParseConstants.TYPE_PUSH_CALENDAR)) {
                obj.put(ParseConstants.KEY_ACTION, ParseConstants.TYPE_PUSH_CALENDAR);
            }


            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setData(obj); //dobaviame dopalnitelnite neshta kam saobshtenieto
            push.sendInBackground();

        } catch (Exception e) {
            //there was an error
        }

    }
}
