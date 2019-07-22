package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testfirestoreapp.R;

public class InsertTestActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    Spinner spin_1,spin_2,spin_3;
    TextView tv_result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserttestactivity);
        component();
    }
    public void component(){
        spin_1=(Spinner)findViewById(R.id.spin_1);
        spin_2=(Spinner)findViewById(R.id.spin_2);
        spin_3=(Spinner)findViewById(R.id.spin_3);
        tv_result=(TextView)findViewById(R.id.tv_result);
        spin_1.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.spin_1:
                tv_result.setText(spin_1.getSelectedItem().toString());
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
