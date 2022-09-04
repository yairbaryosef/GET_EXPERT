package com.example.dreamfood.BusinessLayer.Classes;

import java.util.ArrayList;

public class Deal {
    public String name;
    public String des;
    public String email;
    public double cost;
    public String cost_Time_type;
    public ArrayList<String> quizzes_for_Subscribers;
    public ArrayList<String> tests_for_Subscribers;


    public Deal(){
         quizzes_for_Subscribers=new ArrayList<>();
         tests_for_Subscribers=new ArrayList<>();

    }
}
