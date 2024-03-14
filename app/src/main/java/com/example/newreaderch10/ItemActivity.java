package com.example.newreaderch10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

@SuppressLint("eglCodeCommon")
public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //get references to widgets
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView pubDateTextView = (TextView) findViewById(R.id.pubDateTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView linkTextView = (TextView) findViewById(R.id.linkTextView);

        //get the intent
        Intent intent = getIntent();

        //get data from intent
        String pubDate = intent.getStringExtra("pubDate");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        //display the widgets
        pubDateTextView.setText(pubDate);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        //set listener
        linkTextView.setOnClickListener(this);
    }//end onCreate

    @Override
    public void onClick(View v) {
        //get intent
        Intent intent = getIntent();

        //get the Uri for the link
        String link = intent.getStringExtra("link");
        Uri viewUri = Uri.parse(link);

        //create the intent & start it
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);
    }//end onClick
}//end ItemActivity