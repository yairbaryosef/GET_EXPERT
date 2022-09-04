package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;

import java.util.ArrayList;

public class    Quiz {
    public String pass;
    public ArrayList<String> teachers;
    public ArrayList<String> students;
    public String description;
    public String type;
    public double price;
    public double grade;
    public ArrayList<Question> questions;
    public ArrayList<OptionsQ> Oquestions;

    public String email;
    public Quiz(){
      questions=new ArrayList<Question>();
        Oquestions=new ArrayList<OptionsQ>();
    }
    Strings con=new Strings();

}
