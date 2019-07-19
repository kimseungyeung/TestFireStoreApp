package com.example.testfirestoreapp.Data;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MenuGroupData {
   private ArrayList<String> child;
   private String GroupName;
   public MenuGroupData(String ngroup){
        this.GroupName=ngroup;
        child=new ArrayList<>();
    }
    public void addchild(String childname){
       this.child.add(childname);
    }
}
