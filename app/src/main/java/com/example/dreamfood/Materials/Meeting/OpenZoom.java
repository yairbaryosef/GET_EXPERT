package com.example.dreamfood.Materials.Meeting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.R;
import com.example.dreamfood.Teacher_Controller.Teacher_home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class OpenZoom extends AppCompatActivity implements View.OnClickListener {
  Dialog d;
  Meeting m;
    EditText updateEDITTEXT,link,subject,time,price,location,limit,hour,minute,id,pass;
    Button updateBUTTON,meet,zoom,description,date,updateDate;
    DatePicker datePicker;
    int Year;
    ArrayList<Meeting> meetings=new ArrayList<Meeting>();
    int month;
    int Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_zoom);
        m=new Meeting();
        id=(EditText)findViewById(R.id.id);
        pass=(EditText)findViewById(R.id.pass);
        link=(EditText)findViewById(R.id.link);
        subject=(EditText)findViewById(R.id.subject);
        time=(EditText)findViewById(R.id.time);
        price=(EditText)findViewById(R.id.price);
        location=(EditText)findViewById(R.id.place);
        limit=(EditText)findViewById(R.id.limit);
        zoom=(Button) findViewById(R.id.zoom);
       zoom.setOnClickListener(this);
        date=(Button) findViewById(R.id.schedule);
      date.setOnClickListener(this);
        description=(Button) findViewById(R.id.description);
        description.setOnClickListener(this);
        meet=(Button) findViewById(R.id.meet);
        meet.setOnClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef =  database.getReference("Meetings");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(meet.equals("true")) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        try {


                            Meeting m = snapshot1.getValue(Meeting.class);

                            meetings.add(m);

                        } catch (Exception e) {
                            Toast.makeText(OpenZoom.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
          }
    public void gotUrl(String s){
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    public void createdialogdetails()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.description);
        d.setCancelable(true);
        updateEDITTEXT=(EditText) d.findViewById(R.id.updescription);
        updateBUTTON=(Button) d.findViewById(R.id.upd);
        updateBUTTON.setOnClickListener(this);
        d.show();

    }
    DatePickerDialog.OnDateSetListener listener;
    public void createdialogDATE()
    {
      /*d= new Dialog(this);
        d.setContentView(R.layout.date);

        datePicker=(DatePicker) d.findViewById(R.id.date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Year=year;
                    month=monthOfYear;
                    Date=dayOfMonth;
                }
            });
        }
        hour=(EditText)d.findViewById(R.id.hour);
        minute=(EditText)d.findViewById(R.id.minute);
        updateDate=(Button)d.findViewById(R.id.saveDate);
        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==updateDate){
                    try {
                        int hour1=Integer.valueOf(hour.getText().toString());
                        int min=Integer.valueOf(minute.getText().toString());
                        m.startdate=new Date(Year,month,Date,hour1,min);
                        d.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(OpenZoom.this, "hour and minute supposed to be a number", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        d.show();*/

        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);
        DatePickerDialog    datePickerDialog=new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,listener
        ,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
        listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePickerDialog.dismiss();
                calendar_meet=Calendar.getInstance();
                TimePickerDialog timePickerDialog=new TimePickerDialog(OpenZoom.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendar_meet.set(year,month,dayOfMonth,hourOfDay,minute);
                        java.util.Date d=calendar.getTime();
                        String dTE= (String) DateFormat.format("hh:mm aa",calendar_meet);

                        }
                },12,0,false);

                timePickerDialog.updateTime(calendar_meet.getTime().getHours(),calendar_meet.getTime().getMinutes());
                timePickerDialog.show();


                   }
        };

    }
   Calendar calendar_meet;
    @Override
    public void onClick(View v) {
        if(v==zoom){
            gotUrl("https://us04web.zoom.us/meeting?_x_zm_rtaid=z9mT2rGaQXe9f7z3ZcwXDg.1658856315535.1f19369aea0160c8a01341b934be0ed5&_x_zm_rhtaid=82#/upcoming");
        }
        if(v==description){
            createdialogdetails();
        }
        if(v==date){
            createdialogDATE();
        }
        if(v==updateBUTTON){
            m.description=updateEDITTEXT.getText().toString();
            d.dismiss();
        }
        if(v==meet){
             try {
                 SharedPreferences sp=getSharedPreferences("email",0);
                 m.email=sp.getString("email",null);
                 m.ID=id.getText().toString();
                 m.number= meetings.size()+1;
                 m.pass=pass.getText().toString();
                 m.link=link.getText().toString();
                 m.price=Double.valueOf(price.getText().toString());
                 m.limitStudents=Integer.valueOf(limit.getText().toString());
                 m.time= Integer.valueOf(time.getText().toString());
                 m.type=subject.getText().toString();
                 m.place=location.getText().toString();
                 m.startdate=calendar_meet.getTime();
                 String form = String.valueOf(DateFormat.format("MM-dd-yyyy", calendar_meet)) + " " + (String) DateFormat.format("hh:mm aa", calendar_meet);
m.format=form;
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference("Meetings");

                 myRef.child(String.valueOf(m.email)).child(m.type).child(String.valueOf(m.startdate.getTime())).setValue(m);
                 Toast.makeText(this, m.type, Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(this, Teacher_home.class);
                 startActivity(intent);
             }
             catch (Exception e){
                 Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
             }
        }
    }
}