package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.BusinessLayer.Classes.Deal_for_student;
import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating;
import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Recording_class;

import java.util.ArrayList;

public class Student extends Person {
    public ArrayList<String> fav;
     private String student;
     public ArrayList<Meeting> meetings;
     public ArrayList<grade> grades;
     public ArrayList<Rating> ratings;
     public ArrayList<Summary> summaries;
     public ArrayList<Recording_class> recordings;
     public ArrayList<String> following;
    public ArrayList<Deal_for_student> deal_for_students;
     public ArrayList<Quiz> quizHashtable;
     public ArrayList<Test> tests;
     public ArrayList<Coin> coins;
     public ArrayList<String> teachers_send_username;
     public ArrayList<Integer> teacher_send_count;
     public int sent;
     public Student(String email,String name,String pass,String city,String phone){
         super(0,email,pass,name,city,phone);
         student="student";
         sent=0;
         teacher_send_count=new ArrayList<>();
         teachers_send_username=new ArrayList<>();
         quizHashtable=new ArrayList<Quiz>();
         tests=new ArrayList<Test>();
         grades=new ArrayList<>();
     }
     public Student(){ super();student="student";
         quizHashtable=new ArrayList<Quiz>();
         following=new ArrayList<>();
         grades=new ArrayList<>();
         coins=new ArrayList<>();
         deal_for_students=new ArrayList<>();
         tests=new ArrayList<Test>();
         meetings=new ArrayList<>();
         summaries=new ArrayList<>();
         recordings=new ArrayList<>();
         ratings=new ArrayList<>();
     }
}
