package com.example.amine.learn2sign;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.*;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.security.Policy;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.OnClick;

public class pratice extends AppCompatActivity {
    private TextView current_sign;
    private TextView editText;
    private VideoView sameple_v;
    private TextView title_of_sample;
    private Button Start_Praticing;
    String currentSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pratice);
        editText = (TextView) findViewById(R.id.editText2);
        current_sign =(TextView) findViewById(R.id.Curent_Sign);
        sameple_v = (VideoView) findViewById(R.id.sample_video);
        title_of_sample = (TextView) findViewById(R.id.title_of_sample);
        Start_Praticing =(Button) findViewById(R.id.Start_Praticing);

        sameple_v.setVisibility(View.VISIBLE);
        sameple_v.setVisibility(View.GONE);
        Random rand = new Random();
        int rand_int1 = rand.nextInt(25);
        String path = "";
        String sign_List[] = new String[]{"Alaska","California","Colorado","Florida", "Georgia","Hawaii","Illinois","Indiana","Kansas","Kansas","Louisiana","Massachusetts","Michigan","Minnesota","Nevada","NewJersey","NewMexico","NewYork","Ohio","Pennsylvania","SouthCarolina","Texas","Utah","Washington","Wisconsin"};
        currentSign = sign_List[rand_int1];
        current_sign.setText(currentSign);
        title_of_sample.setText("Random Sign: "+currentSign);
        if(currentSign==("Alaska")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.alaska;
        } else if(currentSign==("Arizona")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.arizona;
        } else if (currentSign==("California")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.california;
        }else if (currentSign==("Colorado")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.colorado;
        }else if (currentSign==("Florida")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.florida;
        }else if (currentSign==("Georgia")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.georgia;
        }else if (currentSign==("Hawaii")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.hawaii;
        }else if (currentSign==("Illinois")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.illinois;
        }else if (currentSign==("Indiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.indiana;
        }else if (currentSign==("Kansas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.kansas;
        }else if (currentSign==("Louisiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.louisiana;
        }else if (currentSign==("Massachusetts")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.massachusetts;
        }else if (currentSign==("Michigan")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.michigan;
        }else if (currentSign==("Minnesota")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.minnesota;
        }else if (currentSign==("Nevada")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.nevada;
        }else if (currentSign==("NewJersey")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_jersey;
        }else if (currentSign==("NewMexico")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_mexico;
        }else if (currentSign==("NewYork")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_york;
        }else if (currentSign==("Ohio")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.ohio;
        }else if (currentSign==("Pennsylvania")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.pennsylvania;
        }else if (currentSign==("SouthCarolina")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.south_carolina;
        }else if (currentSign==("Texas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.texas;
        }else if (currentSign==("Utah")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.utah;
        }else if (currentSign==("Washington")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.washington;
        }else if (currentSign==("Wisconsin")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.wisconsin;
        }
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            sameple_v.setVideoURI(uri);
            sameple_v.start();

        }
        String samplePath = "android.resource://" + getPackageName() + "/" + R.raw.arizona;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });



        editText.setText(samplePath);
        sameple_v.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        sameple_v.start();
                    }
                }

        );



        Start_Praticing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("lol","tets!!!!");
                Intent t = new Intent(pratice.this,UploadNew.class);
                t.putExtra("currentSign",currentSign);
                startActivityForResult(t,9999);

            }
        });


    }









}
