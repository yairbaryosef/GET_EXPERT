package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Deal;
import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Classes.sub;

import java.util.ArrayList;

public class Teacher extends Person{
    public String fav;
    public String profile_url;
    private String student;
    public String favoriteSub;
    public ArrayList<String> Subscribers;
    public ArrayList<Deal> deals;
    public ArrayList<fileinfomodel> tests;
    public ArrayList<sub> subs;
    public Teacher(String email,String name,String pass,String city,String phone){
        super(0,email,pass,name,city,phone);
        student="teacher";
    }
    public Teacher(){ super();
    tests=new ArrayList<fileinfomodel>();
    deals=new ArrayList<>();
    subs=new ArrayList<>();
    }
}
