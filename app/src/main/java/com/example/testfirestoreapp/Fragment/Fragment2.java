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

public class Fragment2 extends Fragment {
    EditText edt_sex,edt_address,edt_name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.insert_fragment2,null);
        component(view);
        return view;
    }
    public void component(View v){
        edt_sex =(EditText)v.findViewById(R.id.edt_create_sex);
        edt_address =(EditText)v.findViewById(R.id.edt_create_address);
        edt_name =(EditText)v.findViewById(R.id.edt_create_name);



    }
    public ArrayList<String> getresult(){
        String sex =edt_sex.getText().toString();
        String address =edt_address.getText().toString();
        String name =edt_name.getText().toString();
        ArrayList<String> r =new ArrayList<>();
        r.add(sex);
        r.add(address);
        r.add(name);
        return r;
    }
    public boolean gettest(){
        String sex =edt_sex.getText().toString();
        String address =edt_address.getText().toString();
        String name =edt_name.getText().toString();
        if(!sex.equals("")&&!address.equals("")&&!name.equals("")){
            return true;
        }
        else{
            return false;
        }
    }
}
