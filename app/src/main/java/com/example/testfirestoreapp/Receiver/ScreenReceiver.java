package com.example.testfirestoreapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.testfirestoreapp.Activity.ScreenSaverActivity;
import com.example.testfirestoreapp.Constants;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean screencheck=false;
    Handler h;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            h=new Handler();
            screencheck=false;

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!screencheck) {
                        Intent i = new Intent(context, ScreenSaverActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            },10000);

        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
           // screencheck=true;
        }else if(intent.getAction().equals(Intent.	ACTION_USER_PRESENT)){
            screencheck=true;
            if(h!=null){
                h.removeMessages(0);
            }
        }
    }
}
