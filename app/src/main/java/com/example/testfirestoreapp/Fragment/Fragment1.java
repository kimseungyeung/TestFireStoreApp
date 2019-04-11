package com.example.testfirestoreapp.Fragment;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class Fragment1 extends Fragment {
    EditText edt_email,edt_password,edt_nickname,edt_phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.insert_fragment,null);
        component(view);
        return view;
    }
    public void component(View v){
        edt_email =(EditText)v.findViewById(R.id.edt_create_email);
        edt_password =(EditText)v.findViewById(R.id.edt_create_password);
        edt_nickname =(EditText)v.findViewById(R.id.edt_create_nickname);
        edt_phone =(EditText)v.findViewById(R.id.edt_create_phonenum);

    }
    public boolean gettest(){
        String email =edt_email.getText().toString();
        String password =edt_password.getText().toString();
        String nickname =edt_nickname.getText().toString();
        String phone =edt_phone.getText().toString();
        if(!email.equals("")&&!password.equals("")&&!nickname.equals("")&&!phone.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    public EditText getedt(){
        return edt_nickname;
    }
    public void setd(String voice){
        if(voice.equals("이메일입력")){
            edt_email.setFocusable(true);
        }
        if(voice.equals("패스워드입력")){
            edt_password.setFocusable(true);
        }
        if(voice.equals("닉네임입력")){
            edt_nickname.setFocusable(true);
        }
        if(voice.equals("폰입력")){
            edt_phone.setFocusable(true);
        }
        else {
            View view=getActivity().getCurrentFocus();
            if(view==null){
                view=edt_email;
            }
            switch (view.getId()){
                case R.id.edt_create_email:
                    edt_email.setText(voice);
                    edt_password.setFocusable(true);
                    edt_password.requestFocus();
                    break;
                case R.id.edt_create_password:
                    edt_password.setText(voice);
                    edt_nickname.requestFocus();
                    break;
                case R.id.edt_create_nickname:
                    edt_nickname.setText(voice);
                    edt_phone.requestFocus();
                    break;
                case R.id.edt_create_phonenum:
                    edt_phone.setText(voice);
                    break;
            }
        }
    }
}
