package com.example.victor.swipeviews;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static com.example.victor.swipeviews.R.id.imageView_to_display_picture;


public class ViewImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ImageView imageView = (ImageView) findViewById(imageView_to_display_picture);
        Uri imageUri = getIntent().getData(); //vzima Uri deto go podadohme ot drugata strana
        //Picasso e vanshta bibilioteka, koito ni pozvoliava da otvariame snimki ot internet
        Picasso.with(this).load(imageUri).into(imageView);
    }





}



