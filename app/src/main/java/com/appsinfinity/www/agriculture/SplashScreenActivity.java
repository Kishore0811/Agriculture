package com.appsinfinity.www.agriculture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView img_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img_gif = (ImageView) findViewById(R.id.img_gif);
        Glide.with(this).load(R.drawable.img_24).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img_gif);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=  new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        }, 2300);
    }
}
