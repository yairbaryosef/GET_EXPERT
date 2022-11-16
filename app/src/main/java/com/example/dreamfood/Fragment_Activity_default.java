package com.example.dreamfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.Fragments.List_Video_Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Fragment_Activity_default extends AppCompatActivity {
    FrameLayout frameLayout;
    String type;
    Student student=new Student();
    Strings constants=new Strings();
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_default);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        SharedPreferences sp=getSharedPreferences(constants.student,0);
        String json=sp.getString(constants.student,null);
        student=gson.fromJson(json,Student.class);
        type= getIntent().getStringExtra("type");
        if(type.equals("rating name")){


            Fragment fragment= new List_Video_Fragment(student.ratings,0);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

        }
        else if(type.equals("rating active")){
            String r=getIntent().getStringExtra("Rating");
            Rating rating=gson.fromJson(r,Rating.class);
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.teacher).child(rating.teacher_email);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Teacher teacher=new Teacher();
                    teacher=snapshot.getValue(Teacher.class);
                    Fragment fragment= new List_Video_Fragment(rating,teacher);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }



    }

}