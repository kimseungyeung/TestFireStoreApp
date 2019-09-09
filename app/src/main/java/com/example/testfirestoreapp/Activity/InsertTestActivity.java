package com.example.testfirestoreapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirestoreapp.Adapter.SpinnerRadioAdapter;
import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class InsertTestActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner spin_1,spin_2,spin_3;
    TextView tv_result;
    String sp_text1="";
    String sp_text2="";
    String sp_text3="";
    Button btn_result;
    SpeechRecognizer mRecognizer;
    Intent i;
    EditText edt_record;
    FloatingActionButton fab_record;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserttestactivity);
        component();
    }
    public void component(){
        String[] spin1 = getResources().getStringArray(R.array.spin_1);
        String[] spin2 = getResources().getStringArray(R.array.spin_2);
        String[] spin3 = getResources().getStringArray(R.array.spin_3);
        edt_record=(EditText)findViewById(R.id.edt_record);
        fab_record=(FloatingActionButton) findViewById(R.id.fab_record);
        fab_record.setOnClickListener(this);
        spin_1=(Spinner)findViewById(R.id.spin_1);
        spin_2=(Spinner)findViewById(R.id.spin_2);
        spin_3=(Spinner)findViewById(R.id.spin_3);
        ArrayAdapter<String>spin_adapter1=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,spin1);
       // SpinnerRadioAdapter spin_adapter1=new SpinnerRadioAdapter(getApplicationContext(),spin1);
        spin_adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin_1.setAdapter(spin_adapter1);

        btn_result=(Button)findViewById(R.id.btn_result);
        tv_result=(TextView)findViewById(R.id.tv_result);
        btn_result.setOnClickListener(this);
        spin_1.setOnItemSelectedListener(this);
        spin_2.setOnItemSelectedListener(this);
        spin_3.setOnItemSelectedListener(this);
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spin_1:
            //   sp_text1=spin_1.getSelectedItem().toString();

                break;
            case R.id.spin_2:
          //      sp_text2=spin_2.getSelectedItem().toString();
                break;
            case R.id.spin_3:
         //       sp_text3=spin_3.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_result:
                tv_result.setText(sp_text1+" "+sp_text2+" "+sp_text3);
                break;
            case R.id.fab_record:
                startlisten();
                break;
        }
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

            edt_record.append(rs[0]);


//            mRecognizer.cancel();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };
}
