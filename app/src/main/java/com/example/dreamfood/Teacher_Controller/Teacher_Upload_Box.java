package com.example.dreamfood.Teacher_Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PDF_Controller.Check_pdf;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class Teacher_Upload_Box extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Teacher teacher;
    ArrayAdapter<String> dataAdapter;
    String userEmail;
    Strings constants=new Strings();
    ArrayList<String> arrayList=new ArrayList<>();
    Hashtable<String, fileinfomodel> files=new Hashtable<String,fileinfomodel>();
    Hashtable<String,ArrayList<fileinfomodel>> tests=new Hashtable<String,ArrayList<fileinfomodel>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_upload_box);
        spinner=(Spinner)findViewById(R.id.spinner);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("teacher");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sp=getSharedPreferences("email",0);
                userEmail=sp.getString("email",null);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    teacher=dataSnapshot.getValue(Teacher.class);
                    if(teacher.getEmail().equals(userEmail)||dataSnapshot.getKey().equals(userEmail)){


                        arrayList.add("show answers");
                        for(fileinfomodel f:teacher.tests){
                            arrayList.add(f.filename+" "+f.teacherEmail);
                            files.put(f.filename+" "+f.teacherEmail,f);
                        }
                        dataAdapter = new ArrayAdapter<String>(Teacher_Upload_Box.this, android.R.layout.select_dialog_multichoice, arrayList);
                        spinner.setAdapter(dataAdapter);
                        spinner.setOnItemSelectedListener(Teacher_Upload_Box.this);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item=parent.getItemAtPosition(position).toString();
        if(!item.equals("show answers")) {
            Intent intent = new Intent(this, Check_pdf.class);
            fileinfomodel f = files.get(item);
            intent.putExtra("url", f.fileurl);
            intent.putExtra("filename",f.filename);
            intent.putExtra("student",f.teacherEmail);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}