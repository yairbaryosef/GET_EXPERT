package com.example.dreamfood.About_US;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.MainActivity;
import com.example.dreamfood.R;

public class About_us extends AppCompatActivity implements View.OnClickListener {
    Button start,teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        start=findViewById(R.id.get_Started);
        start.setOnClickListener(this);
        teacher=findViewById(R.id.teacher);
        teacher.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==start){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(v==teacher){
            Intent intent=new Intent(this, About_Us_Teacher_Version.class);
            startActivity(intent);
        }
    }
}