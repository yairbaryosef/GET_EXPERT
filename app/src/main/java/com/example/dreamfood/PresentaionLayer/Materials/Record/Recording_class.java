package com.example.dreamfood.PresentaionLayer.Materials.Record;

import com.example.dreamfood.BusinessLayer.Classes.Educational_content;
import com.example.dreamfood.BusinessLayer.Classes.video;

import java.util.ArrayList;

public class Recording_class extends Educational_content {
    public ArrayList<String> videos;
    public double price;
    public String description;
    public ArrayList<video> videoArrayList;
    public String subject;
    public String email;

    public Recording_class(){
        videos=new ArrayList<>();
        subject="";
        videoArrayList=new ArrayList<>();
    }
}
