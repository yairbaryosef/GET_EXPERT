package com.example.dreamfood.PresentaionLayer.Extend_Fitchers.About_US;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.MainActivity;
import com.example.dreamfood.R;

public class About_Us_Teacher_Version extends AppCompatActivity implements View.OnClickListener {
    Button start,student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_teacher_version);
        start=findViewById(R.id.get_Started);
        start.setOnClickListener(this);
        student=findViewById(R.id.student);
        student.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==start){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(v==student){
            Intent intent=new Intent(this, About_us.class);
            startActivity(intent);
        }
    }
}