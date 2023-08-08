package com.example.dreamfood.PresentaionLayer.Materials.Quiz;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Get_Started_Student;
import com.example.dreamfood.PresentaionLayer.Materials.Quiz.Show_questions.Open_Question_Fun;
import com.example.dreamfood.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class Fun_Quiz_Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        String st=getIntent().getStringExtra("student");
        String q=getIntent().getStringExtra("quiz");
        student=gson.fromJson(st,Student.class);
        quiz=gson.fromJson(q,Quiz.class);
         if(quiz.Oquestions.size()!=0)
        One_Option_Question_Dialog(quiz.Oquestions.get(0));
         else {
             Intent intent=new Intent(Fun_Quiz_Activity.this, Open_Question_Fun.class);
             String json=gson.toJson(quiz);
             st=gson.toJson(student);
             intent.putExtra("quiz",json);
             intent.putExtra("student",st);
             startActivity(intent);
         }

    }
    Student student=new Student();

    Gson gson=new Gson();
    TextView question,number;
    Quiz quiz=new Quiz();
    int answer;
    Button button_grey;
    int final_answer=0;
    ImageView imageb;
    Button[] answers=new Button[4];
    Button submit_q,next,previous;
    String url="";
    Strings constants=new Strings();
    public void One_Option_Question_Dialog(OptionsQ optionsQ){

        question=findViewById(R.id.question);
        number=findViewById(R.id.number);
        int k=quiz.Oquestions.size()+quiz.questions.size();
        int index=quiz.Oquestions.indexOf(optionsQ);
        number.setText(String.valueOf(index+1)+"/"+String.valueOf(k));
        question.setText(optionsQ.Question);
        imageb=findViewById(R.id.image);
       // imageb.setOnClickListener(this);
        answers[0]=findViewById(R.id.answer_1);
        answers[1]=findViewById(R.id.answer_2);
        answers[2]=findViewById(R.id.answer_3);
        answers[3]=findViewById(R.id.answer_4);
        submit_q=findViewById(R.id.submit);
        submit_q.setOnClickListener(this);
        next=findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==next) {

                    int index = quiz.Oquestions.indexOf(optionsQ);

                    if ((index + 1) >= quiz.Oquestions.size()+quiz.questions.size()) {
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
                        Intent intent=new Intent(Fun_Quiz_Activity.this, Get_Started_Student.class);
                        Toast.makeText(Fun_Quiz_Activity.this,"Your grade is: "+ String.valueOf(quiz.grade), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                    else if((index + 1) >= quiz.Oquestions.size()){
                        Toast.makeText(Fun_Quiz_Activity.this, String.valueOf(quiz.Oquestions.size()), Toast.LENGTH_SHORT).show();
                       Intent intent=new Intent(Fun_Quiz_Activity.this, Open_Question_Fun.class);
                       String json=gson.toJson(quiz);
                       String st=gson.toJson(student);
                       intent.putExtra("quiz",json);
                       intent.putExtra("student",st);
                       startActivity(intent);
                    }
                    else {
                        for(int i=0;i<4;i++){
                            answers[i].setBackgroundResource(R.drawable.circle);
                        }
                        One_Option_Question_Dialog(quiz.Oquestions.get(index + 1));

                    }
                }
            }
        });
        url=optionsQ.getImage_url();
        Picasso.get().load(Uri.parse(url)).into(imageb);
        previous=findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==previous) {
                    int index = quiz.Oquestions.indexOf(optionsQ);
                    if ((index - 1) < 0) {
                        Toast.makeText(Fun_Quiz_Activity.this, "there is no previous question", Toast.LENGTH_SHORT).show();
                    } else {
                        for(int i=0;i<4;i++){
                            answers[i].setBackgroundResource(R.drawable.circle);
                        }
                        One_Option_Question_Dialog(quiz.Oquestions.get(index - 1));
                    }
                }
            }
        });


        Random r=new Random();
        answer=r.nextInt(4);
        answers[answer].setText(optionsQ.Answer);
        int j=0;

        for(int i=0;i<4;i++){
            answers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(v==answers[0]){
                        if(button_grey!=null){
                            button_grey.setBackgroundResource(R.drawable.circle);
                        }
                        button_grey=answers[0];
                        final_answer=0;
                        optionsQ.userAns=answers[0].getText().toString();
                        Toast.makeText(Fun_Quiz_Activity.this, optionsQ.userAns, Toast.LENGTH_SHORT).show();
                        answers[0].setBackgroundResource(R.drawable.pick_grey_answer_button_design);
                    }
                    if(v==answers[1]){
                        if(button_grey!=null){
                            button_grey.setBackgroundResource(R.drawable.circle);
                        }
                        final_answer=1;
                        optionsQ.userAns=answers[1].getText().toString();
                        button_grey=answers[1];
                        answers[1].setBackgroundResource(R.drawable.pick_grey_answer_button_design);
                    }
                    if(v==answers[2]){
                        final_answer=2;
                        if(button_grey!=null){
                            button_grey.setBackgroundResource(R.drawable.circle);
                        }
                        optionsQ.userAns=answers[2].getText().toString();
                        button_grey=answers[2];
                        answers[2].setBackgroundResource(R.drawable.pick_grey_answer_button_design);
                    }
                    if(v==answers[3]){
                        final_answer=3;
                        if(button_grey!=null){
                            button_grey.setBackgroundResource(R.drawable.circle);
                        }
                        optionsQ.userAns=answers[3].getText().toString();
                        button_grey=answers[3];
                        answers[3].setBackgroundResource(R.drawable.pick_grey_answer_button_design);
                    }
                }
            });
            if(i!=answer){
                if(i<=optionsQ.answers.size()) {
                    String ans = optionsQ.answers.get(j);
                    j++;
                    answers[i].setText(ans);
                }
                else{
                    answers[i].setVisibility(View.INVISIBLE);
                }
            }
        }

    }
    ImageView add_picture;
    Dialog d;
    public void Add_Picture_Dialog(String url){
        d=new Dialog(this);
        d.setContentView(R.layout.image_view);
        add_picture=d.findViewById(R.id.add_picture);
        add_picture.setBackgroundResource(R.drawable.white);
        Picasso.get().load(Uri.parse(url)).into(add_picture);
        d.show();
    }
    @Override
    public void onClick(View v) {
        if(v==imageb){
            Add_Picture_Dialog(url);
        }
        if(v==submit_q){
            answers[answer].setBackgroundResource(R.drawable.pick_green_answer_button_design);
            Toast.makeText(Fun_Quiz_Activity.this, "right", Toast.LENGTH_SHORT).show();
            if(answer!=final_answer){
                answers[final_answer].setBackgroundResource(R.drawable.pick_red_answer_button_design);
                Toast.makeText(Fun_Quiz_Activity.this, "wrong", Toast.LENGTH_SHORT).show();
            }
        }


    }
}