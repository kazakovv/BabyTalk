package com.example.victor.swipeviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SendMessage extends Activity {
    EditText messageToSend;
    ImageView mUploadMedia;


    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PHOTO_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST =3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;

    protected Uri mMediaUri;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; //1024*1024 = 1MB

    public static final String TAG = Main.class.getSimpleName();

    //onCLick listener za kamerata
    protected DialogInterface.OnClickListener mCameraOptions =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: //take picture
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); //tova e metod, koito e definiran po-dolu
                            if (mMediaUri == null) {
                                Toast.makeText(SendMessage.this, R.string.error_message_toast_external_storage, Toast.LENGTH_LONG).show();
                            } else {
                                takePicture();
                            }
                            break;
                        case 1: //take video
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); //tova e metod, koito e definiran po-dolu
                            if (mMediaUri == null) {
                                Toast.makeText(SendMessage.this, R.string.error_message_toast_external_storage, Toast.LENGTH_LONG).show();
                            } else {
                                takeVideo();
                            }
                            break;
                        case 2: //choose picture
                            Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            choosePhotoIntent.setType("image/*");
                            startActivityForResult(choosePhotoIntent,CHOOSE_PHOTO_REQUEST);
                            break;
                        case 3: //choose video
                            Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            chooseVideoIntent.setType("video/*");
                            Toast.makeText(SendMessage.this,R.string.warning_max_video_size,Toast.LENGTH_LONG).show();
                            startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);
                            break;
                    }
                }

                //tuk zapochvat vatreshni helper metodi za switch statementa

                private Uri getOutputMediaFileUri(int mediaType) {
                    //parvo triabva da se proveri dali ima external storage

                    if (isExternalStorageAvailable()) {

                        //sled tova vrashtame directoriata za pictures ili ia sazdavame
                        //1.Get external storage directory
                        String appName = SendMessage.this.getString(R.string.app_name);
                        String environmentDirectory; //
                        //ako snimame picture zapismave v papkata za kartiniki, ako ne v papkata za Movies

                        if(mediaType == MEDIA_TYPE_IMAGE) {
                            environmentDirectory = Environment.DIRECTORY_PICTURES;
                        } else {
                            environmentDirectory = Environment.DIRECTORY_MOVIES;
                        }
                        File mediaStorageDirectory = new File(
                                Environment.getExternalStoragePublicDirectory(environmentDirectory),
                                appName);

                        //2.Create subdirectory if it does not exist
                        if (! mediaStorageDirectory.exists()) {
                            if (!mediaStorageDirectory.mkdirs()) {
                                Log.e(TAG, "failed to create directory");
                                return null;
                            }
                        }

                        //3.Create file name
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        File mediaFile;
                        if (mediaType == MEDIA_TYPE_IMAGE) {
                            mediaFile = new File(mediaStorageDirectory.getPath() + File.separator +
                                    "IMG_" + timeStamp + ".jpg");
                        } else if (mediaType == MEDIA_TYPE_VIDEO) {
                            mediaFile = new File(mediaStorageDirectory.getPath() + File.separator +
                                    "MOV_" + timeStamp + ".mp4");
                        } else {
                            return null;
                        }
                        //4.Return the file's URI
                        Log.d(TAG, "File path: " + Uri.fromFile(mediaFile));
                        return Uri.fromFile(mediaFile);

                    } else //ako niama external storage
                        Log.d("Vic","no external strogage, mediaUri si null");
                    return null;

                }


                private boolean isExternalStorageAvailable() {
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                private void takePicture() {
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                }

                private void takeVideo() {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                }
            };


    @Override
    //metod koito se vika kogato niakoi Intent varne rezultat
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_PHOTO_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST) {
                //tova e sluchaia v koito izbirame photo ili video ot galeriata
                if(data == null) {
                    Toast.makeText(this,R.string.general_error_message,Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }
                Log.d("Vic","Media URI: " +mMediaUri);
                if(requestCode == CHOOSE_VIDEO_REQUEST) {
                    //proveriavame dali file size > 10MB
                    int fileSize = 0;
                    InputStream inputStream = null;
                    try {
                        //potvariame izbranoto video i proveriavame kolko e goliamo
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();

                    } catch (Exception e) {
                        Toast.makeText(SendMessage.this,R.string.error_video_cannot_be_added, Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();

                        } catch (IOException e) {
                            //blank
                        }
                    }
                    if(fileSize > FILE_SIZE_LIMIT) {
                        Toast.makeText(SendMessage.this,R.string.error_file_too_large,Toast.LENGTH_LONG).show();
                        return; //prekratiavame metoda tuk.
                    }
                }
            } else {
                //dobaviame snimkata ili videoto kam galeriata
                //tova e v sluchaite v koito sme snimali neshto
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); //broadcast intent
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent); //broadcast intent
            }
            //startirame prozoreca, kadeto se izbira na kogo da pratish saobshtenieto.
            Intent recipientsIntent = new Intent(this, ActivityRecipients.class);
            recipientsIntent.setData(mMediaUri); //vrazvame mMediaUri kam intent

            //Dobaviame tipa na file kam Intent
            String fileType;
            if(requestCode == TAKE_PHOTO_REQUEST || requestCode == CHOOSE_PHOTO_REQUEST) {

                fileType = ParseConstants.TYPE_IMAGE;
            } else {
                fileType = ParseConstants.TYPE_VIDEO;
            }
            //dobaviame tipa na file kam Intent
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE,fileType);

            startActivity(recipientsIntent);

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this,R.string.general_error_message,Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        mUploadMedia = (ImageView) findViewById(R.id.uploadPictureOrMovie);

        mUploadMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SendMessage.this);
                builder.setTitle(R.string.menu_camera_alertdialog_title);
                builder.setItems(R.array.camera_choices, mCameraOptions);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
        messageToSend = (EditText) findViewById(R.id.messageToSend);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_send) {


            //Pravia spisak s poluchatelite
            ArrayList<String> recepientIDs = new ArrayList<String>();
            recepientIDs.add(ParseUser.getCurrentUser().getObjectId());


            ParseUser recepient = ParseUser.getCurrentUser();//izprashtam go na men si.

            //Izprashtam push notification
            SendParsePushMessagesAndParseObjects pushM = new SendParsePushMessagesAndParseObjects();

            String senderMessageText = ParseUser.getCurrentUser().getUsername() + " " +
                    getString(R.string.send_a_message_message); //niakoi ti izprati sabshtenie
            String message = messageToSend.getText().toString();
            pushM.sendPush(recepient, senderMessageText, ParseConstants.TYPE_PUSH_MESSAGE,message);

            //Izprashtam Parse message
            ParseObject messageTosend = new ParseObject(ParseConstants.CLASS_MESSAGES);
            messageTosend.put(ParseConstants.KEY_SENDER_ID,ParseUser.getCurrentUser().getObjectId());
            messageTosend.put(ParseConstants.KEY_SENDER_NAME,ParseUser.getCurrentUser().getUsername());
            messageTosend.put(ParseConstants.KEY_RECEPIENT_IDS,recepientIDs);
            messageTosend.put(ParseConstants.KEY_FILE_TYPE, ParseConstants.TYPE_TEXTMESSAGE);


            SendParsePushMessagesAndParseObjects sendParse = new SendParsePushMessagesAndParseObjects();
            sendParse.send(messageTosend,this);
        }



        return super.onOptionsItemSelected(item);
    }


}
