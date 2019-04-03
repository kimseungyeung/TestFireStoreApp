package com.example.testfirestoreapp.Data;

import android.graphics.Bitmap;

public class ImageData {
    Bitmap bitmap;
    String bitmapName;
    public ImageData(Bitmap bit,String bitname){
        bitmap =bit;
        bitmapName=bitname;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getBitmapName() {
        return this.bitmapName;
    }
}
