package com.example.testfirestoreapp.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.testfirestoreapp.Data.MenuGroupData;
import com.example.testfirestoreapp.R;

import java.util.ArrayList;

public class SettingMenuAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<MenuGroupData>menugroup;
    public  SettingMenuAdapter(Context ctx,ArrayList<MenuGroupData>mgroup){
        this.context=ctx;
        this.menugroup=mgroup;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=linf.inflate(R.layout.item_expendable_group,parent,false);
        }
        TextView tv_child=(TextView)convertView.findViewById(R.id.tv_group);
        tv_child.setText(menugroup.get(groupPosition).getGroupName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=linf.inflate(R.layout.item_expendable_child,parent,false);
        }
        TextView tv_child=(TextView)convertView.findViewById(R.id.tv_child);
        tv_child.setText(menugroup.get(groupPosition).getChild().get(childPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menugroup.get(groupPosition).getChild().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return menugroup.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menugroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menugroup.get(groupPosition).getChild().get(childPosition);
    }
}
