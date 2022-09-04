package com.example.dreamfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamfood.BusinessLayer.Course;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.Materials.Quiz.Open_Quiz;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class open_course extends AppCompatActivity implements View.OnClickListener {
     EditText subject;
     Button subjects,quiz;
    Quiz quizz;
    Course course=new Course();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_course);
        subject=(EditText) findViewById(R.id.sub);
        subjects=(Button) findViewById(R.id.subject);
        subjects.setOnClickListener(this);
        quiz=(Button)findViewById(R.id.quizz);
        quiz.setOnClickListener(this);
        FileInputStream fis = null;
        try {
            fis = this.openFileInput("quiz");
            ObjectInputStream is = new ObjectInputStream(fis);
           quizz = (Quiz) is.readObject();

            is.close();
            fis.close();
            if(!quizz.type.equals("")){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        if(v==quiz){
            if(subject.length()>0){
                Toast.makeText(this, "add subject", Toast.LENGTH_SHORT).show();
            }
            else{
                SharedPreferences sp=getSharedPreferences("course",0);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("subject",subject.getText().toString());

                editor.commit();
                Intent intent=new Intent(this, Open_Quiz.class);
                startActivity(intent);
            }
        }

    }
}