package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.testfirestoreapp.Constants;
import com.example.testfirestoreapp.R;

public class ScreenSaverActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardactiity);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//기본 잠금화면보다 이걸먼저 띄우도록 함

        btn_close=(Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close:
                finish();
                Constants.checklock=false;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
