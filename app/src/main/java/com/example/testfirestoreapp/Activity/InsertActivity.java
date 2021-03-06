package com.example.testfirestoreapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Fragment.Fragment1;
import com.example.testfirestoreapp.Fragment.Fragment2;
import com.example.testfirestoreapp.Fragment.Fragment3;
import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class InsertActivity extends FragmentActivity implements View.OnClickListener {
    Button btn_next, btn_prev, btn_frag1, btn_frag2, btn_frag3,btn_record;
    FragmentManager fm;
    Fragment1 f1;
    Fragment2 f2;
    Fragment3 f3;
    Intent i;
    SpeechRecognizer mRecognizer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activity);
        f1 = new Fragment1();
        f2 = new Fragment2();
        f3 = new Fragment3();
        addFragment(f1);
        addFragment(f2);
        addFragment(f3);
        nextflag(1);
        component();
        btn_frag1.setBackgroundColor(getResources().getColor(R.color.skyblue));
    }

    public void component() {
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_frag1 = (Button) findViewById(R.id.btn_frag1);
        btn_frag2 = (Button) findViewById(R.id.btn_frag2);
        btn_frag3 = (Button) findViewById(R.id.btn_frag3);
        btn_record =(Button)findViewById(R.id.btn_record);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_frag1.setOnClickListener(this);
        btn_frag2.setOnClickListener(this);
        btn_frag3.setOnClickListener(this);
        btn_record.setOnClickListener(this);
    }

    public void setFragment(Fragment f) {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addFragment(Fragment f) {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_fragment, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void nextflag(int index) {
        if (index == 1) {


            fm.beginTransaction().show(f1).commit();
            fm.beginTransaction().hide(f2).commit();
            fm.beginTransaction().hide(f3).commit();
        } else if (index == 2) {

            fm.beginTransaction().hide(f1).commit();
            fm.beginTransaction().show(f2).commit();
            fm.beginTransaction().hide(f3).commit();
        } else if (index == 3) {

            fm.beginTransaction().hide(f1).commit();
            fm.beginTransaction().hide(f2).commit();
            fm.beginTransaction().show(f3).commit();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (getVisibleFragment() instanceof Fragment1) {
                    if(!f1.gettest()){
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    else if(f1.gettest()){
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.White));
                    }
                    btn_frag2.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        nextflag(2);

                }
                if (getVisibleFragment() instanceof Fragment2) {
                    if(!f1.gettest()){
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    else if(f1.gettest()){
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.White));
                    }
                    if(!f2.gettest()){
                        btn_frag2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    else if(f2.gettest()){
                        btn_frag2.setBackgroundColor(getResources().getColor(R.color.White));
                    }
                    btn_frag3.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        nextflag(3);

                }
                if (getVisibleFragment() instanceof Fragment3) {
                    if(f1.gettest()&&f2.gettest()&&f3.gettest()){
                        String result="";
                        ArrayList ss =f1.getresult();
                        ArrayList dd =f2.getresult();
                       ArrayList ff= f3.getresult();
                       ss.addAll(dd);
                       ss.addAll(ff);
                       for(int i=0; i<ss.size(); i++){
                           result=result+ss.get(i)+"\n";
                       }
                        resultDialog(result);
                    }else{
                        resultDialog("입력을 확인하세요");
                    }
                }

                break;
            case R.id.btn_prev:
                if (getVisibleFragment() instanceof Fragment2) {
                    btn_frag1.setBackgroundColor(getResources().getColor(R.color.skyblue));
                    btn_frag2.setBackgroundColor(getResources().getColor(R.color.White));
                    btn_frag3.setBackgroundColor(getResources().getColor(R.color.White));
                    nextflag(1);
                }
                if (getVisibleFragment() instanceof Fragment3) {
                    if(!f1.gettest()){
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        btn_frag1.setBackgroundColor(getResources().getColor(R.color.White));
                    }
                    btn_frag2.setBackgroundColor(getResources().getColor(R.color.skyblue));
                    btn_frag3.setBackgroundColor(getResources().getColor(R.color.White));
                    nextflag(2);
                }

                break;
            case R.id.btn_frag1:
                btn_frag1.setBackgroundColor(getResources().getColor(R.color.skyblue));
                btn_frag2.setBackgroundColor(getResources().getColor(R.color.White));
                btn_frag3.setBackgroundColor(getResources().getColor(R.color.White));
                    nextflag(1);
                break;
            case R.id.btn_frag2:

                if(!f1.gettest()){
                btn_frag1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                btn_frag1.setBackgroundColor(getResources().getColor(R.color.White));
            }
                btn_frag2.setBackgroundColor(getResources().getColor(R.color.skyblue));
                btn_frag3.setBackgroundColor(getResources().getColor(R.color.White));
                nextflag(2);

                break;
            case R.id.btn_frag3:
                if(!f1.gettest()){
                    btn_frag1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else{
                    btn_frag1.setBackgroundColor(getResources().getColor(R.color.White));
                }
                if(!f2.gettest()){
                    btn_frag2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else {
                    btn_frag2.setBackgroundColor(getResources().getColor(R.color.White));
                }
                btn_frag3.setBackgroundColor(getResources().getColor(R.color.skyblue));
                    nextflag(3);

                break;
            case R.id.btn_record:
                startlisten();
                break;
        }
    }
    public Fragment getVisibleFragment() {
        for (Fragment fragment: fm.getFragments()) {
            if (fragment.isVisible()) {
                return ((Fragment)fragment);
            }
        }
        return null;
    }

    public void set(){



    }
    public void startlisten(){

        mRecognizer.startListening(i);


    }
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식시작",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Toast.makeText(getApplicationContext(),"음성인식끝",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onError(int error) {
            String mError=null;
            switch (error) {

                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    mError = " network timeout";
                    startlisten();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    mError = " network";
                    //toast("Please check data bundle or network settings");
                    return;
                case SpeechRecognizer.ERROR_AUDIO:
                    mError = " audio";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    mError = " server";
                    startlisten();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    mError = " client";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    mError = " speech time out";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    mError = " no match";
//                    startlisten();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    mError = " recogniser busy";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    mError = " insufficient permissions";
                    break;


            }
            Toast.makeText(getApplicationContext(), mError, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle results) {
            String key = ""; key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            if(rs[0].equals("다음")){
                btn_next.performClick();
            }
            if(rs[0].equals("뒤로")){
                btn_prev.performClick();
            }
           else{
               f1.setd(rs[0]);
            }


//            mRecognizer.cancel();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(getVisibleFragment() instanceof Fragment1){
            finish();
        }
        else if(getVisibleFragment() instanceof Fragment2){
            btn_frag1.performClick();
        }
        else if(getVisibleFragment() instanceof Fragment3){
            btn_frag2.performClick();
        }
    }
    public void resultDialog(String text) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.result_dialog, null);
        dialogBuilder.setView(view);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_result = (TextView)view.findViewById(R.id.tv_result);
        tv_result.setText(text);
        Button btn_result_ok=(Button)view.findViewById(R.id.btn_result_ok);
        btn_result_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
}
