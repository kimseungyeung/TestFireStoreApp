package com.example.testfirestoreapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testfirestoreapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallingService extends Service {
    public static final String EXTRA_CALL_NUMBER = "call_number";
    protected View rootView;

    TextView tv_call_number;
    String call_number;
    WindowManager.LayoutParams params;
    private WindowManager windowManager;

    @Override
    public IBinder onBind(Intent intent) { // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.call_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setView(view);
      String phonenumber=  intent.getStringExtra(EXTRA_CALL_NUMBER);
     /*   dialogBuilder.setMessage(phonenumber);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
     TextView tv_text=(TextView)view.findViewById(R.id.tv_text);
     Button btn_customer_ok=(Button)view.findViewById(R.id.btn_customer_ok);
        Button btn_customer_view=(Button)view.findViewById(R.id.btn_customer_view);
     String name="";
     if(phonenumber.equals("010-9930-1510")){
         name="안성수";
     }else if(phonenumber.equals("010-9525-9533")){
         name="전성하";
     }else if(phonenumber.equals("010-6646-9774")){
         name="김현";
     }
     String text=name+"\n"+phonenumber+"\n"
             +"계약번호 15-04290928-001"+"\n"
             +"계약명:무배당 프로미라이프 다이렉트 운전자보험1304"+"\n"
             +"생년월일: (남)520824"+"\n"
             +"주소: 지번주소)10938"+"\n" +
             "경기 파주시 초리읍 두루평로13432 동부금융센터"+"\n"
             +"보험기간: 2017-09-25~2027-09-25"+"\n"
             +"만기/납기: 15년만기/15년납기\n"
             +"가입형태: 개인\n"
             +"계약상태: 정상\n"
             +"사고사항: 있음(1)\n"
             +"보상: 박창수\n"
             +"조사: 김성근";
     tv_text.setText(text);
        AlertDialog alertDialog = dialogBuilder.create();
        btn_customer_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alertDialog.dismiss();
            }
        });
        btn_customer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(Build.VERSION_CODES.O< Build.VERSION.SDK_INT) {

                    startActivity(i);
                }else{

                    startActivity(i);
                }
                alertDialog.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1);
        } else {
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        return START_REDELIVER_INTENT;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try  {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //stopForeground(true);
            }
        }catch (Exception e){

        }
    }


}


