package com.example.testfirestoreapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    ArrayList<String> imagepathlist;
    ArrayList<Bitmap> bitmaps;
    Context context;

    public LayoutInflater Inflater =null;
    public GridAdapter(Context con,ArrayList<String>ipathlist){
        this.imagepathlist=ipathlist;
        this.context=con;
        bitmaps=new ArrayList<>();
        Inflater =(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(String path :imagepathlist){
            Bitmap bit=  BitmapFactory.decodeFile(path);

            bitmaps.add(bit);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return imagepathlist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=null;
        if(convertView == null) {
            convertView = Inflater.inflate(R.layout.item_grid_image, null);


        }else{
            imageView = (ImageView)convertView;
        }
        imageView = (ImageView)convertView.findViewById(R.id.iv_grid_image);
        imageView.setImageBitmap(bitmaps.get(position));
        return imageView;


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return bitmaps.get(position);
    }


}
