package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.BusinessLayer.Classes.Deal;
import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating;
import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Classes.sub;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.Profile_Adapter;

import java.util.ArrayList;

public class Teacher extends Person{
    public String fav;
    public int followers;
    public String profile_url;
    private String student;
    public String favoriteSub;
    public ArrayList<String> Subscribers;
    public ArrayList<String> followers_names;
    public ArrayList<Rating> ratings;
    public ArrayList<Deal> deals;

    public ArrayList<Profile_Adapter.profile> messages;
    public ArrayList<fileinfomodel> tests;
    public ArrayList<Summary> summaries;
    public ArrayList<Coin> coins;
    public ArrayList<sub> subs;
    public Teacher(String email,String name,String pass,String city,String phone){
        super(0,email,pass,name,city,phone);
        student="teacher";
    }
    public Teacher(){ super();
        followers=0;
        coins=new ArrayList<>();
        summaries=new ArrayList<>();
        followers_names=new ArrayList<>();
    tests=new ArrayList<fileinfomodel>();
    deals=new ArrayList<>();
    subs=new ArrayList<>();
    messages=new ArrayList<>();
    ratings=new ArrayList<>();
    Subscribers=new ArrayList<>();
    }
}
