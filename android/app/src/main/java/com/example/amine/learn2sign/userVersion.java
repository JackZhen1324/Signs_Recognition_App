package com.example.amine.learn2sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;


public class userVersion extends AppCompatActivity {
    String path;
    String returnedURI;
    String old_text = "";
    SharedPreferences sharedPreferences;
    long time_started = 0;
    long time_started_return = 0;
    String signname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_version);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent pre_intent = getIntent();
        signname = pre_intent.getStringExtra("filename");
        returnedURI = pre_intent.getStringExtra(INTENT_URI);
        time_started_return = pre_intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
        Log.e("signname",signname);
        Log.e("returnedURI",returnedURI);
        Log.e("time_started_return",String.valueOf(time_started_return));



    }

}
