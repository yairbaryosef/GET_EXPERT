package com.example.dreamfood.PresentaionLayer.Materials.Quiz.Show_questions;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Animation.anim;
import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Open_Question_Fun extends AppCompatActivity implements View.OnClickListener {
    Quiz quiz;
    Gson gson=new Gson();
    Strings constants=new Strings();
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_question_fun);
        String q=getIntent().getStringExtra("quiz");
        quiz=gson.fromJson(q,Quiz.class);
        String st=getIntent().getStringExtra("student");
        student=gson.fromJson(st,Student.class);
        initViews();
        initQuestion(quiz.questions.get(0));
    }

    private void initQuestion(Question q) {
        question.setText(q.Question);
        try {
            image.setImageURI(Uri.parse(q.getImage_url()));
        }
        catch (Exception e){

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==next){
                    q.userAns=ans.getText().toString();
                    int index = quiz.questions.indexOf(q);

                    if ((index + 1) >= quiz.questions.size()) {
                        int gr=100;
                        int k=100/(quiz.Oquestions.size()+quiz.questions.size());
                        for(OptionsQ optionsQ:quiz.Oquestions){
                            if(!optionsQ.userAns.equals(optionsQ.Answer)){
                                gr=gr-k;
                            }
                        }
                        for(Question optionsQ:quiz.questions){
                            if(!optionsQ.userAns.equals(optionsQ.Answer)){
                                gr=gr-k;
                            }
                        }
                        quiz.grade=gr;
                        student.quizHashtable.add(quiz);
                        String email=getSharedPreferences("email",0).getString("email",null);
                        FirebaseDatabase.getInstance().getReference(constants.student).child(email).setValue(student);
                        anim anim=new anim(Open_Question_Fun.this,quiz.grade);
                    }

                    else {

                        initQuestion(quiz.questions.get(index+1));

                    }
                }
            }
        });

    }

    Button next,previous,submit;
    TextView question;
    ImageView image;
    EditText ans;
    private void initViews() {
        image=findViewById(R.id.image);
        question=findViewById(R.id.question);
        ans=findViewById(R.id.ans);
        next=findViewById(R.id.next);

        previous=findViewById(R.id.previous);

        submit=findViewById(R.id.submit);

    }

    @Override
    public void onClick(View v) {
    }
}