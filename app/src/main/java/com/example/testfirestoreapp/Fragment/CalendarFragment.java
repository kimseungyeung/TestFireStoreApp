package com.example.testfirestoreapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Activity.LoginActivity;
import com.example.testfirestoreapp.Adapter.CalendarAdapter;
import com.example.testfirestoreapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements View.OnClickListener {
    int DAY_COUNT=42; //전체 칸수
    GridView gr_calendar;
    TextView tv_date;
    Button btn_nextmonth,btn_prevmonth;
    ArrayList<Date>daylist;
    Calendar mcal;
    int nowyear;
    int nowmonth;
    int nowdate;
    CalendarAdapter cadapter;
    static List<String> df;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_layout,null);
        component(v);
        return v;
    }
    public void component(View v){
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        nowyear=Integer.parseInt(curYearFormat.format(date));
        nowmonth=Integer.parseInt(curMonthFormat.format(date));
        nowdate=Integer.parseInt(curDayFormat.format(date));
        mcal=Calendar.getInstance();
        mcal.set(nowyear,nowmonth-1,1);

        int daynum=mcal.get(Calendar.DAY_OF_WEEK)-1;
        mcal.add(Calendar.DAY_OF_MONTH,-daynum);
        int lastdate=mcal.getActualMaximum(Calendar.DAY_OF_MONTH);
        gr_calendar=(GridView)v.findViewById(R.id.gr_calendar);
        tv_date=(TextView)v.findViewById(R.id.tv_date);
        tv_date.setText(curMonthFormat.format(date)+"월"+":"+Integer.toString(daynum)+":"+lastdate);
        btn_nextmonth=(Button)v.findViewById(R.id.btn_nextmonth);
        btn_prevmonth=(Button)v.findViewById(R.id.btn_prevmonth);
        btn_nextmonth.setOnClickListener(this);
        btn_prevmonth.setOnClickListener(this);
        daylist=new ArrayList<Date>();
        for(int i=0; i<DAY_COUNT; i++){
            daylist.add(mcal.getTime());
            mcal.add(Calendar.DAY_OF_MONTH,1);
        }
        int c=0;
        cadapter=new CalendarAdapter(getActivity(),daylist,nowmonth);

        gr_calendar.setAdapter(cadapter);
        gr_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),""+cadapter.getItem(position),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_prevmonth:
                if(nowmonth==1){
                    nowyear--;
                    nowmonth=12;
                }else {
                    nowmonth--;
                }
                mcal.set(nowyear,nowmonth-1,1);
                int daynum=mcal.get(Calendar.DAY_OF_WEEK)-1;
                mcal.add(Calendar.DAY_OF_MONTH,-daynum);
                cadapter.setDatelist(mcal,nowmonth);
                tv_date.setText(nowyear+"년"+"/"+nowmonth+"월");
                break;
            case R.id.btn_nextmonth:
                if(nowmonth==12){
                 nowyear++;
                 nowmonth=1;
                }else {
                    nowmonth++;
                }

                mcal.set(nowyear,nowmonth-1,1);
                int daynum2=mcal.get(Calendar.DAY_OF_WEEK)-1;
                mcal.add(Calendar.DAY_OF_MONTH,-daynum2);
                cadapter.setDatelist(mcal,nowmonth);
                tv_date.setText(nowyear+"년"+"/"+nowmonth+"월");
                break;
        }
    }


}
