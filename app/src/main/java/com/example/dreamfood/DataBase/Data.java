package com.example.dreamfood.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class Data {
    public DatabaseReference data;
    public StorageReference storage;
    Strings constants=new Strings();
    Gson gson=new Gson();
    public Data(String pathData){
        data= FirebaseDatabase.getInstance().getReference(pathData);
    }
    public Data(Student student){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.student).child(constants.emailStart(student.getEmail()));
        databaseReference.setValue(student);
    }
    public Data(Teacher teacher){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.student).child(constants.emailStart(teacher.getEmail()));
        databaseReference.setValue(teacher);
    }
    public Data(Quiz quiz){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.student).child(constants.emailStart(quiz.email)).child(quiz.type);
        databaseReference.setValue(quiz);
    }
    public void Serialize_Object(Context context,Student student){
        SharedPreferences sp=context.getSharedPreferences(constants.student,0);
        SharedPreferences.Editor editor=sp.edit();
        String json=gson.toJson(student);
        editor.putString(constants.student,json);
        editor.commit();

    }
    public Student DeSerialize_Object(Context context){
        SharedPreferences sp=context.getSharedPreferences(constants.student,0);

        String json= sp.getString(constants.student,null);
        Student student=new Student();
        student=gson.fromJson(json,Student.class);
        return student;


    }


}
