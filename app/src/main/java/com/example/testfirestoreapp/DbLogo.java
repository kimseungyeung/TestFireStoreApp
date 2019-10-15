package com.example.testfirestoreapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.jar.Attributes;

public class DbLogo extends RelativeLayout {
    RelativeLayout main;
    ImageView iv_blue,iv_green,iv_red;
    int logowidth;
    int logoheight;
    int bg;
    int speed;
    float scale;

    public DbLogo(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        getAttrs(attributeSet);
        initview(context);
    }
    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs,R.styleable.DbLogo);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typeArray){
        logowidth=typeArray.getInteger(R.styleable.DbLogo_lwidth,100);
        logoheight=typeArray.getInteger(R.styleable.DbLogo_lheight,100);
        bg = typeArray.getInteger(R.styleable.DbLogo_backgroundColor,0);
        speed = typeArray.getInteger(R.styleable.DbLogo_speed,0);
        scale= getResources().getDisplayMetrics().density;

    }
    public void initview(Context ctx){
        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.logo_view,null);
        addView(view);
        int w=(int)(logowidth*scale);
        int h=(int)(logoheight*scale);
        main=this;
        iv_blue=(ImageView)findViewById(R.id.iv_blue);
        iv_green=(ImageView)findViewById(R.id.iv_green);
        iv_red=(ImageView)findViewById(R.id.iv_red);
        iv_blue.getLayoutParams().width=w;
        iv_blue.getLayoutParams().height=h;
        iv_green.getLayoutParams().width=w;
        iv_green.getLayoutParams().height=h;
        iv_red.getLayoutParams().width=w;
        iv_red.getLayoutParams().height=h;
        Animation bluetrans= AnimationUtils.loadAnimation(ctx,R.anim.center_move_bottom);
        Animation redtrans= AnimationUtils.loadAnimation(ctx,R.anim.center_move_right);
        Animation greentrans= AnimationUtils.loadAnimation(ctx,R.anim.center_move_left);

        iv_blue.startAnimation(bluetrans);
        iv_green.startAnimation(greentrans);
        iv_red.startAnimation(redtrans);
    }

}
