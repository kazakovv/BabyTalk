package com.example.victor.swipeviews;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class FragmentPartners extends Fragment {
    protected Button sendButton;
    protected EditText messageToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_partners, container, false);

        sendButton = (Button) inflatedView.findViewById(R.id.sendButton);
        messageToSend = (EditText) inflatedView.findViewById(R.id.messageToSend);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("user", ParseUser.getCurrentUser());
                installation.put("message",messageToSend.getText().toString());
                installation.saveInBackground();

                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("user",  ParseUser.getCurrentUser()); //izprashtam go na men si
                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage("Willie Hayes injured by own pop fly.");
                push.sendInBackground();
            }
        });

        return inflatedView;
    }


}
