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
    protected Button sendKissButton;
    protected Button sendMessageButton;
    protected EditText messageToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_partners, container, false);

        sendKissButton = (Button) inflatedView.findViewById(R.id.sendButton);
        sendMessageButton = (Button) inflatedView.findViewById(R.id.sendMessageForTheDayButton);
        messageToSend = (EditText) inflatedView.findViewById(R.id.messageToSend);


        sendKissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser recepient = ParseUser.getCurrentUser();//izprashtam go na men si.
                String message = ParseUser.getCurrentUser().getUsername() + " " +
                        getString(R.string.send_a_kiss_message); //niakoi ti izprati celuvka
                sendPush(recepient,message,ParseConstants.TYPE_PUSH_KISS,"");

            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser recepient = ParseUser.getCurrentUser();//izprashtam go na men si.
                String senderMessageText = ParseUser.getCurrentUser().getUsername() + " " +
                        getString(R.string.send_a_message_message); //niakoi ti izprati sabshtenie
                String message = messageToSend.getText().toString();
                sendPush(recepient, senderMessageText, ParseConstants.TYPE_PUSH_MESSAGE,message);
            }
        });


        return inflatedView;
    }

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
