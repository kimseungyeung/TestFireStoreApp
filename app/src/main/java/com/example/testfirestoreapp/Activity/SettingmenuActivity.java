package com.example.testfirestoreapp.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ExpandableListView;

import com.example.testfirestoreapp.Adapter.SettingMenuAdapter;
import com.example.testfirestoreapp.Data.MenuGroupData;
import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class SettingmenuActivity extends AppCompatActivity {
    ArrayList<MenuGroupData> menuGroupDataArrayList;
    ExpandableListView ebl_menu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingmenuactivity);
        component();
    }

    public void component(){
        ebl_menu=(ExpandableListView)findViewById(R.id.ebl_menu);
        menuGroupDataArrayList= new ArrayList<>();
        MenuGroupData m1=new MenuGroupData("menu1");
        m1.getChild().add("1smenu1");
        m1.getChild().add("1smenu2");
        m1.getChild().add("1smenu3");
        MenuGroupData m2=new MenuGroupData("menu2");
        m2.getChild().add("2smenu1");
        m2.getChild().add("2smenu2");
        m2.getChild().add("2smenu3");
        MenuGroupData m3=new MenuGroupData("menu3");
        m3.getChild().add("3smenu1");
        m3.getChild().add("3smenu2");
        m3.getChild().add("3smenu3");
        MenuGroupData m4=new MenuGroupData("menu4");
        m4.getChild().add("4smenu1");
        m4.getChild().add("4smenu2");
        m4.getChild().add("4smenu3");
        menuGroupDataArrayList.add(m1);
        menuGroupDataArrayList.add(m2);
        menuGroupDataArrayList.add(m3);
        menuGroupDataArrayList.add(m4);
        SettingMenuAdapter smadapter= new SettingMenuAdapter(getApplicationContext(),menuGroupDataArrayList);
        ebl_menu.setAdapter(smadapter);
        smadapter.notifyDataSetChanged();
    }
}