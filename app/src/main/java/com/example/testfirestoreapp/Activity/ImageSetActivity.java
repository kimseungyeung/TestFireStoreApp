package com.example.testfirestoreapp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.testfirestoreapp.Adapter.GridAdapter;
import com.example.testfirestoreapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class ImageSetActivity extends AppCompatActivity implements View.OnClickListener {
    GridView gv_thumnail;
    ArrayList<String> imagepathlist=null;
    FloatingActionButton fab,fab_1,fab_2;
    boolean isFabOpen=false;
    Animation fab_open,fab_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_activity);
        component();
    }
    public void component(){
        gv_thumnail=(GridView)findViewById(R.id.gv_thumnail);
       File f= new File(Environment.getExternalStorageDirectory().getAbsolutePath());
      File [] flist = f.listFiles();
      imagepathlist=new ArrayList<>();
      for(File file : flist){
       if(file.getName().contains(".jpg")||f.getName().contains(".png")) {
           String fpath = file.getAbsolutePath();
           imagepathlist.add(fpath);
       }
      }
      GridAdapter gadapter =new GridAdapter(getApplicationContext(),imagepathlist);
      gv_thumnail.setAdapter(gadapter);
      gv_thumnail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Bitmap bit =(Bitmap) gadapter.getItem(position);
             Intent i = new Intent(getApplicationContext(),FullImageActivity.class);
             i.putExtra("path",imagepathlist.get(position));
             startActivity(i);
          }
      });
        gadapter.notifyDataSetChanged();
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab_1=(FloatingActionButton)findViewById(R.id.fab_1);
        fab_2=(FloatingActionButton)findViewById(R.id.fab_2);
        fab_1.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_2.setOnClickListener(this);
        fab_open= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:


                    anim();

                break;
            case R.id.fab_1:

                anim();
                break;
            case R.id.fab_2:

                anim();
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    public void anim() {

        if (isFabOpen) {
            try{
                fab_1.startAnimation(fab_close);
                fab_2.startAnimation(fab_close);
                fab_1.setClickable(false);
                fab_2.setClickable(false);
                fab_1.setVisibility(View.INVISIBLE);
                fab_2.setVisibility(View.INVISIBLE);
                isFabOpen = false;
            }catch (Exception e){

            }
        } else {
            try{
                fab_1.setVisibility(View.VISIBLE);
                fab_2.setVisibility(View.VISIBLE);
                fab_1.startAnimation(fab_open);
                fab_2.startAnimation(fab_open);
                fab_1.setClickable(true);
                fab_2.setClickable(true);
                isFabOpen = true;
            }catch (Exception e){

            }
        }
    }
}
