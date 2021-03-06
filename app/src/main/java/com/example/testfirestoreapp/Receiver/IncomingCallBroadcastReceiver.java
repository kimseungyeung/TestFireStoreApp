package com.example.testfirestoreapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.testfirestoreapp.Service.CallingService;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "PHONE STATE";
    private static String mLastState;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()"); /** * http://mmarvick.github.io/blog/blog/lollipop-multiple-broadcastreceiver-call-state/ * 2번 호출되는 문제 해결 */
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//        if (state.equals(mLastState)) {
//            return;
//        } else {
//            mLastState = state;
//        }
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (incomingNumber!=null) {

                final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
                Intent serviceIntent = new Intent(context, CallingService.class);
                serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    context.startService(serviceIntent);
                } else {
                    context.startForegroundService(serviceIntent);

                }
            }
        }
    }
}
