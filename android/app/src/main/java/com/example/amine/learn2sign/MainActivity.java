package com.example.amine.learn2sign;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;
import com.facebook.stetho.Stetho;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.example.amine.learn2sign.LoginActivity.INTENT_EMAIL;
import static com.example.amine.learn2sign.LoginActivity.INTENT_ID;
import static com.example.amine.learn2sign.LoginActivity.INTENT_SERVER_ADDRESS;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import cz.msebera.android.httpclient.Header;
public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    @BindView(R.id.rg_practice_learn)
    RadioGroup rg_practice_learn;

    @BindView(R.id.rb_learn)
    RadioButton rb_learn;

    @BindView(R.id.rb_practice)
    RadioButton rb_practice;

    @BindView(R.id.sp_words)
    Spinner sp_words;

    @BindView(R.id.sp_ip_address)
    Spinner sp_ip_address;

    @BindView(R.id.vv_video_learn)
    VideoView vv_video_learn;

    @BindView(R.id.vv_record)
    VideoView vv_record;

    @BindView(R.id.bt_record)
    Button bt_record;

    @BindView(R.id.bt_send)
    Button bt_send;

    @BindView(R.id.bt_cancel)
    Button bt_cancel;

    @BindView(R.id.ll_after_record)
    LinearLayout ll_after_record;

    String path;
    String returnedURI;
    String old_text = "";
    SharedPreferences sharedPreferences;
    long time_started = 0;
    long time_started_return = 0;
    Activity mainActivity;
    int isEnoughSign = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.print("hello");
        //bind xml to activity
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        rb_learn.setChecked(true);
        bt_cancel.setVisibility(View.GONE);
        bt_send.setVisibility(View.GONE);
        rg_practice_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rb_learn.getId()) {
                    Toast.makeText(getApplicationContext(),"Learn",Toast.LENGTH_SHORT).show();
                    vv_video_learn.setVisibility(View.VISIBLE);
                    vv_video_learn.start();
                    time_started = System.currentTimeMillis();
                } else if ( checkedId==rb_practice.getId()) {
                    Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT).show();
                    vv_video_learn.setVisibility(View.GONE);
                }
            }
        });

        sp_words.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = sp_words.getSelectedItem().toString();
                if(!old_text.equals(text)) {
                    path = "";
                    time_started = System.currentTimeMillis();
                    play_video(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_ip_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sharedPreferences.edit().putString(INTENT_SERVER_ADDRESS, sp_ip_address.getSelectedItem().toString()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.start();

                }

             }
        };
        vv_record.setOnCompletionListener(onCompletionListener);
        vv_video_learn.setOnCompletionListener(onCompletionListener);
        vv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vv_record.start();
            }
        });
        vv_video_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vv_video_learn.isPlaying()) {
                    vv_video_learn.start();
                }
            }
        });
        time_started = System.currentTimeMillis();
        sharedPreferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if(intent.hasExtra(INTENT_EMAIL) && intent.hasExtra(INTENT_ID)) {
            Toast.makeText(this,"User : " + intent.getStringExtra(INTENT_EMAIL),Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Already Logged In",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {

        vv_video_learn.start();
        time_started = System.currentTimeMillis();
        super.onResume();

    }

    public void play_video(String text) {
        old_text = text;
        if(text.equals("Alaska")) {

             path = "android.resource://" + getPackageName() + "/" + R.raw.alaska;
        } else if(text.equals("Arizona")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.arizona;
        } else if (text.equals("California")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.california;
        }else if (text.equals("Colorado")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.colorado;
        }else if (text.equals("Florida")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.florida;
        }else if (text.equals("Georgia")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.georgia;
        }else if (text.equals("Hawaii")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.hawaii;
        }else if (text.equals("Illinois")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.illinois;
        }else if (text.equals("Indiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.indiana;
        }else if (text.equals("Kansas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.kansas;
        }else if (text.equals("Louisiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.louisiana;
        }else if (text.equals("Massachusetts")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.massachusetts;
        }else if (text.equals("Michigan")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.michigan;
        }else if (text.equals("Minnesota")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.minnesota;
        }else if (text.equals("Nevada")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.nevada;
        }else if (text.equals("NewJersey")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_jersey;
        }else if (text.equals("NewMexico")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_mexico;
        }else if (text.equals("NewYork")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_york;
        }else if (text.equals("Ohio")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.ohio;
        }else if (text.equals("Pennsylvania")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.pennsylvania;
        }else if (text.equals("SouthCarolina")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.south_carolina;
        }else if (text.equals("Texas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.texas;
        }else if (text.equals("Utah")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.utah;
        }else if (text.equals("Washington")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.washington;
        }else if (text.equals("Wisconsin")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.wisconsin;
        }
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vv_video_learn.setVideoURI(uri);
            vv_video_learn.start();

        }

    }

    @OnClick(R.id.p_button)
    public void pratice_sign()
    {String id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENT_ID,"00000000");
        String server_ip = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENT_SERVER_ADDRESS,"10.211.17.171");
        AsyncHttpClient client_logs = new AsyncHttpClient();
        RequestParams params = new RequestParams();


            params.put("id",id);

        client_logs.post("http://"+server_ip+"/check_video_count.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200) {
                    String res = new String(responseBody);
                    res=res.replaceAll("\\s+","");
                    String res2 = res;
                    int in2 = new Integer(res.toString());
                    Log.e("test",res);
                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
                    if( in2 < 75)
                    {   isEnoughSign = 0;
                        Log.e("error","missing!!!!!!!!!");
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setMessage("Sorry, you don't have enough signs to enter this module! You currently have "+ res +" signs on server.");
                        alertDialogBuilder.setPositiveButton("Enter Anyway!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("test","test11");
                                isEnoughSign = 1;
                                Log.e("currentSign",String.valueOf(isEnoughSign));
                                if(isEnoughSign == 1)
                                {
                                    Intent t = new Intent(MainActivity.this,pratice.class);
                                    Log.e("test","test");
                                    startActivityForResult(t, 9999);
                                }
                            }
                        });
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                    else
                    {

                        Intent t = new Intent(MainActivity.this, pratice.class);
                        Log.e("test","test");
                        startActivityForResult(t, 9999);


                    }

                }
                else
                   // Toast.makeText(MainActivity.this, "Internet Error", Toast.LENGTH_SHORT).show();
                isEnoughSign = 0;
                Log.e("error","missing!!!!!!!!!");
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Sorry, you don't have enough signs to enter this module! You currently have "+ 0 +" signs on server.");
                alertDialogBuilder.setPositiveButton("Enter Anyway!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("test","test11");
                        isEnoughSign = 1;
                        Log.e("currentSign",String.valueOf(isEnoughSign));
                        if(isEnoughSign == 1)
                        {
                            Intent t = new Intent(MainActivity.this,pratice.class);
                            Log.e("test","test");
                            startActivityForResult(t, 9999);
                        }
                    }
                });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                isEnoughSign = 0;
                Log.e("error","missing!!!!!!!!!");
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Sorry, you don't have enough signs to enter this module! You currently have "+ 0 +" signs on server.");
                alertDialogBuilder.setPositiveButton("Enter Anyway!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("test","test11");
                        isEnoughSign = 1;
                        Log.e("currentSign",String.valueOf(isEnoughSign));
                        if(isEnoughSign == 1)
                        {
                            Intent t = new Intent(MainActivity.this,pratice.class);
                            Log.e("test","test");
                            startActivityForResult(t, 9999);
                        }
                    }
                });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        /*
        System.out.print("help!!!");
        Log.e("lol","tets!!!!");
        String RecordPath = Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/";
        String sign_List[] = new String[]{"Alaska","California","Colorado","Florida", "Georgia","Hawaii","Illinois","Indiana","Kansas","Kansas","Louisiana","Massachusetts","Michigan","Minnesota","Nevada","NewJersey","NewMexico","NewYork","Ohio","Pennsylvania","SouthCarolina","Texas","Utah","Washington","Wisconsin"};
        File a = new File(RecordPath);
        String [] matchingFiles = a.list();

        int count =0;
        for(int j =0 ;j< sign_List.length;j++)
        {
            count =0;
            if(matchingFiles!=null){
                for (int i = 0; i < matchingFiles.length; i++)
                {
                    Log.e("oneResourceRecord",matchingFiles[i]);
                    if(matchingFiles[i].contains(sign_List[j]))
                    {
                        count++;
                    }
                }
            }

            if(count<3)
            {   isEnoughSign = 0;
                Log.e("error","missing!!!!!!!!!");
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Sorry, you don't have enough signs to enter this module!");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("test","test11");
                        isEnoughSign = 1;
                        Log.e("currentSign",String.valueOf(isEnoughSign));
                        if(isEnoughSign == 1)
                        {
                            Intent t = new Intent(MainActivity.this,pratice.class);
                            Log.e("test","test");
                            startActivityForResult(t, 9999);
                        }
                    }
                });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
            }
        }
        Log.e("currentSign",String.valueOf(isEnoughSign));
        if(isEnoughSign == 1)
        {
            Intent t = new Intent(this, pratice.class);
            Log.e("test","test");
            startActivityForResult(t, 9999);
        }
        */

    }
    @OnClick(R.id.bt_record)
    public void record_video() {

         if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

             // Permission is not granted
             // Should we show an explanation?

             if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                     Manifest.permission.CAMERA)) {
                 // Show an explanation to the user *asynchronously* -- don't block
                 // this thread waiting for the user's response! After the user
                 // sees the explanation, try again to request the permission.
             } else {
                 // No explanation needed; request the permission
                 ActivityCompat.requestPermissions(this,
                         new String[]{Manifest.permission.CAMERA},
                         101);

                 // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                 // app-defined int constant. The callback method gets the
                 // result of the request.
             }
         }


         if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            // Permission has already been granted
             File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

             if (!f.exists()) {
                 f.mkdirs();
             }

             time_started = System.currentTimeMillis() - time_started;

             Intent t = new Intent(this,VideoActivity.class);
             t.putExtra(INTENT_WORD,sp_words.getSelectedItem().toString());
             t.putExtra(INTENT_TIME_WATCHED, time_started);
             startActivityForResult(t,9999);
            /*File m = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign");
            if(!m.exists()) {
                if(m.mkdir()) {
                    Toast.makeText(this,"Directory Created",Toast.LENGTH_SHORT).show();
                }
            }

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(EXTRA_DURATION_LIMIT, 10);

            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }*/
        }
    }

    @OnClick(R.id.bt_send)
    public void sendToServer() {
        Toast.makeText(this,"Send to Server",Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this,UploadActivity.class);
        startActivityForResult(t,2000);
    }

    @OnClick(R.id.bt_part2)
    public void predict() {
        Intent t = new Intent(MainActivity.this,predictore.class);
        Log.e("part2","part2");
        startActivityForResult(t, 9999);

    }


    @OnClick(R.id.bt_cancel)
    public void cancel() {
        vv_record.setVisibility(View.GONE);
        if(rb_learn.isSelected()) {
            vv_video_learn.setVisibility(View.VISIBLE);
        }
        bt_record.setVisibility(View.VISIBLE);
        bt_send.setVisibility(View.GONE);
        bt_cancel.setVisibility(View.GONE);

        sp_words.setEnabled(true);

        rb_learn.setEnabled(true);
        //rb_practice.setEnabled(true);
        time_started = System.currentTimeMillis();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    Log.e("OnActivityresult",requestCode+" "+resultCode);
        if(requestCode==2000 ) {
            //from video activity
            vv_record.setVisibility(View.GONE);
            rb_learn.setChecked(true);
            bt_cancel.setVisibility(View.GONE);
            bt_send.setVisibility(View.GONE);
            bt_record.setVisibility(View.VISIBLE);
            sp_words.setEnabled(true);
            rb_learn.setEnabled(true);
            //rb_practice.setEnabled(true);
            sp_ip_address.setEnabled(true);

        }
        if(requestCode==9999 && resultCode == 8888) {
            if(intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                returnedURI = intent.getStringExtra(INTENT_URI);
                time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);

                vv_record.setVisibility(View.VISIBLE);
                bt_record.setVisibility(View.GONE);
                bt_send.setVisibility(View.VISIBLE);
                bt_cancel.setVisibility(View.VISIBLE);
                sp_words.setEnabled(false);
                rb_learn.setEnabled(false);
                //rb_practice.setEnabled(false);
                vv_record.setVideoURI(Uri.parse(returnedURI));
                int try_number = sharedPreferences.getInt("record_"+sp_words.getSelectedItem().toString(),0);
                try_number++;
                String toAdd  = sp_words.getSelectedItem().toString()+"_"+try_number+"_"+time_started_return + "";
                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("RECORDED",new HashSet<String>());
                set.add(toAdd);
                sharedPreferences.edit().putStringSet("RECORDED",set).apply();
                sharedPreferences.edit().putInt("record_"+sp_words.getSelectedItem().toString(), try_number).apply();

                vv_video_learn.start();
            }

        }

        
        if(requestCode==9999 && resultCode==7777)
        {
            if(intent!=null) {
                //create folder
                if(intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                    returnedURI = intent.getStringExtra(INTENT_URI);
                    time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
                    File f = new File(returnedURI);
                    f.delete();
                  //  int try_number = sharedPreferences.getInt("record_"+sp_words.getSelectedItem().toString(),0);
                   // try_number++;
                    //String toAdd  = sp_words.getSelectedItem().toString()+"_"+try_number+"_"+time_started_return + "_cancelled";
                    //HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("RECORDED",new HashSet<String>());
                   // set.add(toAdd);
                  //  sharedPreferences.edit().putStringSet("RECORDED",set).apply();
                 //   sharedPreferences.edit().putInt("record_"+sp_words.getSelectedItem().toString(), try_number).apply();




                    time_started = System.currentTimeMillis();
                    vv_video_learn.start();
                }
            }

        }

        /*if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            final Uri videoUri = intent.getData();


            vv_record.setVisibility(View.VISIBLE);
            vv_record.setVideoURI(videoUri);
            vv_record.start();
            play_video(sp_words.getSelectedItem().toString());
            bt_record.setVisibility(View.GONE);
            int i=0;
            File n = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                    + sharedPreferences.getString(INTENT_ID,"0000")+"_"+sp_words.getSelectedItem().toString()+"_0" + ".mp4");
            while(n.exists()) {
                i++;
                n = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                        + sharedPreferences.getString(INTENT_ID,"0000")+"_"+sp_words.getSelectedItem().toString()+"_"+i + ".mp4");
            }
            SaveFile saveFile = new SaveFile();
            saveFile.execute(n.getPath(),videoUri.toString());

            bt_send.setVisibility(View.VISIBLE);
            bt_cancel.setVisibility(View.VISIBLE);

            sp_words.setEnabled(false);
            rb_learn.setEnabled(false);
            rb_practice.setEnabled(false);
        }*/
    }

    //Menu Item for logging out
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mainActivity = this;
                    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("ALERT");
                    alertDialog.setMessage("Logging out will delete all the data!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sharedPreferences.edit().clear().apply();
                                    File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");
                                    if (f.isDirectory())
                                    {
                                        String[] children = f.list();
                                        for (int i = 0; i < children.length; i++)
                                        {
                                            new File(f, children[i]).delete();
                                        }
                                    }
                                    startActivity(new Intent(mainActivity,LoginActivity.class));
                                    mainActivity.finish();

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();



                    return true;
            case R.id.menu_upload_server:
                sharedPreferences.edit().putInt(getString(R.string.gotoupload), sharedPreferences.getInt(getString(R.string.gotoupload),0)+1).apply();
                Intent t = new Intent(this,UploadActivity.class);
                startActivityForResult(t,2000);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SaveFile extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            try {
                fileOutputStream = new FileOutputStream(strings[0]);
                fileInputStream = (FileInputStream) getContentResolver().openInputStream(Uri.parse(strings[1]));
                Log.d("msg", fileInputStream.available() + " ");
                byte[] buffer = new byte[1024];
                while (fileInputStream.available() > 0) {

                    fileInputStream.read(buffer);
                    fileOutputStream.write(buffer);
                    publishProgress(fileInputStream.available()+"");
                }

                fileInputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Video Saved Successfully",Toast.LENGTH_SHORT).show();
        }
    }
}
