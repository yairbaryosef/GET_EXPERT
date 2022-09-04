package com.example.dreamfood.Student_Controller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Get_Started_Student;
import com.example.dreamfood.Materials.Quiz.Quiz_result;
import com.example.dreamfood.Materials.Test.Test_result;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class scrolling_activity extends AppCompatActivity implements View.OnClickListener {
    Button back,my_details;
    Student student=new Student();
    Strings constants=new Strings();
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolling_student);
        email=getSharedPreferences("email",0).getString("email",null);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.student).child(email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        initWidgets();
    }
    CalendarView calendarView;
    Button getStarted,result;
    private void initWidgets(){
        back=findViewById(R.id.back);
        back.setOnClickListener( this);
        my_details=findViewById(R.id.my_details);
        my_details.setOnClickListener( this);
        result=findViewById(R.id.show_result);
        result.setOnClickListener(this);
        calendarView=findViewById(R.id.calender);
        getStarted=findViewById(R.id.get_Started);
        getStarted.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(scrolling_activity.this, String.valueOf(dayOfMonth), Toast.LENGTH_SHORT).show();
                Date_Dialog(new Date(year,month,dayOfMonth));
            }
        });

    }

   Dialog d;
   Button quiz,test;
   ListView list;
    public void Date_Dialog(Date date){
        d=new Dialog(this);
        d.setContentView(R.layout.videos_list);
        list=d.findViewById(R.id.list);
        ArrayList<String> arrayList=new ArrayList<>();
        for(Meeting m:student.meetings){

           Date date1=new Date(m.startdate.getYear(),m.startdate.getMonth(),m.startdate.getDay());
            if(date1.getTime()==date.getTime()){
                arrayList.add(m.type+" "+m.startdate.toString());
            }
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,arrayList);
        list.setAdapter(arrayAdapter);
        d.show();
    }
   public void ResultDialog(){
       d=new Dialog(this);
       d.setContentView(R.layout.results_student);
       quiz=d.findViewById(R.id.quiz);
       quiz.setOnClickListener(this);
       test=d.findViewById(R.id.test);
       test.setOnClickListener(this);
       d.show();
   }
    @Override
    public void onClick(View v) {
       if(v==my_details){
           Intent intent=new Intent(this, Student_details.class);
           startActivity(intent);
       }
        if(v==test){
            Intent intent=new Intent(this, Test_result.class);
            startActivity(intent);
        }
       if(v==quiz){
           Intent intent=new Intent(this, Quiz_result.class);
           startActivity(intent);
       }
        if(v==result){
            ResultDialog();
        }
        if(v==back){
            finish();
        }
        if(v==getStarted){
            Intent intent=new Intent(this, Get_Started_Student.class);
            startActivity(intent);
        }
    }


}