package com.example.testfirestoreapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.testfirestoreapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraStampActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_camera;
    ImageView iv_picture;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_stamp_activity);
        component();
    }
    public void component(){
        btn_camera=(Button)findViewById(R.id.btn_camera);
        iv_picture=(ImageView)findViewById(R.id.iv_picture);
        btn_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
           Bitmap resultbit=cameramask(imageBitmap);
            iv_picture.setImageBitmap(resultbit);


        }

    }
    public Bitmap cameramask(Bitmap bit){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime =sdf.format(date);
        Bitmap bmOverlay = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), bit.getConfig());


        Canvas canvas = new Canvas(bmOverlay);
        Typeface tp =Typeface.create(Typeface.SERIF,Typeface.BOLD);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTypeface(tp);

        canvas.drawBitmap(bit, 0, 0, null);
        canvas.drawText(getTime,30,bit.getHeight()/2,p);
        return bmOverlay;
    }
}
