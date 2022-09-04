package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Deal_for_student;
import com.example.dreamfood.BusinessLayer.Classes.grade;

import java.util.ArrayList;

public class Student extends Person {
    public ArrayList<String> fav;
     private String student;
     public ArrayList<Meeting> meetings;
     public ArrayList<grade> grades;
    public ArrayList<Deal_for_student> deal_for_students;
     public ArrayList<Quiz> quizHashtable;
     public ArrayList<Test> tests;
     public Student(String email,String name,String pass,String city,String phone){
         super(0,email,pass,name,city,phone);
         student="student";
         quizHashtable=new ArrayList<Quiz>();
         tests=new ArrayList<Test>();
         grades=new ArrayList<>();
     }
     public Student(){ super();student="student";
         quizHashtable=new ArrayList<Quiz>();
         grades=new ArrayList<>();
         deal_for_students=new ArrayList<>();
         tests=new ArrayList<Test>();
         meetings=new ArrayList<>();
     }
}
