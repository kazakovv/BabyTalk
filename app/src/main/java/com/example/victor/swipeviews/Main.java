package com.example.victor.swipeviews;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;


public class Main extends FragmentActivity implements ActionBar.TabListener {
    ViewPager pager;
    ActionBar actionbar;
    static Context context;
    static MenuItem fertilityCalandarIcon; //izplolzva se za reference v MaleOrFemaleDialog.
    protected ParseUser currentUser;

    public static final String TAG = Main.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        currentUser = ParseUser.getCurrentUser();
        //ako niama lognat potrebitel preprashta kam log-in ekrana
        if (currentUser == null) {
            //prashta ni kam login screen
            navigateToLogin();


        } else {
            // ako ima lognat potrebitel prodalzhava natatak
            Log.i(TAG,"imame lognat potrebitel");

        }

            pager = (ViewPager) findViewById(R.id.pager);
            PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pAdapter);
            actionbar = getActionBar();
            actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_days_title).setTabListener(this));
            actionbar.addTab(actionbar.newTab().setText(R.string.tab_chat_title).setTabListener(this));

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

    private void navigateToLogin() {
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        //zadava dali iconkata na kalendara e enabled ili ne
        fertilityCalandarIcon =  menu.findItem(R.id.menu_fertility_calendar);

        SharedPreferences savedSettings = getSharedPreferences("MYPREFS",0);
        fertilityCalandarIcon.setVisible(savedSettings.getBoolean("FertilityCalendar", true));

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
