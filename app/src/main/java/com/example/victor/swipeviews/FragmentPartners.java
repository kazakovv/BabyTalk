package com.example.victor.swipeviews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;




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
                //instalaciata vrazva device sas application
                //posle moga da napisha query, koiato da tarsi po parseuser ustroistvoto
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put(ParseConstants.KEY_USER, ParseUser.getCurrentUser());
                installation.saveInBackground();

                // Create our Installation query
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo(ParseConstants.KEY_USER,
                        ParseUser.getCurrentUser()); //izprashtam go na men si
                //dobaviame dopalinetelnata info kam JSON object
                JSONObject obj;
                try {
                    obj = new JSONObject();
                    obj.put(ParseConstants.KEY_ALERT, "Maria set you a kiss"); //tova e osnovnoto saobshtenie
                    obj.put(ParseConstants.KEY_ACTION,ParseConstants.TYPE_PUSH_KISS); //zadava tipa push


                    // Send push notification to query
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setData(obj); //dobaviame dopalnitelnite neshta kam saobshtenieto
                    push.sendInBackground();
                } catch (Exception e) {
                //there was an error
                }

            }
        });

        return inflatedView;
    }


}
