package com.appsinfinity.www.agriculture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    String first, second, third, total, medicinee, percentage;

    TextView t1, t2, t3;

    Integer firstI, secondI, thirdI;

    Uri imageUri;


    ImageView imv_result_1;

    Bitmap bmp1;

    int[] intArray1;

    ProgressDialog progressdialog;

    MediaPlayer mp1, mp2, mp3, mp4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressdialog = new ProgressDialog(ResultActivity.this);
        progressdialog.setMessage("Generating full resolution images...");
        progressdialog.show();
        progressdialog.setCancelable(false);

        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        total = getIntent().getStringExtra("total");
        imageUri = (Uri) getIntent().getExtras().get("fileUri");

        mp1 = MediaPlayer.create(this, R.raw.au_3);
        mp2 = MediaPlayer.create(this, R.raw.au_4);
        mp3 = MediaPlayer.create(this, R.raw.au_5);
        mp4 = MediaPlayer.create(this, R.raw.au_6);

        Log.e("Result", "here "+first+" "+second+" "+third+" "+total);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog = new ProgressDialog(ResultActivity.this);
                progressdialog.setMessage("Please wait...");
                progressdialog.show();
                progressdialog.setCancelable(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressdialog.dismiss();
                        Intent intent = new Intent(ResultActivity.this, MedicineActivity.class);
                        intent.putExtra("medicine", medicinee);
                        intent.putExtra("percentage", percentage);
                        startActivity(intent);
                    }
                }, 2000);
            }
        });

        firstI = Integer.parseInt(first);
        secondI = Integer.parseInt(second);
        thirdI = Integer.parseInt(third);


        t1 = findViewById(R.id.txt_first);
        t2 = findViewById(R.id.txt_second);
        t3 = findViewById(R.id.txt_third);

        initialize();
    }

    private void initialize() {
        if (firstI < 100 && secondI < 100 && thirdI < 100) {
            t1.setText("No disease Found. Plant is healthy.");
            medicinee = "4";
            Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp4.start();
                    }
                },300);
            startProcessing("1");
        } else {
            if ((firstI > secondI) && (firstI > thirdI)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp1.start();
                    }
                },300);

                Float dd = (Float.parseFloat(first) / Float.parseFloat(total)) * 100;
                t1.setText("Bacterial Leaf Streak Found. (" + dd + "%).");
                percentage = String.valueOf(dd);
                startProcessing("1");
                medicinee = "1";
            } else if (secondI > thirdI) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp2.start();
                    }
                },300);

                Float dd = ((Float.parseFloat(second) / Float.parseFloat(total)) * 100);
                t1.setText("Brown Spot Found. (" + dd + "%).");
                percentage = String.valueOf(dd);
                medicinee = "2";
                startProcessing("2");
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mp3.start();
                    }
                },300);
                Float dd = ((Float.parseFloat(third) / Float.parseFloat(total)) * 100);
                t1.setText("Black Horse Riding Found. (" + dd + "%).");
                percentage = String.valueOf(dd);
                medicinee = "3";
                startProcessing("3");
            }
        }
    }

    private void startProcessing(String choice) {
        try {
            bmp1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imv_result_1 = (ImageView) findViewById(R.id.img_cover_top);

        //Guarantees that the image is decoded in the ARGB8888 format
        bmp1 = bmp1.copy(Bitmap.Config.ARGB_8888, true);


        //Initialize the intArray with the same size as the number of pixels on the image
        intArray1 = new int[bmp1.getWidth()*bmp1.getHeight()];

        //copy pixel data from the Bitmap into the 'intArray' array
        bmp1.getPixels(intArray1, 0, bmp1.getWidth(), 0, 0, bmp1.getWidth(), bmp1.getHeight());

        //replace the red pixels with yellow ones
        if(choice.equals("1")) {
            for (int i = 0; i < intArray1.length; i++) {
                int redValue = Color.red(intArray1[i]);
                int blueValue = Color.blue(intArray1[i]);
                int greenValue = Color.green(intArray1[i]);
                if (
                        (redValue > 137 && redValue < 224) &&
                                (greenValue > 130 && greenValue < 231) &&
                                (blueValue > 120 && blueValue < 241)
                        ) {
                    intArray1[i] = 0xFFFF0000;
                }
            }
        }

        if(choice.equals("2")) {
            for (int i = 0; i < intArray1.length; i++) {
                int redValue = Color.red(intArray1[i]);
                int blueValue = Color.blue(intArray1[i]);
                int greenValue = Color.green(intArray1[i]);
                if (
                        (redValue > 159 && redValue < 182) &&
                                (greenValue > 120 && greenValue < 158) &&
                                (blueValue > 58 && blueValue < 88)
                        ) {
                    intArray1[i] = 0xFFFF0000;
                }
            }
        }

        if(choice.equals("3")) {
            for (int i = 0; i < intArray1.length; i++) {
                int redValue = Color.red(intArray1[i]);
                int blueValue = Color.blue(intArray1[i]);
                int greenValue = Color.green(intArray1[i]);
                if (
                        (redValue > 0 && redValue < 95) &&
                                (greenValue > 0 && greenValue < 100) &&
                                (blueValue > 1 && blueValue < 127)
                        ) {
                    intArray1[i] = 0xFFFF0000;
                }
            }
        }

        //Initialize the bitmap, with the replaced color
        bmp1 = Bitmap.createBitmap(intArray1, bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);

        //Draw the bitmap with the replaced color
        imv_result_1.setImageBitmap(bmp1);

        progressdialog.dismiss();
    }

}
