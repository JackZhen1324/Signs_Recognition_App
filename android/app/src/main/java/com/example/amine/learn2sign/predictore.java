package com.example.amine.learn2sign;

import android.content.res.AssetManager;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.time.*;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.facebook.stetho.Stetho;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import android.content.res.AssetManager;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class predictore extends AppCompatActivity {

    private static final int REQUEST_CODE = 43;
    //Button Find_file;
    //TextView path;
    String [] filename = new String[450];
    String [] fileOutput;
    int globalCount = 0;
    long startTime = 0;
    long duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictore);

        long startTime = System.nanoTime();

        listAsset();
        readCsvFile();

        //long endTime = System.nanoTime();
        //long duration = (endTime - startTime);
        //duration = duration/1000000;
        //Log.e("Timer", "took this long: " + duration + "milliseconds");
        //fileOutput[globalCount] = "Time to execute task:" + duration;

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fileOutput);
        ListView File_View = (ListView)findViewById(R.id.File_View);
        File_View.setAdapter(adapter);

    }

    public void listAsset() {

        startTime = System.nanoTime();

        final AssetManager assetManager = getAssets();
        try {
            int counter = 0;
            // for assets folder add empty string
            String[] filelist = assetManager.list("");
            // for assets/subFolderInAssets add only subfolder name
            //String[] filelistInSubfolder = assetManager.list("subFolderInAssets");
            if (filelist == null) {
                // dir does not exist or is not a directory
            } else {
                for (int i=0; i<filelist.length; i++) {
                    // Get filename of file or directory
                    if(filelist[i].contains("csv")) {
                        filename [globalCount] = filelist[i];
                        Log.i("Raw Asset: ", " " + Arrays.toString(filename));
                        Log.i("Raw Asset count: ","" + globalCount);
                        globalCount++;
                        /*
                        try {
                            InputStreamReader is = new InputStreamReader(getAssets().open(filename[i]));
                            BufferedReader reader = new BufferedReader(is);
                            reader.readLine();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] Data = line.split(",");
                                Log.e("Hi test in it",Data[19]);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                           */
                    }
                }
                fileOutput = new String[globalCount+1];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCsvFile () {
        String line = "";
        String cvsSplitBy = ",";

        double[] leftSh_x = new double[200];
        double[] leftSh_y = new double[200];
        double[] rightSh_x = new double[200];
        double[] rightSh_y = new double[200];
        double[] leftElb_x = new double[200];
        double[] leftElb_y = new double[200];
        double[] rightElb_x = new double[200];
        double[] rightElb_y = new double[200];
        double[] leftWrist_x = new double[200];
        double[] leftWrist_y = new double[200];
        double[] rightWrist_x = new double[200];
        double[] rightWrist_y = new double[200];

        InputStream is = getResources().openRawResource(R.raw._1204828140_about_0);

        //InputStream is = null;

        /*
        int file_n = 1;
        while (file_n < 3)
        {
            if (file_n == 1)
            {
                is = getResources().openRawResource(R.raw._1204828140_about_0);
                readCsvFile();
                file_n++;
            }

        }
        */

        int counter = 0;
        int iCount = 1;
        while (iCount <= globalCount) {

            BufferedReader read = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            InputStreamReader is2 = null;
            try {
                is2 = new InputStreamReader(getAssets().open(filename[iCount-1]));
                Log.e("File access", iCount + " " + filename[iCount-1]);
                iCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(is2);

            try {
                //while ((line = read.readLine()) != null) {
                while ((line = reader.readLine()) != null) {
                    // use comma as separator
                    String[] Data = line.split(",");

                    if (counter != 0) {

                        //Log.e("line_data", Data[19]);

                        Data[18] = Data[18].replaceAll("^\"|\"$", "");
                        leftSh_x[counter - 1] = Double.valueOf(Data[18]);

                        Data[19] = Data[19].replaceAll("^\"|\"$", "");
                        leftSh_y[counter - 1] = Double.valueOf(Data[19]);

                        Data[21] = Data[21].replaceAll("^\"|\"$", "");
                        rightSh_x[counter - 1] = Double.valueOf(Data[21]);

                        Data[22] = Data[22].replaceAll("^\"|\"$", "");
                        rightSh_y[counter - 1] = Double.valueOf(Data[22]);

                        Data[24] = Data[24].replaceAll("^\"|\"$", "");
                        leftElb_x[counter - 1] = Double.valueOf(Data[24]);

                        Data[25] = Data[25].replaceAll("^\"|\"$", "");
                        leftElb_y[counter - 1] = Double.valueOf(Data[25]);

                        Data[27] = Data[27].replaceAll("^\"|\"$", "");
                        rightElb_x[counter - 1] = Double.valueOf(Data[27]);

                        Data[28] = Data[28].replaceAll("^\"|\"$", "");
                        rightElb_y[counter - 1] = Double.valueOf(Data[28]);

                        Data[30] = Data[30].replaceAll("^\"|\"$", "");
                        leftWrist_x[counter - 1] = Double.valueOf(Data[30]);

                        Data[31] = Data[31].replaceAll("^\"|\"$", "");
                        leftWrist_y[counter - 1] = Double.valueOf(Data[31]);

                        Data[33] = Data[33].replaceAll("^\"|\"$", "");
                        rightWrist_x[counter - 1] = Double.valueOf(Data[33]);

                        Data[34] = Data[34].replaceAll("^\"|\"$", "");
                        rightWrist_y[counter - 1] = Double.valueOf(Data[34]);
                    }
                    counter++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("readline", "end");

            Log.e("array", Arrays.toString(rightWrist_x));

            double sum = 0;
            double sum1 = 0;
            double sum2 = 0;
            double sum3 = 0;
            double sum4 = 0;
            double sum5 = 0;
            double sum6 = 0;
            double sum7 = 0;
            double sum8 = 0;
            double sum9 = 0;
            double sum10 = 0;
            double sum11 = 0;

            int n = counter;
            double[] mean = new double[13];

            for (int i = 0; i < counter - 1; i++) {

                sum = sum + leftSh_x[i];
                sum1 = sum1 + leftSh_y[i];
                sum2 = sum2 + rightSh_x[i];
                sum3 = sum3 + rightSh_y[i];
                sum4 = sum4 + leftElb_x[i];
                sum5 = sum5 + leftElb_y[i];
                sum6 = sum6 + rightElb_x[i];
                sum7 = sum7 + rightElb_y[i];
                sum8 = sum8 + leftWrist_x[i];
                sum9 = sum9 + leftWrist_y[i];
                sum10 = sum10 + rightWrist_x[i];
                sum11 = sum11 + rightWrist_y[i];

                //Log.e("total Matrix", "Total: " + sum);

            }

            Log.e("Total Sum:", "sum: " + sum + " "+ sum1 + " " + sum2 + " " + sum3);
            Log.e("Total Sum:", "sum: " + sum4 + " "+ sum5 + " " + sum6 + " " + sum7);
            Log.e("Total Sum:", "sum: " + sum8 + " "+ sum9 + " " + sum10 + " " + sum11);
            //Log.e("Counter what is it:" ,"" + counter + " " + n);

            double mean99 = 0;
            mean99 = sum11 / counter;
            //mean[1] = sum/counter;
            //Log.e("readline", "mean: " + mean99);
            //Log.e("readline", "mean[1]: " + mean[1]);

            n = n-1;
            Log.e("Counter what is it:" ,"" + counter + " " + n);

            mean[0] = sum / n;
            mean[1] = sum1 / n;
            mean[2] = sum2 / n;
            mean[3] = sum3 / n;
            mean[4] = sum4 / n;
            mean[5] = sum5 / n;
            mean[6] = sum6 / n;
            mean[7] = sum7 / n;
            mean[8] = sum8 / n;
            mean[9] = sum9 / n;
            mean[10] = sum10 / n;
            mean[11] = sum11 / n;

            Log.e("total Matrix2", "Total: " + mean[0]);

            Log.e("readline", "mean[]: " + Arrays.toString(mean));

            double [] Weights = {-0.0593642873427882, 0.0961219103271560, 0.0884218528655367, -0.109517461829910, 0.0366563345397992, 0.0397370692378729, 0.00795493201044906, -0.00599367318008603, -0.0438464116643331, 0.0364945425486667, 0.0168144548228852, -0.0309973982827785};
            double bias = -8.805615921003477;
            double total = 0;

            double total1 = mean[0]*Weights[0]+ mean[1]*Weights[1]+ mean[2]*Weights[2]+ mean[3]*Weights[3]+ mean[4]*Weights[4]+ mean[5]*Weights[5] + mean[6]*Weights[6]+ mean[7]*Weights[7]+ mean[8]*Weights[8]+ mean[9]*Weights[9]+ mean[10]*Weights[10]+ mean[11]*Weights[11] + bias;
            Log.e("total1", "Final 1: " + total1);

            for (int i=0; i < 12; i++){
                total = mean[i] * Weights[i] + total;
                //Log.e("mult Matrix", "mean[]: " + mean[i] + " " + Weights[i]);
                //Log.e("total Matrix", "Total: " + total);
            }

            total = total + bias;

            Log.e("Final total_bias", "bias: " + total);
            if (total <= 0)
            {
                fileOutput[iCount-2] = "This file name: " + filename[iCount-2] + "\nClassified as: " + total + "\nFile is: About";
                Log.e("Final result", "This file is About " + filename[iCount-2]);
            }
            else if(total > 0)
            {
                fileOutput[iCount-2] = "This file name: " + filename[iCount-2] + "\nClassified as: " + total + "\nFile is: Father";
                Log.e("Final result", "This file is Father " + filename[iCount-2]);
            }

            counter = 0;

        }

        Log.e("Final result", "This file is Father " + Arrays.toString(fileOutput));
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        duration = duration/1000000;
        Log.e("Timer", "took this long: " + duration + "milliseconds");
        fileOutput[globalCount] = "Time to execute task: " + duration + " milliseconds";
    }

}
