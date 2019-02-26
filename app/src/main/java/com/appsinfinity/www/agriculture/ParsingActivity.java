package com.appsinfinity.www.agriculture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ParsingActivity extends AppCompatActivity {

    Handler handler1, handler2, handler3, handler4;
    TextView a;
    ProgressBar b;
    ImageView c;
    String imageName, imageSize;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing);

        a = findViewById(R.id.txt_success_message);
        b = findViewById(R.id.bar_1);
        c = findViewById(R.id.img_1);

        imageUri = (Uri) getIntent().getExtras().get("fileUri");
        imageName = getIntent().getStringExtra("imageName");
        imageSize = getIntent().getStringExtra("imageSize");

        a.setText("Accessing Storage..");

        handler1 = new Handler();
        handler2 = new Handler();
        handler3 = new Handler();
        handler4 = new Handler();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                a.setText("Fetching image..");
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ParsingActivity.this,
                                "Image Fetched Successfully",
                                Toast.LENGTH_SHORT).show();
                        a.setText("Initialize parsing..");
                        handler3.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                a.setText("Image Parsed Successfully.");
                                b.setVisibility(View.GONE);
                                c.setVisibility(View.VISIBLE);
                                Toast.makeText(ParsingActivity.this,
                                        "Please wait..",
                                        Toast.LENGTH_SHORT).show();
                                final Intent intent = new Intent(ParsingActivity.this,
                                        ProcessingActivity.class);
                                intent.putExtra("fileUri", imageUri);
                                intent.putExtra("imageName", imageName);
                                intent.putExtra("imageSize", imageSize);
                                handler4.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        startActivity(intent);
                                    }
                                },3000);
                            }
                        }, 4000);

                    }
                }, 3000);
            }
        },4000);
    }

    @Override
    public void onBackPressed() {
        handler1.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        handler3.removeCallbacksAndMessages(null);
        handler4.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }
}
