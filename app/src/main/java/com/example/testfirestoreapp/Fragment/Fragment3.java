package com.example.testfirestoreapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class Fragment3 extends Fragment {
    EditText edt_hobby,edt_kg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.insert_fragment3,null);
        component(view);
        return view;
    }
    public void component(View v){
        edt_hobby =(EditText)v.findViewById(R.id.edt_create_hobby);
        edt_kg =(EditText)v.findViewById(R.id.edt_create_kg);



    }
    public ArrayList<String> getresult(){
        String hobby =edt_hobby.getText().toString();
        String kg =edt_kg.getText().toString();
        ArrayList<String> r =new ArrayList<>();
        r.add(hobby);
        r.add(kg);
        return r;
    }
    public boolean gettest(){
        String hobby =edt_hobby.getText().toString();
        String kg =edt_kg.getText().toString();
        if(!hobby.equals("")&&!kg.equals("")){
            return true;
        }
        else{
            return false;
        }
    }
}
