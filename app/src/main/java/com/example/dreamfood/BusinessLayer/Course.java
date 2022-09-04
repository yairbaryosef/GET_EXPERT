package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Mission;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Course {
    public String subject;
    public long id;
    public ArrayList<String> SubThemes;
    public ArrayList<Test> tests;
    public ArrayList<QestionsAndAnswers> QA;
    public ArrayList<Student> students;
    public ArrayList<Teacher> teachers;
    public ArrayList<Mission> missions;
    public ArrayList<Quiz> quizzes;
    public Course(){
        SubThemes=new ArrayList<String>();
        tests=new ArrayList<Test>();
        QA=new ArrayList<QestionsAndAnswers>();
        students=new ArrayList<Student>();
        teachers=new ArrayList<Teacher>();
        missions=new ArrayList<Mission>();
        quizzes=new ArrayList<Quiz>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("counter courses");
        //id=counter courses+1
    }
}
