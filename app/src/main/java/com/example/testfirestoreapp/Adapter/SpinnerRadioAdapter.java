package com.example.testfirestoreapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class SpinnerRadioAdapter extends BaseAdapter {
    String [] spinanme;
    Context context;
    LayoutInflater layoutInflater;
    public SpinnerRadioAdapter(Context ctx,String[] name){
        this.spinanme=name;
        this.context=ctx;
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder=null;
        if(convertView==null){
            convertView =layoutInflater.inflate(R.layout.item_spinner_radio,null);
             holder= new Viewholder();
            holder.tv_itemname=(TextView)convertView.findViewById(R.id.tv_itemname);
            holder.rbtn_check=(RadioButton)convertView.findViewById(R.id.rbtn_check);
            holder.tv_itemname.setText(spinanme[position]);
            convertView.setTag(holder);
        }else{
            holder=(Viewholder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return spinanme[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return spinanme.length;
    }
    public class Viewholder{
        TextView tv_itemname;
        RadioButton rbtn_check;

    }
}
