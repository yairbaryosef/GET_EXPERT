package com.example.dreamfood.PresentaionLayer.Materials.Quiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Quiz_activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    TextView question,number,answer;
    Button next;
    Student student=new Student();
    String s="";
int i=1,j=1,k=0;
double grade=0.0;
    SharedPreferences sp;
    EditText editText;
    String userEmail;
    ListView listView;
    Quiz quiz=new Quiz();
    String item="",email="",subject="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        editText=findViewById(R.id.editText);
        sp=getSharedPreferences("email",0);
        userEmail=sp.getString("email",null);
        question=(TextView)findViewById(R.id.question);
        answer=(TextView)findViewById(R.id.answer);
        number=(TextView)findViewById(R.id.number);
        next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        email=getIntent().getStringExtra("email");
        subject=getIntent().getStringExtra("subject");
        listView = (ListView) findViewById(R.id.list);

        myRef = database.getReference("Quiz");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if(email.equals(snapshot1.getKey())) {
                            for(DataSnapshot dataSnapshot:snapshot1.getChildren()) {
                               if(dataSnapshot.getKey().equals(subject)){
                                   quiz=dataSnapshot.getValue(Quiz.class);
                                   break;
                               }
                            }
                        }

                    }
                    k=quiz.questions.size()+quiz.Oquestions.size();
                number.setText("1/"+String.valueOf(k));
                ArrayList<OptionsQ>  Oquestions=quiz.Oquestions;

              if(Oquestions.size()>0) {
                  question.setText(Oquestions.get(0).Question);

                  OptionsQ optionsQ = Oquestions.get(0);
                  ArrayList<String> arrayList = new ArrayList<String>();
                  for (int i = 0; i < optionsQ.answers.size(); i++) {
                      arrayList.add(optionsQ.answers.get(i));
                  }
                  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Quiz_activity.this, android.R.layout.simple_list_item_single_choice, arrayList);
                  listView=findViewById(R.id.list);
                  listView.setAdapter(dataAdapter);
                  listView.setOnItemClickListener(Quiz_activity.this);
                  editText.setVisibility(View.INVISIBLE);

              }
              else{
                  Toast.makeText(Quiz_activity.this, String.valueOf(quiz.questions.size()), Toast.LENGTH_SHORT).show();
                ArrayList<Question>  questions=quiz.questions;

                 question.setText(questions.get(0).Question);

              }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef = database.getReference("student");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    try {
                        student=snapshot1.getValue(Student.class);
                        if(student.getEmail().equals(userEmail)||snapshot1.getKey().equals(userEmail)){
                            break;
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(Quiz_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View v) {
if(v==next){
    if(Objects.equals(item, "")&&editText.getVisibility()==View.VISIBLE){
        item=editText.getText().toString();
        answer.setText(item);
    }

    if(answer.getText().toString().equals("answer")) {
        Toast.makeText(this, "choose an option", Toast.LENGTH_SHORT).show();
    }
    else {

        if (i < quiz.Oquestions.size()) {

            try {
                if (answer.getText().toString().equals(quiz.Oquestions.get(i - 1).Answer)) {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    grade = grade + 100.0 / k;
                    Toast.makeText(this, String.valueOf(grade), Toast.LENGTH_SHORT).show();
                    s = s + String.valueOf(i) + String.valueOf(0) + " ";
                } else {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                    s = s + String.valueOf(i) + String.valueOf(1) + " ";
                }
                quiz.Oquestions.get(i - 1).userAns = answer.getText().toString();

                i++;
                number.setText(String.valueOf(i) + "/" + String.valueOf(quiz.questions.size() + quiz.Oquestions.size()));
                ArrayList<OptionsQ> questions = quiz.Oquestions;

                question.setText(questions.get(i - 1).Question);

                OptionsQ optionsQ = questions.get(i - 1);
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int t = 0; t < optionsQ.answers.size(); t++) {
                    arrayList.add(optionsQ.answers.get(t));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Quiz_activity.this, android.R.layout.simple_list_item_single_choice, arrayList);
                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(Quiz_activity.this);
                answer.setText("answer");
                item="";

            }catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if(j<=quiz.questions.size()){
            if (answer.getText().toString().equals(quiz.questions.get(j - 1).Answer)) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                grade = grade + 100.0 / k;
                Toast.makeText(this, String.valueOf(grade), Toast.LENGTH_SHORT).show();
                s = s + String.valueOf(j) + String.valueOf(0) + " ";
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                s = s + String.valueOf(i) + String.valueOf(1) + " ";
            }

            if (j <quiz.questions.size()) {
                quiz.questions.get(j - 1).userAns = answer.getText().toString();
            }
            number.setText(String.valueOf(i+j ) +"/"+ String.valueOf(quiz.questions.size() + quiz.Oquestions.size()));

            question.setText(quiz.questions.get(j-1).Question);
            editText.setText(null);
            j++;
            editText.setHint("answer");
            answer.setText("answer");
            item="";
        }
            else {
                if(editText.getVisibility()==View.INVISIBLE) {
                    if (answer.getText().toString().equals(quiz.Oquestions.get(i - 1).Answer)) {
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        grade = grade + 100.0 / k;
                        Toast.makeText(this, String.valueOf(grade), Toast.LENGTH_SHORT).show();
                        s = s + String.valueOf(i) + String.valueOf(0) + " ";
                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                        s = s + String.valueOf(i) + String.valueOf(1) + " ";
                    }

                    quiz.Oquestions.get(i - 1).userAns = answer.getText().toString();
                }
                else{
                    if (answer.getText().toString().equals(quiz.questions.get(j - 1).Answer)) {
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        grade = grade + 100.0 / k;
                        Toast.makeText(this, String.valueOf(grade), Toast.LENGTH_SHORT).show();
                        s = s + String.valueOf(i) + String.valueOf(0) + " ";
                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                        s = s + String.valueOf(i) + String.valueOf(1) + " ";
                    }

                    quiz.questions.get(j - 1).userAns = answer.getText().toString();
                }

                quiz.grade=grade;
                student.quizHashtable.add(quiz);
                myRef = database.getReference("student").child(userEmail);
                myRef.setValue(student);


            }
        }
    }

}


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item=parent.getItemAtPosition(position).toString();
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        answer.setText(item);
    }
}