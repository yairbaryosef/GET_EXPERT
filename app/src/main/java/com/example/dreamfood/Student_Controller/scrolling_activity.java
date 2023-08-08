package com.example.dreamfood.Student_Controller;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dreamfood.BusinessLayer.Classes.Meeting_Adpter.Meetings_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Profile.Pick_A_Teacher;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Fragment_Activity_default;
import com.example.dreamfood.Get_Started_Student;
import com.example.dreamfood.List_Activity_With_Search;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.OpenChat;
import com.example.dreamfood.PresentaionLayer.Materials.Quiz.Quiz_result;
import com.example.dreamfood.PresentaionLayer.Materials.Test.Test_result;
import com.example.dreamfood.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class scrolling_activity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Button back,my_details,rate,follow;
    Student student=new Student();
    Strings constants=new Strings();
    String email;
    NavigationView navigationView;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolling_student);


        drawer = findViewById(R.id.drawer_layout);
     navigationView=findViewById(R.id.menu);
     navigationView.setItemIconTintList(null);
     navigationView.setNavigationItemSelectedListener(this);

       email=getSharedPreferences("email",0).getString("email",null);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.student).child(email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);
                Gson gson=new Gson();
                String st_json=gson.toJson(student);
                SharedPreferences sp=getSharedPreferences(constants.student,0);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString(constants.student,st_json);
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initWidgets();
    }


    CalendarView calendarView;
    Button getStarted,result;
    /*
    init views
     */
    private void initWidgets(){
        follow=findViewById(R.id.follow);
        follow.setOnClickListener(this);
        back=findViewById(R.id.back);
        back.setOnClickListener( this);

        result=findViewById(R.id.show_result);
        result.setOnClickListener(this);
        calendarView=findViewById(R.id.calender);
        getStarted=findViewById(R.id.get_Started);
        getStarted.setOnClickListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(scrolling_activity.this, String.valueOf(dayOfMonth), Toast.LENGTH_SHORT).show();
                year=year-(3922-2022);
               String date=String.valueOf( DateFormat.format("MM-dd-yyyy",new Date(year,month,dayOfMonth)));
                Toast.makeText(scrolling_activity.this, date, Toast.LENGTH_SHORT).show();
                Date_Dialog(date);
            }
        });

    }

   Dialog d;
   Button quiz,test;
   ListView list;
   /*
   show meetings for a day
    */
    public void Date_Dialog(String day){
        d=new Dialog(this);
        d.setContentView(R.layout.videos_list);
        list=d.findViewById(R.id.list);
        ArrayList<Meeting> arrayList=new ArrayList<>();

        for(Meeting m:student.meetings){
            try {

             String check_date=    String.valueOf( DateFormat.format("MM-dd-yyyy",m.startdate));

                if(day.equals(check_date)) {
                    arrayList.add(m);
                }

            }
            catch (Exception e){

            }
        }
        Meetings_Adapter arrayAdapter=new Meetings_Adapter(this, arrayList,0);
        list.setAdapter(arrayAdapter);
        d.show();
    }
    /*
    show result dialog
     */
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
        if(v==follow){
            Intent intent=new Intent(this, Pick_A_Teacher.class);
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
           drawer.openDrawer(GravityCompat.START);
        }
        if(v==getStarted){
            Intent intent=new Intent(this, Get_Started_Student.class);
            startActivity(intent);
        }

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().toString().equals("rating")){
            Intent intent=new Intent(this, Fragment_Activity_default.class);

            intent.putExtra("type","rating name");
            startActivity(intent);
        }
       else if(item.getTitle().toString().equals("chat")){
            Intent intent=new Intent(this, OpenChat.class);
            intent.putExtra("job","teacher");
            Gson gson=new Gson();
            intent.putExtra(constants.student,gson.toJson(student));
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals("details")){
            Intent intent=new Intent(this, Student_details.class);
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals("get coin")){
            Intent intent=new Intent(this, List_Activity_With_Search.class);
            intent.putExtra("item",constants.coin);
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals("test")){
            Intent intent=new Intent(this, Test_result.class);
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals("quiz")){
            Intent intent=new Intent(this, Quiz_result.class);
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals("follow")){
            Intent intent=new Intent(this, Pick_A_Teacher.class);
            startActivity(intent);
        }

        return false;
    }
}