package com.example.testfirestoreapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.testfirestoreapp.Activity.ScreenSaverActivity;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean screencheck=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            screencheck=false;
            if(!screencheck) {
                Intent i = new Intent(context, ScreenSaverActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            screencheck=true;
        }
    }
}
