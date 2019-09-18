package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testfirestoreapp.CustomDialog;
import com.example.testfirestoreapp.Fragment.CalendarFragment;
import com.example.testfirestoreapp.Fragment.Fragment1;
import com.example.testfirestoreapp.Fragment.Fragment2;
import com.example.testfirestoreapp.Fragment.Fragment3;
import com.example.testfirestoreapp.R;

public class FragmentTestActivity extends FragmentActivity implements View.OnClickListener {
    FragmentManager fm=null;
    FragmentTransaction fs;
    Button btn_1,btn_2,btn_3,btn_4;
    String result="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenttest);
        component();
    }
    public void component(){
        fm=getSupportFragmentManager();
       fs= fm.beginTransaction().addToBackStack("f1");

        CalendarFragment cf = new CalendarFragment();
        Fragment3 f3=new Fragment3();
        fs.add(R.id.frame, cf);
        fs.commit();
        btn_1=(Button)findViewById(R.id.btn_1);
        btn_2=(Button)findViewById(R.id.btn_2);
        btn_3=(Button)findViewById(R.id.btn_3);
        btn_4=(Button)findViewById(R.id.btn_4);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                Fragment1 f1=new Fragment1();
                FragmentTransaction fs1=fm.beginTransaction().addToBackStack(null);
                fs1.replace(R.id.frame,f1).commit();
                break;
            case R.id.btn_2:
                Fragment2 f2=new Fragment2();
                FragmentTransaction fs2=fm.beginTransaction().addToBackStack(null);
                fs2.replace(R.id.frame,f2).commit();
                break;
            case R.id.btn_3:
                Fragment3 f3=new Fragment3();
                FragmentTransaction fs3=fm.beginTransaction().addToBackStack(null);
                fs3.replace(R.id.frame,f3).commit();
                break;
            case R.id.btn_4:
//                CustomDialog cd =new CustomDialog(R.layout.customdialog2,this,"2");
//                cd.show();
                Toast.makeText(this,result,Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //
        if(fm.getBackStackEntryCount()-1>0) {
//            if(fm.getFragments().get(fm.getBackStackEntryCount()-1)instanceof Fragment1) {
                fm.popBackStack();
//            }
        }else{
            finish();
        }
    }
    public void setdd(String dd){
        result=dd;
    }

}
