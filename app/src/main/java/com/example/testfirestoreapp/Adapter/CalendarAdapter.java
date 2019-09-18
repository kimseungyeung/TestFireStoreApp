package com.example.testfirestoreapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.testfirestoreapp.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class CalendarAdapter extends BaseAdapter  {
    private  List<Date> datelist;
    private LayoutInflater inflater;
    private Context context;
    private SimpleDateFormat day;
    private SimpleDateFormat month;
    private SimpleDateFormat year;
    private SimpleDateFormat all;
    private int nowmonth;
    private Date nowDate;
    public CalendarAdapter(Context ctx,List<Date>dlist,int nmonth){
        this.datelist=dlist;
        this.context=ctx;
        this.inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.day=new SimpleDateFormat("dd", Locale.KOREA);
        this.month=new SimpleDateFormat("MM", Locale.KOREA);
        this.year=new SimpleDateFormat("yy", Locale.KOREA);
        this.all =new SimpleDateFormat("yy-MM-dd",Locale.KOREA);
        this.nowmonth=nmonth;
        this.nowDate=new Date(System.currentTimeMillis());
        this.nowDate.setMonth(this.nowDate.getMonth());
    }

    @Override
    public int getCount() {
        return datelist.size();
    }

    @Override
    public Object getItem(int position) {
        return datelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        int m=Integer.parseInt(month.format(getItem(position)));
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_calendar_date,null);
            holder=new ViewHolder();
            holder.tv_item_date=(TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tv_item_date.setText(""+day.format(getItem(position)));
        if(all.format(this.nowDate).equals(all.format(getItem(position)))){
            holder.tv_item_date.setBackgroundColor(context.getResources().getColor(R.color.blue));
            holder.tv_item_date.setTextColor(context.getResources().getColor(R.color.White));
        }else{
            holder.tv_item_date.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.tv_item_date.setTextColor(context.getResources().getColor(R.color.black));
        }
        if(m!=nowmonth){
            holder.tv_item_date.setTextColor(context.getResources().getColor(R.color.quantum_grey));
        }else{
            holder.tv_item_date.setTextColor(context.getResources().getColor(R.color.black));
        }
        return convertView;
    }

    public class ViewHolder{
        TextView tv_item_date;
    }

    public void setDatelist(Calendar c,int nmon){
        this.nowmonth=nmon;
        this.datelist.clear();
        this.datelist=new ArrayList<>();
        for(int i=0; i<42; i++){
            datelist.add(c.getTime());
            c.add(Calendar.DAY_OF_MONTH,1);
        }
        notifyDataSetChanged();
    }
}
