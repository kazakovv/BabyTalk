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
    protected Button sendMessageButton;
    protected EditText messageToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_partners, container, false);

        sendMessageButton = (Button) inflatedView.findViewById(R.id.sendMessageForTheDayButton);
        messageToSend = (EditText) inflatedView.findViewById(R.id.messageToSend);




        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SendPushMessages sadarza metoda za izprashtane na push
                SendPushMessages pushM = new SendPushMessages();

                ParseUser recepient = ParseUser.getCurrentUser();//izprashtam go na men si.
                String senderMessageText = ParseUser.getCurrentUser().getUsername() + " " +
                        getString(R.string.send_a_message_message); //niakoi ti izprati sabshtenie
                String message = messageToSend.getText().toString();
                pushM.sendPush(recepient, senderMessageText, ParseConstants.TYPE_PUSH_MESSAGE,message);
            }
        });


        return inflatedView;
    }


}
