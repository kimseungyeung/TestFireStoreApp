package com.example.testfirestoreapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.testfirestoreapp.Activity.ScreenSaverActivity;

public class AppAplication extends Application {
    Handler h=null;
    public boolean startact=false; //핸들러 실행여부 체크
    @Override
    public void onCreate() {
        super.onCreate();

            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                    if (!Constants.active&&Constants.userlogin) {
                        Constants.active = true;
                        if(h!=null){
                            Log.e("화면보호기","핸들러삭제");
                            h.removeMessages(0);
                            Intent i = new Intent(getApplicationContext(), ScreenSaverActivity.class);
                            if(startact) {
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(i);
                                Constants.checklock = true;
                                startact = false;
                            }
                        }
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN&&Constants.userlogin) {
            Constants.active = false;
            h = new Handler();

            Log.e("화면보호기", "실행");
            if (!Constants.active) {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("화면보호기", "생성");
                        if(!Constants.checklock) {
                         startact=true;
                        }
                    }

                }, 20000);
            }
        }
    }
}
