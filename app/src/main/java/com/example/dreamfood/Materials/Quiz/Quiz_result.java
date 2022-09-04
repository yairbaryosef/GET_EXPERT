package com.example.dreamfood.Materials.Quiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Answers_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.answers_adapter_item;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Quiz_result extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
Button q1,q2,q3,q4,q5,q6,q7,q8;
Spinner spinner;
ArrayAdapter<String> dataAdapter;
ArrayList<Quiz> quizzes;
Student student;
DatabaseReference myRef;
ArrayList<String> jobs;
ListView list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        spinner=(Spinner)findViewById(R.id.spinner);
        list=(ListView)findViewById(R.id.list);
        SharedPreferences sp=getSharedPreferences("email",0);
        String userEmail=sp.getString("email",null);
        myRef = database.getReference("student");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    try {
                        student=snapshot1.getValue(Student.class);
                        if(student.getEmail().equals(userEmail)||snapshot1.getKey().equals(userEmail)){
                            quizzes=student.quizHashtable;
                            break;
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(Quiz_result.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                for(Quiz q:quizzes){
                    jobs.add(q.type+" "+q.grade);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        jobs=new ArrayList<String>();
        jobs.add("choose");


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jobs);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item=parent.getItemAtPosition(position).toString();
        if(!item.equals("choose")) {
            Quiz q =quizzes.get(position-1);
            ArrayList<answers_adapter_item> answers=new ArrayList<>();
            for(OptionsQ optionsQ:q.Oquestions){
                answers.add(new answers_adapter_item(optionsQ.Answer,optionsQ.userAns,optionsQ.Answer.equals(optionsQ.userAns)));
            }
            for(Question optionsQ:q.questions){
                answers.add(new answers_adapter_item(optionsQ.Answer,optionsQ.userAns,optionsQ.Answer.equals(optionsQ.userAns)));
            }
            Answers_Adapter answers_adapter=new Answers_Adapter(this,answers);
            list.setAdapter(answers_adapter);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}