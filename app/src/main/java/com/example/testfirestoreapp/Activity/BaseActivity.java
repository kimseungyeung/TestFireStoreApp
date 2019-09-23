package com.example.testfirestoreapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.testfirestoreapp.Constants;

public class BaseActivity extends AppCompatActivity implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constants.active=true;
        return super.onTouchEvent(event);

    }
}
