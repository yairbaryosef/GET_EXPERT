package com.example.dreamfood.Materials.Record;

import com.example.dreamfood.BusinessLayer.Classes.video;

import java.util.ArrayList;

public class Recording_class {
    public ArrayList<String> videos;
    public double price;
    public ArrayList<video> videoArrayList;
    public String subject;

    public Recording_class(){
        videos=new ArrayList<>();
        subject="";
        videoArrayList=new ArrayList<>();
    }
}
