package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.testfirestoreapp.R;

public class InsertTestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Spinner spin_1,spin_2,spin_3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void component(){
        spin_1=(Spinner)findViewById(R.id.spin_1);
        spin_2=(Spinner)findViewById(R.id.spin_2);
        spin_3=(Spinner)findViewById(R.id.spin_3);
        spin_1.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
