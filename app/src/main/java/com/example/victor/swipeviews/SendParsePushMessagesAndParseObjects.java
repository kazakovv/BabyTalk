package com.example.victor.swipeviews;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Sadarzha edin metod za izprashtane na push notifications, koito moze da se izpolzva navsiakade
 * v programata.
 */
public class SendParsePushMessagesAndParseObjects {


    protected void sendPush( ParseUser recepient, String senderMessageText, String typeOfMessage,
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
            if (typeOfMessage.equals(ParseConstants.TYPE_PUSH_KISS)) {
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



//Tova se metodite, za izprashtane na saobshtenia, koito se izpolzvat v tutoriala
/*
    protected ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID,ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME,ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECEPIENT_IDS,getRecepientIDs());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        //razbiva fila na array ot bitove, za da go pratim prez parse.com
        byte[] fileBytes = FileHelper.getByteArrayFromFile(this,mMediaUri);

        if (fileBytes == null ) {
            return null;
        } else {
            if(mFileType.equals(ParseConstants.TYPE_IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            String fileName = FileHelper.getFileName(this,mMediaUri,mFileType);
            ParseFile file = new ParseFile(fileName,fileBytes);//***** sazdavame ParseFile********
            message.put(ParseConstants.KEY_FILE,file);

            return message;
        }

    }

    protected ArrayList<String> getRecepientIDs() {
        ArrayList<String> recepientIDs = new ArrayList<String>();
        for (int i=0; i < getListView().getCount(); i++) {
            if(getListView().isItemChecked(i)) {
                recepientIDs.add(mFriends.get(i).getObjectId());
            }
        }
        return recepientIDs;
    }
 */
    protected void send(ParseObject message, final Context context) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //success
                    Toast.makeText(context,R.string.message_successfully_sent,Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //builder.setMessage(R.string.error_sending_file)
                    builder.setMessage(e.toString())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }

}

