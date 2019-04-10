package com.example.testfirestoreapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

public interface InCallback {
    public void firebaseGlide(String email, String name , ImageView iv, Context ctx); //이미지 둥글게 셋팅
    public void pictureupload(Bitmap bitmap, String email, String imagename, Uri uip); //갤러리선택 업로드
    public String nowtime();
}
