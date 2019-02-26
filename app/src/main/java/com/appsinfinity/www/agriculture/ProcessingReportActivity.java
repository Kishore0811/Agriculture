package com.appsinfinity.www.agriculture;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ProcessingReportActivity extends AppCompatActivity {

    String first, second, third, total;

    Uri imageUri;

    ImageView img_gif;

    TextView t1;

    Handler handler, handler1, handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_report);

        img_gif = (ImageView) findViewById(R.id.img_1);
        Glide.with(this).load(R.drawable.img_report).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img_gif);

        t1 = findViewById(R.id.txt_success_message);
        t1.setText("Generating report..");

        first = getIntent().getStringExtra("first");
        second = getIntent().getStringExtra("second");
        third = getIntent().getStringExtra("third");
        total = getIntent().getStringExtra("total");
        imageUri = (Uri) getIntent().getExtras().get("fileUri");

        handler = new Handler();
        handler1 = new Handler();
        handler2 = new Handler();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.au_1);
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.au_2);
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.start();
            }
        }, 300);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                t1.setText("Report successfully generated.");
                mp2.start();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProcessingReportActivity.this, ResultActivity.class);
                        intent.putExtra("first", first);
                        intent.putExtra("second", second);
                        intent.putExtra("third", third);
                        intent.putExtra("total", total);
                        intent.putExtra("fileUri", imageUri);
                        startActivity(intent);
                        finish();
                    }
                }, 4000);

            }
        }, 9000);
    }
}
