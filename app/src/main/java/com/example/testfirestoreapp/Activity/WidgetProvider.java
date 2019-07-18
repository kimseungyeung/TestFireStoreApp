package com.example.testfirestoreapp.Activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.widget.RemoteViews;

import com.example.testfirestoreapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }



    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews updateViews=new RemoteViews(context.getPackageName(), R.layout.widget_preview);
        Intent i = new Intent(context,LoginActivity.class);
        PendingIntent pintent= PendingIntent.getActivity(context,0,i,0);
        updateViews.setOnClickPendingIntent(R.id.btn_1,pintent);
        Intent i2 = new Intent(context, Camera.class);
        PendingIntent pintent2= PendingIntent.getActivity(context,0,i,0);
        updateViews.setOnClickPendingIntent(R.id.btn_2,pintent);
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, WidgetProvider.class),updateViews);
    }
    public static void updateAppWidget(Context context,AppWidgetManager appWidgetManager,int appwidgetid){
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        RemoteViews updateViews=new RemoteViews(context.getPackageName(), R.layout.widget_preview);
        Intent i = new Intent(context,LoginActivity.class);
        PendingIntent pintent= PendingIntent.getActivity(context,0,i,0);
        updateViews.setOnClickPendingIntent(R.id.btn_1,pintent);
        Intent i2 = new Intent(context, Camera.class);
        PendingIntent pintent2= PendingIntent.getActivity(context,0,i,0);
        updateViews.setOnClickPendingIntent(R.id.btn_2,pintent);
        AppWidgetManager.getInstance(context).updateAppWidget(appwidgetid,updateViews);
    }
}
