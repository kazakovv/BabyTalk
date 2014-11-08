package com.example.victor.swipeviews;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentPartners extends Fragment {
    protected Button sendButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_partners, container, false);
        sendButton = (Button) inflatedView.findViewById(R.id.sendButton);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParsePush push = new ParsePush();
                push.setChannel("Victor");
                push.setMessage("1-2-1-2");
                push.sendInBackground();

                // Create our Installation query
                /*ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("channels", "Victor"); // Set the channel

                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage("Giants scored against the A's! It's now 2-2.");
                push.sendInBackground();
                */


            }
        });

        return inflatedView;
    }


}
