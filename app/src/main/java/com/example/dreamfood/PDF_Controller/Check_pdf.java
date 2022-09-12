package com.example.dreamfood.PDF_Controller;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Check_pdf extends AppCompatActivity implements View.OnClickListener {
    PDFView pdf;
    Dialog d;
    EditText getGrade,comments;
    Button back,check,upload,save;
   String title="",url="",email="",filename="",student1="",subject="";
   Student student;
   Teacher teacher;
Strings constant=new Strings();
String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pdf);
        back=findViewById(R.id.back);
        subject=getIntent().getStringExtra("subject");
filename=getIntent().getStringExtra("filename");
        student1=getIntent().getStringExtra("student");
        back.setOnClickListener(this);
        check=findViewById(R.id.check);
        check.setOnClickListener(this);
        try {
            email=getIntent().getStringExtra("student");
            title=getIntent().getStringExtra("title");
            url=getIntent().getStringExtra("url");

        }
        catch (Exception e){

        }
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(constant.student);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    student=dataSnapshot.getValue(Student.class);
                    if(dataSnapshot.getKey().equals(email)){
                        key=dataSnapshot.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       reference= FirebaseDatabase.getInstance().getReference(constant.teacher);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sp=getSharedPreferences("email",0);
                String t=sp.getString("email",null);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    teacher=dataSnapshot.getValue(Teacher.class);
                    if(dataSnapshot.getKey().equals(t)||teacher.getEmail().equals(t)){
                       break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pdf=(PDFView) findViewById(R.id.pdfView);
        String url=getIntent().getStringExtra("url");
        new Retrieve().execute(url);
    }

    @Override
    public void onClick(View v) {
        if(v==back){
            finish();
        }
        if(v==upload){
            Intent intent=new Intent(this, add_pdf.class);
            intent.putExtra("email","checking");
            intent.putExtra("student",email);
            startActivity(intent);
        }
        if(v==save){
            grade grade=new grade();
            grade.comments=comments.getText().toString();
            try {

                grade.subject=subject;
                if (!title.equals("")) {
                    grade.title = title;
                }
                if (!url.equals("")) {
                    grade.url = url;
                }
            }
            catch (Exception e){

            }
            try {
                grade.grade = Double.valueOf(getGrade.getText().toString());
                SharedPreferences sp=getSharedPreferences("email",0);
                grade.teacher=sp.getString("email",null);
                if(student.grades==null){
                    student.grades=new ArrayList<>();
                }
                student.grades.add(grade);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference(constant.student);
                reference.child(email).setValue(student);
                reference=FirebaseDatabase.getInstance().getReference(constant.teacher);
                ArrayList<fileinfomodel> fileinfomodels=teacher.tests;
                for(fileinfomodel f:fileinfomodels ){
                    if(f.filename.equals(filename)&&f.teacherEmail.equals(student1)){
                        fileinfomodels.remove(f);
                        break;
                    }
                }
                reference.child(constant.emailStart(grade.teacher)).setValue(teacher);
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if(v==check){
            createdialogdetails();
        }

    }
    public void createdialogdetails()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.grade_file);
        d.setCancelable(true);
        getGrade=(EditText) d.findViewById(R.id.grade);
        comments=(EditText) d.findViewById(R.id.comments);
        save=(Button) d.findViewById(R.id.save);
        save.setOnClickListener(this);
        upload=(Button) d.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        d.show();

    }

    class Retrieve extends AsyncTask<String,Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (Exception e){
                return  null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).load();
        }
    }
}