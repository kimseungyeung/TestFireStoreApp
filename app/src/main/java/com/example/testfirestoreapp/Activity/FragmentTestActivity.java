package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.example.testfirestoreapp.Fragment.Fragment1;
import com.example.testfirestoreapp.Fragment.Fragment2;
import com.example.testfirestoreapp.Fragment.Fragment3;
import com.example.testfirestoreapp.R;

public class FragmentTestActivity extends FragmentActivity implements View.OnClickListener {
    FragmentManager fm=null;
    FragmentTransaction fs;
    Button btn_1,btn_2,btn_3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenttest);
        component();
    }
    public void component(){
        fm=getSupportFragmentManager();
       fs= fm.beginTransaction().addToBackStack("f1");
        Fragment1 f1=new Fragment1();

        Fragment3 f3=new Fragment3();
        fs.add(R.id.frame, f1);
        fs.commit();
        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                break;
            case R.id.btn_2:
                Fragment2 f2=new Fragment2();
                FragmentTransaction fs2=fm.beginTransaction().addToBackStack("f2");
                fs2.add(R.id.frame,f2).commit();
                break;
            case R.id.btn_3:
                Fragment3 f3=new Fragment3();
                FragmentTransaction fs3=fm.beginTransaction().addToBackStack("f3");
                fs3.add(R.id.frame,f3).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //
        if(fm.getBackStackEntryCount()-1>0) {
            if(fm.getFragments().get(fm.getBackStackEntryCount()-1)instanceof Fragment1) {
                fm.popBackStack();
            }
        }else{
            finish();
        }
    }
}