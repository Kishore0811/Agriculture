package com.appsinfinity.www.agriculture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class ProcessingActivity extends AppCompatActivity {

    ImageView imv_result_1, imv_result_2, imv_result_3;
    Bitmap bmp1, bmp2, bmp3;
    int[] intArray1, intArray2, intArray3;
    Uri imageUri;
    ProgressDialog progressdialog;
    Button btn_see_results;
    String imageName, imageSize;
    Integer count1 = 0, count2 = 0, count3 = 0;
    private int REQUEST_READ_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_see_results = (Button) findViewById(R.id.btn_see_results);
        btn_see_results.setVisibility(View.GONE);
        imageUri = (Uri) getIntent().getExtras().get("fileUri");
        imageName = getIntent().getStringExtra("imageName");
        imageSize = getIntent().getStringExtra("imageSize");

        progressdialog = new ProgressDialog(ProcessingActivity.this);
        progressdialog.setMessage("Generating full resolution images...");
        progressdialog.show();
        progressdialog.setCancelable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startProcessing();
            }
        },1000);

        btn_see_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProcessingActivity.this, ProcessingReportActivity.class);
                intent.putExtra("first", count1.toString());
                intent.putExtra("second", count2.toString());
                intent.putExtra("third", count3.toString());
                intent.putExtra("fileUri", imageUri);
                intent.putExtra("total", String.valueOf(intArray1.length));
                startActivity(intent);
            }
        });
    }

    private void startProcessing() {
        try {
            bmp1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bmp2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bmp3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imv_result_1 = (ImageView) findViewById(R.id.img_result_1);
        imv_result_2 = (ImageView) findViewById(R.id.img_result_2);
        imv_result_3 = (ImageView) findViewById(R.id.img_result_3);

        //Guarantees that the image is decoded in the ARGB8888 format
        bmp1 = bmp1.copy(Bitmap.Config.ARGB_8888, true);
        bmp2 = bmp2.copy(Bitmap.Config.ARGB_8888, true);
        bmp3 = bmp2.copy(Bitmap.Config.ARGB_8888, true);


        //Initialize the intArray with the same size as the number of pixels on the image
        intArray1 = new int[bmp1.getWidth()*bmp1.getHeight()];
        intArray2 = new int[bmp1.getWidth()*bmp1.getHeight()];
        intArray3 = new int[bmp1.getWidth()*bmp1.getHeight()];

        //copy pixel data from the Bitmap into the 'intArray' array
        bmp1.getPixels(intArray1, 0, bmp1.getWidth(), 0, 0, bmp1.getWidth(), bmp1.getHeight());
        bmp2.getPixels(intArray2, 0, bmp2.getWidth(), 0, 0, bmp2.getWidth(), bmp2.getHeight());
        bmp3.getPixels(intArray3, 0, bmp3.getWidth(), 0, 0, bmp3.getWidth(), bmp3.getHeight());

        //replace the red pixels with yellow ones
        for (int i=0; i < intArray1.length; i++)
        {
            int redValue = Color.red(intArray1[i]);
            int blueValue = Color.blue(intArray1[i]);
            int greenValue = Color.green(intArray1[i]);
            if(
                    (redValue > 137 && redValue < 224) &&
                            (greenValue > 130 && greenValue < 231) &&
                            (blueValue > 120 && blueValue < 241)
                    ) {
                intArray1[i] =  0xFFFF0000;
                count1 = count1 + 1;
            }
        }

        for (int i=0; i < intArray1.length; i++)
        {
            int redValue = Color.red(intArray1[i]);
            int blueValue = Color.blue(intArray1[i]);
            int greenValue = Color.green(intArray1[i]);
            if(
                    (redValue > 159 && redValue < 182) &&
                            (greenValue > 120 && greenValue < 158) &&
                            (blueValue > 58 && blueValue < 88)
                    ) {
                intArray2[i] =  0xFFFF0000;
                count2 = count2 + 1;
            }
        }

        for (int i=0; i < intArray3.length; i++)
        {
            int redValue = Color.red(intArray3[i]);
            int blueValue = Color.blue(intArray3[i]);
            int greenValue = Color.green(intArray3[i]);
            if(
                    (redValue > 0 && redValue < 95) &&
                            (greenValue > 0 && greenValue < 100) &&
                            (blueValue > 1 && blueValue < 127)
                    ) {
                intArray3[i] =  0xFFFF0000;
                count3 = count3 + 1;
            }
        }

        //Initialize the bitmap, with the replaced color
        bmp1 = Bitmap.createBitmap(intArray1, bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);
        bmp2 = Bitmap.createBitmap(intArray2, bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);
        bmp3 = Bitmap.createBitmap(intArray3, bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);

        //Draw the bitmap with the replaced color
        imv_result_1.setImageBitmap(bmp1);
        imv_result_2.setImageBitmap(bmp2);
        imv_result_3.setImageBitmap(bmp3);

        btn_see_results.setVisibility(View.VISIBLE);
        progressdialog.dismiss();

    }
}
