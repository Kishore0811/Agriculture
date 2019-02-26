package com.appsinfinity.www.agriculture;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int PICK_IMAGE = 100;

    ImageView img_chosen_image;
    Button btn_choose_image, btn_continue;
    Uri imageUri;
    String fileName, fileSize;
    private boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img_chosen_image = (ImageView)findViewById(R.id.imv_chosen_image);
        btn_choose_image = (Button)findViewById(R.id.btn_change_image);
        btn_continue = (Button)findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(this);
        btn_choose_image.setOnClickListener(this);

        chooseImage();

    }

    private void chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_PERMISSION);
        } else {
            try {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to perform this operation.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                Intent gallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to perform this operation.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Cursor returnCursor = getContentResolver().query(
                    imageUri,
                    null,
                    null,
                    null,
                    null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            fileName = returnCursor.getString(nameIndex);
            fileSize = returnCursor.getString(sizeIndex);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                img_chosen_image.setImageBitmap(bitmap);
                isImageSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_change_image){
            chooseImage();
        } else if (view.getId() == R.id.btn_continue){
            if (isImageSelected) {
                final ProgressDialog progressView = new ProgressDialog(
                        this
                );
                progressView.setMessage("Please wait..");
                progressView.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressView.setCancelable(false);
                progressView.show();

                final Intent i = new Intent(MainActivity.this, ParsingActivity.class);
                i.putExtra("fileUri", imageUri);
                i.putExtra("imageName", fileName);
                i.putExtra("imageSize", fileSize);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressView.dismiss();
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }, 2000);
            } else {
                Toast.makeText(MainActivity.this, "Please select an image.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
