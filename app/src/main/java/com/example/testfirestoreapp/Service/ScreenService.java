package com.example.testfirestoreapp.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.service.dreams.*;

import com.example.testfirestoreapp.R;
import com.example.testfirestoreapp.Receiver.ScreenReceiver;

public class ScreenService extends Service {
    public ScreenReceiver mReceiver=null;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver=new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            if(intent.getAction()==null){
               if(mReceiver==null){
                   mReceiver=new ScreenReceiver();
                   IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                   registerReceiver(mReceiver,filter);
               }
                           }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }


}
