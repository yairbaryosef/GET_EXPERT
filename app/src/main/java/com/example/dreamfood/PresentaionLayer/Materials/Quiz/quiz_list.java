package com.example.dreamfood.PresentaionLayer.Materials.Quiz;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Quiz_Adapter.Quiz_Q_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Quiz_Adapter.question_info_for_adapter;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class quiz_list extends AppCompatActivity implements View.OnClickListener {
    String item="",email="",subject="";
    Strings constants=new Strings();
    ArrayList<question_info_for_adapter> questions;
    Quiz quiz;
    Button submit,version;
    ListView list;
    int counter=0;
    Student student=new Student();
    Dialog d;
    public void Fun_Dialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.dialog_choose_version);
        Button button=d.findViewById(R.id.fun);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==button){
                    Gson gson=new Gson();
                    String json=gson.toJson(quiz);
                    String student_json=gson.toJson(student);
                    Intent intent=new Intent(quiz_list.this,Fun_Quiz_Activity.class);
                    intent.putExtra("quiz",json);

                    intent.putExtra("student",student_json);
                    startActivity(intent);
                }
            }
        });
        d.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
       initWidgets();

     DatabaseReference reference=FirebaseDatabase.getInstance().getReference(constants.student).child(user);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.Quiz).child(email).child(subject);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quiz=snapshot.getValue(Quiz.class);
                int i=0;
                for(OptionsQ optionsQ:quiz.Oquestions){
                    try{
                        Uri uri=Uri.parse(optionsQ.getImage_url());
                        questions.add(new question_info_for_adapter(optionsQ.Question,optionsQ.Answer,optionsQ.answers,optionsQ.getImage_url(),"option",i));

                    }
                    catch (Exception e){
                        questions.add(new question_info_for_adapter(optionsQ.Question,optionsQ.Answer,optionsQ.answers,"","option",i));

                    }
                     i++;
                }
                int j=0;
                for(Question optionsQ:quiz.questions){
                    try {
                        Uri uri=Uri.parse(optionsQ.getImage_url());
                          if(optionsQ.getImage_url().equals("")){
                              questions.add(new question_info_for_adapter(optionsQ.Question, optionsQ.Answer, new ArrayList<>(), "", "open", j));

                          }
                          else{
                              questions.add(new question_info_for_adapter(optionsQ.Question, optionsQ.Answer, new ArrayList<>(), optionsQ.getImage_url(), "open", j));

                          }


                        }
                    catch (Exception e){
                        questions.add(new question_info_for_adapter(optionsQ.Question, optionsQ.Answer, new ArrayList<>(), "", "open", j));

                    }
                    j++;
                }
                if(quiz.Oquestions.size()>0) {
                    Fun_Dialog();
                }
                Quiz_Q_Adapter quiz_q_adapter=new Quiz_Q_Adapter(quiz_list.this,questions,quiz);
                list.setAdapter(quiz_q_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void initWidgets(){
        email=getIntent().getStringExtra("email");
        subject=getIntent().getStringExtra("subject");
        submit=findViewById(R.id.submit);
        user=getSharedPreferences("email",0).getString("email",null);
        version=findViewById(R.id.version);
        submit.setOnClickListener(this);
        version.setOnClickListener(this);
        list=findViewById(R.id.list);
        questions=new ArrayList<>();
    }
    String user;

    @Override
    public void onClick(View v) {
        if(v==version){
            Gson gson=new Gson();
            String json=gson.toJson(quiz);
            String student_json=gson.toJson(student);
            Intent intent=new Intent(this,Quiz_One_Question_View.class);
            intent.putExtra("quiz",json);

            intent.putExtra("student",student_json);
            startActivity(intent);
        }
         if(v==submit){
             int questions_number=questions.size();
             int grade=100;
             for(int i=0;i<quiz.questions.size();i++){
                 if(!quiz.questions.get(i).userAns.equals(quiz.questions.get(i).Answer)){
                     grade=grade-100/questions_number;
                 }
             }
             for(int i=0;i<quiz.Oquestions.size();i++){
                 if(!quiz.Oquestions.get(i).userAns.equals(quiz.Oquestions.get(i).Answer)){
                     grade=grade-100/questions_number;
                 }
             }
             quiz.grade=grade;
             student.quizHashtable.add(quiz);
             FirebaseDatabase.getInstance().getReference(constants.student).child(user).setValue(student);
             Toast.makeText(this, "quiz successfully upload. ", Toast.LENGTH_LONG).show();
             finish();
         }
    }
}