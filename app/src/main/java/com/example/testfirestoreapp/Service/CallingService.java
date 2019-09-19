package com.example.testfirestoreapp.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Activity.LoginActivity;
import com.example.testfirestoreapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

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


      String phonenumber=  intent.getStringExtra(EXTRA_CALL_NUMBER);
     /*   dialogBuilder.setMessage(phonenumber);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

     new DataLoadTask(phonenumber).execute();
//     String text=name+"\n"+phonenumber+"\n"
//             +"계약번호 15-04290928-001"+"\n"
//             +"계약명:무배당 프로미라이프 다이렉트 운전자보험1304"+"\n"
//             +"생년월일: (남)520824"+"\n"
//             +"주소: 지번주소)10938"+"\n" +
//             "경기 파주시 초리읍 두루평로13432 동부금융센터"+"\n"
//             +"보험기간: 2017-09-25~2027-09-25"+"\n"
//             +"만기/납기: 15년만기/15년납기\n"
//             +"가입형태: 개인\n"
//             +"계약상태: 정상\n"
//             +"사고사항: 있음(1)\n"
//             +"보상: 박창수\n"
//             +"조사: 김성근";


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

    public class DataLoadTask extends AsyncTask<String,String,String> {
        //httpurl 참조변수
        HttpURLConnection urlConnection =null;
        //url뒤에 붙여서 보낼 파라미터

        String result="";

        String pn="";
        public DataLoadTask(String phonenum){

            pn=phonenum;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://10.167.107.208:8080/test2");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                StringBuilder sb2 = new StringBuilder();
                sb2.append(URLEncoder.encode("phonnum","UTF-8"));
                sb2.append("=");
                sb2.append(URLEncoder.encode(pn,"UTF-8"));
                byte [] pd=sb2.toString().getBytes("UTF-8");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(pd);

                StringBuilder sb = new StringBuilder();

                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    result+=line;
                }


                Log.e("결과",result);

            } catch (MalformedURLException ee) {
                ee.printStackTrace();
                result=ee.getMessage().toString();
                Log.e("MalformedURL에러",ee.getMessage().toString());
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                result=e.getMessage().toString();
                Log.e("IOE에러",e.getMessage().toString());
                return result;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("결과",s);
            Context mContext = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.call_dialog, null);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            dialogBuilder.setView(view);
            TextView tv_text=(TextView)view.findViewById(R.id.tv_text);
            Button btn_customer_ok=(Button)view.findViewById(R.id.btn_customer_ok);
            Button btn_customer_view=(Button)view.findViewById(R.id.btn_customer_view);
            String name="";
            if(pn.equals("010-9930-1510")){
                name="안성수";
            }else if(pn.equals("010-9525-9533")){
                name="전성하";
            }else if(pn.equals("010-6646-9774")){
                name="김현";
            }
            tv_text.setText(name+"\n"+s);
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
                    Intent i = new Intent(mContext, LoginActivity.class);
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
        }
    }
}


