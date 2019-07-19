package com.example.testfirestoreapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.testfirestoreapp.R;

import java.io.File;

public class FullImageActivity extends AppCompatActivity {
    Bitmap resultbit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);
        String imagepath =getIntent().getExtras().getString("path");
        Bitmap resultbit= BitmapFactory.decodeFile(imagepath);
        ImageView iv=(ImageView)findViewById(R.id.iv_full_image);
        iv.setImageBitmap(resultbit);
    }
}
