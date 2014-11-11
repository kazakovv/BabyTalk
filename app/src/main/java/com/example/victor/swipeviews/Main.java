package com.example.victor.swipeviews;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main extends FragmentActivity implements ActionBar.TabListener {
    ViewPager pager;
    ActionBar actionbar;
    static Context context;
    static MenuItem fertilityCalandarIcon; //izplolzva se za reference v MaleOrFemaleDialog.
    protected ParseUser currentUser;

    protected String MaleOrFemale;
    TextView mainMessage;

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
                                Toast.makeText(Main.this, R.string.error_message_toast_external_storage, Toast.LENGTH_LONG).show();
                            } else {
                                takePicture();
                            }
                            break;
                        case 1: //take video
                            mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); //tova e metod, koito e definiran po-dolu
                            if (mMediaUri == null) {
                                Toast.makeText(Main.this, R.string.error_message_toast_external_storage, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Main.this,R.string.warning_max_video_size,Toast.LENGTH_LONG).show();
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
                        String appName = Main.this.getString(R.string.app_name);
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
                        Toast.makeText(Main.this,R.string.error_video_cannot_be_added, Toast.LENGTH_LONG).show();
                        return;
                    } finally {
                        try {
                            inputStream.close();

                        } catch (IOException e) {
                           //blank
                        }
                    }
                    if(fileSize > FILE_SIZE_LIMIT) {
                        Toast.makeText(Main.this,R.string.error_file_too_large,Toast.LENGTH_LONG).show();
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        //vrazvame osnovnotosaobshtenie
        currentUser = ParseUser.getCurrentUser();
        //ako niama lognat potrebitel preprashta kam log-in ekrana

        if (currentUser == null) {
            //prashta ni kam login screen
            navigateToLogin();
        } else {
            // ako ima lognat potrebitel prodalzhava natatak
            Log.i(TAG,"imame lognat potrebitel");

            //proveriavame dali e maz ili zhena
            MaleOrFemale = currentUser.getString(ParseConstants.KEY_MALEORFEMALE);

            //instalaciata vrazva device sas application. Tova se pravi za da mozhe da se poluchavat push notifications
            //posle moga da napisha query, koiato da tarsi po parseuser ustroistvoto
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put(ParseConstants.KEY_USER, ParseUser.getCurrentUser());
            installation.saveInBackground();
        }

            pager = (ViewPager) findViewById(R.id.pager);
            PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pAdapter);
            actionbar = getActionBar();
            actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_days_title).setTabListener(this));
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_chat_title).setTabListener(this));
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_friends_title).setTabListener(this));
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_partners_title).setTabListener(this));
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    actionbar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


    }



    protected void navigateToLogin() {
        //preprashta kam login screen
        Intent intent = new Intent(this, LoginActivity.class);

        //Celta na sledvashtite 2 reda e da ne moze da otidesh ot log-in ekrana
        //kam osnovnia ekran, ako natisnesh back butona

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //sazdavo zadacha
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //iztriva vsichki predishni zadachi.
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {
            case R.id.menu_send_kiss:
                //SendPushMessages sadarza metoda za izprashtane na push
                SendPushMessages pushM = new SendPushMessages();

                ParseUser recepient = ParseUser.getCurrentUser();//izprashtam go na men si.
                String message = ParseUser.getCurrentUser().getUsername() + " " +
                        getString(R.string.send_a_kiss_message); //niakoi ti izprati celuvka
                pushM.sendPush(recepient,message,ParseConstants.TYPE_PUSH_KISS,"");
                return true;
            case R.id.menu_send_message:
                 Intent intent = new Intent(this,SendMessage.class);
                startActivity(intent);
                return true;

            case R.id.menu_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.menu_camera_alertdialog_title);
                builder.setItems(R.array.camera_choices, mCameraOptions);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.menu_fertility_calendar:
                DialogFragment newDialog = new MenstrualCalendarDialog();
                newDialog.show(getFragmentManager(),"Welcome");
                Log.d("Vic","Calendar menu");
                return true;

            case R.id.menu_sex:
                DialogFragment sexDialog = new MaleOrFemaleDialog();
                sexDialog.show(getFragmentManager(),"Welcome");
                Log.d("Vic","Sex menu");
                return true;
            case R.id.menu_logout:
                currentUser.logOut();

                //prashta kam login screen
                navigateToLogin();
                return true;

            case R.id.menu_edit_friends:
                Intent intentSendMessage = new Intent(this,EditFriendsActivity.class);
                startActivity(intentSendMessage);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        //vrazvam osnovnoto saobshtenie( your fertile days are/your partner fertile days are)
        mainMessage = (TextView) findViewById(R.id.mainMessage);
        //zadava dali iconkata na kalendara e enabled ili ne
        fertilityCalandarIcon =  menu.findItem(R.id.menu_fertility_calendar);
        //proveriavame dali current user ne e null.
        if(currentUser != null) {
            if (MaleOrFemale.equals(ParseConstants.SEX_FEMALE)) {
                fertilityCalandarIcon.setVisible(true);
                mainMessage.setText(R.string.main_message_female);

            } else {
                //ako ne e zhena triabva da e maz
                fertilityCalandarIcon.setVisible(false);
                mainMessage.setText(R.string.main_message_male);

            }
        }
        //SharedPreferences savedSettings = getSharedPreferences("MYPREFS",0);
        //fertilityCalandarIcon.setVisible(savedSettings.getBoolean("FertilityCalendar", true));

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        //Sahraniavam shared preferences kato izlizam ot fragmenta

        SharedPreferences savedSettings = getSharedPreferences("MYPREFS",0);
        SharedPreferences.Editor editor = savedSettings.edit();
        editor.putBoolean("FertilityCalendar",fertilityCalandarIcon.isVisible());
        editor.commit();

    }
}
