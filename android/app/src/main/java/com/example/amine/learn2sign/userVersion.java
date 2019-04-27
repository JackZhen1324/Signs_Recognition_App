package com.example.amine.learn2sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;


import static com.example.amine.learn2sign.LoginActivity.INTENT_ID;
import static com.example.amine.learn2sign.LoginActivity.INTENT_SERVER_ADDRESS;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;


public class userVersion extends AppCompatActivity {
    String path;
    String returnedURI;
    String old_text = "";
    SharedPreferences sharedPreferences;
    long accepted_Practice = 0;
    long decline_Practice = 0;
    long time_started_return = 0;

    private Button accept;
    private Button decline;

    String signname;
    private VideoView sameple_v;
    private VideoView user_v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_version);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent pre_intent = getIntent();

        sharedPreferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);

        sameple_v = (VideoView) findViewById(R.id.sample_video1);
        user_v = (VideoView) findViewById(R.id.user_video1);
        signname = pre_intent.getStringExtra("word");
        returnedURI = pre_intent.getStringExtra(INTENT_URI);
        time_started_return = pre_intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
        Log.e("signname",signname);
        Log.e("returnedURI",returnedURI);
        Log.e("time_started_return",String.valueOf(time_started_return));

        if(signname.equals("Alaska")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.alaska;
        } else if(signname.equals("Arizona")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.arizona;
        } else if (signname.equals("California")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.california;
        }else if (signname.equals("Colorado")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.colorado;
        }else if (signname.equals("Florida")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.florida;
        }else if (signname.equals("Georgia")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.georgia;
        }else if (signname.equals("Hawaii")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.hawaii;
        }else if (signname.equals("Illinois")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.illinois;
        }else if (signname.equals("Indiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.indiana;
        }else if (signname.equals("Kansas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.kansas;
        }else if (signname.equals("Louisiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.louisiana;
        }else if (signname.equals("Massachusetts")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.massachusetts;
        }else if (signname.equals("Michigan")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.michigan;
        }else if (signname.equals("Minnesota")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.minnesota;
        }else if (signname.equals("Nevada")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.nevada;
        }else if (signname.equals("NewJersey")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_jersey;
        }else if (signname.equals("NewMexico")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_mexico;
        }else if (signname.equals("NewYork")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_york;
        }else if (signname.equals("Ohio")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.ohio;
        }else if (signname.equals("Pennsylvania")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.pennsylvania;
        }else if (signname.equals("SouthCarolina")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.south_carolina;
        }else if (signname.equals("Texas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.texas;
        }else if (signname.equals("Utah")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.utah;
        }else if (signname.equals("Washington")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.washington;
        }else if (signname.equals("Wisconsin")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.wisconsin;
        }
        Log.e("path+++++++++",path);
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            sameple_v.setVideoURI(uri);
            sameple_v.start();
        }
        if(!returnedURI.isEmpty()) {
            Uri uri2 = Uri.parse(returnedURI);
            user_v.setVideoURI(uri2);
            user_v.start();
        }

        sameple_v.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        sameple_v.start();
                    }
                }

        );
        user_v.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        user_v.start();
                    }
                }

        );

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vid = getIntent();
                String a = vid.getStringExtra(INTENT_URI);
                vid.getData();
                Log.e("HMM", a);



                String toAdd  = signname+"_"+"Accept_"+"";
                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE",new HashSet<String>());
                set.add(toAdd);
                sharedPreferences.edit().putStringSet("PRACTICE",set).apply();

                String id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENT_ID,"00000000");

                String server_ip = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENT_SERVER_ADDRESS,"10.211.17.171");
                Log.d("msg",server_ip);

                File f = new File(a);
                AsyncHttpClient client_logs = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                try {
                    params.put("uploaded_file",f);
                    params.put("id",id);
                } catch (FileNotFoundException e) { }

                AsyncHttpClient client = new AsyncHttpClient();

                client.post("http://" + server_ip + "/upload_video_performance.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("msg success",statusCode+"");
                        if(statusCode==200) {

                            Toast.makeText(userVersion.this, "Success", Toast.LENGTH_SHORT).show();

                            Log.e("gotomain","go to main");
                            Intent t = new Intent(userVersion.this,MainActivity.class);
                            startActivityForResult(t,9999);
                        }
                        else {

                            Toast.makeText(userVersion.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Log.e("failed",statusCode+"");

                        Toast.makeText(userVersion.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //end onclick accept

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toAdd  = signname+"_"+"Decline_"+"";
                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE",new HashSet<String>());
                set.add(toAdd);
                sharedPreferences.edit().putStringSet("PRACTICE",set).apply();

                Log.e("gotopractice","go to practice");
                Intent t = new Intent(userVersion.this,pratice.class);
                startActivityForResult(t,9999);

            }
        });
    }


}
