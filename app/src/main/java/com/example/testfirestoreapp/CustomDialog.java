package com.example.testfirestoreapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog extends Dialog implements View.OnClickListener {
    int vid;
    View view;
    LayoutInflater layoutInflater;
    Button btn_ok,btn_cancel;
    EditText editText;
    String Tag;
    public CustomDialog(int id,Context context,String T) {
        super(context);
        vid=id;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(id,null);
        Tag=T;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = this.getWindow();
        window.setAttributes(lp);
        component();
    }
    public void component(){
        switch (vid){
            case R.layout.customdialog1:
                btn_ok=(Button)findViewById(R.id.btn_ok);
                btn_cancel=(Button)findViewById(R.id.btn_cancle);
                btn_ok.setOnClickListener(this);
                btn_cancel.setOnClickListener(this);
                break;
            case R.layout.customdialog2:
                editText= (EditText)findViewById(R.id.edt_1);
                btn_ok=(Button)findViewById(R.id.btn_ok);
                btn_cancel=(Button)findViewById(R.id.btn_cancle);
                btn_ok.setOnClickListener(this);
                btn_cancel.setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                if(Tag.equals("1")) {
                    Toast.makeText(getContext(), "오케이", Toast.LENGTH_LONG).show();
                }else if(Tag.equals("2")){
                    Toast.makeText(getContext(), editText.getText().toString()+"입니다.", Toast.LENGTH_LONG).show();
                }
                dismiss();
                break;
            case R.id.btn_cancle:
                Toast.makeText(getContext(),"취소",Toast.LENGTH_LONG).show();
                dismiss();
                break;
        }
    }
}
