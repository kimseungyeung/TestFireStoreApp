package com.example.testfirestoreapp.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.testfirestoreapp.R;

public class SmSReceiver extends BroadcastReceiver {

    // 지정한 특정 액션이 일어나면 수행되는 메서드
    @Override
    public void onReceive(Context context, Intent intent) {

        // SMS를 받았을 경우에만 반응하도록 if문을 삽입
        if (intent.getAction().equals(
                "android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder sms = new StringBuilder();    // SMS문자를 저장할 곳
            Bundle bundle = intent.getExtras();         // Bundle객체에 문자를 받아온다

            if (bundle != null) {
                // 번들에 포함된 문자 데이터를 객체 배열로 받아온다
                Object[] pdusObj = (Object[])bundle.get("pdus");

                // SMS를 받아올 SmsMessage 배열을 만든다
                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < pdusObj.length; i++) {
                    messages[i] =
                            SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // SmsMessage의 static메서드인 createFromPdu로 pdusObj의
                    // 데이터를 message에 담는다
                    // 이 때 pdusObj는 byte배열로 형변환을 해줘야 함
                }

                // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                for (SmsMessage smsMessage : messages) {
                    // getMessageBody메서드는 문자 본문을 받아오는 메서드
                    sms.append(smsMessage.getMessageBody());
                }

                sms.toString() ;// StringBuilder 객체 sms를 String으로 변환

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.call_dialog, null);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setView(view);
                TextView tv_text=(TextView)view.findViewById(R.id.tv_text);
                Button btn_customer_ok=(Button)view.findViewById(R.id.btn_customer_ok);
                tv_text.setText(sms);
                AlertDialog alertDialog = dialogBuilder.create();
                btn_customer_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0新特性
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1);
                } else {
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                }
                alertDialog.show();

            }
        }
    }
}