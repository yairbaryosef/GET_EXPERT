package com.example.dreamfood.Materials.Quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Get_Started_Student;
import com.example.dreamfood.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class Quiz_One_Question_View extends AppCompatActivity {
   ListView list_Questions;
   ListView list_Oquestions;
    Strings constants=new Strings();
Quiz quiz;
Student student;
Button submit;
Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_one_question_view);
        gson=new Gson();
        String quiz_json=getIntent().getStringExtra("quiz");
         quiz=gson.fromJson(quiz_json,Quiz.class);
        String student_json=getIntent().getStringExtra("student");
         student=gson.fromJson(student_json,Student.class);
        initWidgets();

    }
    public void initWidgets(){
        list_Oquestions=findViewById(R.id.listoptions);
        list_Questions=findViewById(R.id.list);
         ArrayList<ArrayList<OptionsQ>> arrayList=new ArrayList<>();
        ArrayList<OptionsQ> optionsQS=new ArrayList<>();
         for(OptionsQ optionsQ:quiz.Oquestions) {

             optionsQS.add(optionsQ);
         }
         init_ListOptions init_listOptions=new init_ListOptions(this,optionsQS);
         list_Oquestions.setAdapter(init_listOptions);
         submit=findViewById(R.id.quiz);
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(v==submit){
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
                     FirebaseDatabase.getInstance().getReference(constants.student).child(constants.emailStart(student.getEmail())).setValue(student);
                     Intent intent=new Intent(Quiz_One_Question_View.this, Get_Started_Student.class);
                     Toast.makeText(Quiz_One_Question_View.this,"Your grade is: "+ String.valueOf(quiz.grade), Toast.LENGTH_LONG).show();
                     startActivity(intent);

                 }
             }
         });

    }
    TextView status;
    ArrayList<TextView> textViews;
    Dialog d;
    public class init_ListOptions extends ArrayAdapter<OptionsQ> implements View.OnClickListener {
ArrayList<OptionsQ> optionsQ;
        public init_ListOptions(@NonNull Context context, ArrayList<OptionsQ> arrayList) {
            super(context, R.layout.questions_row,R.id.text3, arrayList);
textViews=new ArrayList<>();
optionsQ=arrayList;
        }
        Button[] questions;
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            OptionsQ optionsQS=getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.questions_row, parent, false);
            }

            Button q=convertView.findViewById(R.id.show_question);
            int i=optionsQ.indexOf(optionsQS);
            q.setText(String.valueOf(i+1));

           status=convertView.findViewById(R.id.status);
           textViews.add(status);
            if(!optionsQS.userAns.equals("")){
                status.setBackgroundResource(R.color.grey);
            }
            q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==q){
                        One_Option_Question_Dialog(optionsQS);
                    }
                }
            });

            return super.getView(position,convertView, parent);
        }

        @Override
        public void onClick(View v) {
          if(v==imageb){
              Add_Picture_Dialog(url);
          }
            if(v==submit_q){
                answers[answer].setBackgroundResource(R.drawable.pick_green_answer_button_design);
                Toast.makeText(Quiz_One_Question_View.this, "right", Toast.LENGTH_SHORT).show();
                if(answer!=final_answer){
                    answers[final_answer].setBackgroundResource(R.drawable.pick_red_answer_button_design);
                    Toast.makeText(Quiz_One_Question_View.this, "wrong", Toast.LENGTH_SHORT).show();
                }
            }


        }
        ImageView add_picture;
        public void Add_Picture_Dialog(String url){
            d=new Dialog(getContext());
            d.setContentView(R.layout.image_view);
            add_picture=d.findViewById(R.id.add_picture);
            Picasso.get().load(Uri.parse(url)).into(add_picture);
            d.show();
        }

        Dialog d;
        TextView question,number;
        ImageView imageb;
        Button[] answers=new Button[5];
        Button submit_q,next,previous;
        String url="";
        public void One_Option_Question_Dialog(OptionsQ optionsQ){
            d=new Dialog(getContext());
            d.setContentView(R.layout.activity_quiz);
             question=d.findViewById(R.id.question);
             number=d.findViewById(R.id.number);
             int k=quiz.Oquestions.size()+quiz.questions.size();
             int index=quiz.Oquestions.indexOf(optionsQ);
             number.setText(String.valueOf(index+1)+"/"+String.valueOf(k));
             question.setText(optionsQ.Question);
             imageb=d.findViewById(R.id.image);
             imageb.setOnClickListener(this);
             answers[0]=d.findViewById(R.id.answer_1);
            answers[1]=d.findViewById(R.id.answer_2);
            answers[2]=d.findViewById(R.id.answer_3);
            answers[3]=d.findViewById(R.id.answer_4);
            submit_q=d.findViewById(R.id.submit);
            submit_q.setOnClickListener(this);
            next=d.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==next) {
                        int index = quiz.Oquestions.indexOf(optionsQ);
                        if ((index + 1) >= quiz.Oquestions.size()) {
                            Toast.makeText(Quiz_One_Question_View.this, "there is no next question", Toast.LENGTH_SHORT).show();

                        } else {
                            One_Option_Question_Dialog(quiz.Oquestions.get(index + 1));

                        }
                    }
                }
            });
            url=optionsQ.getImage_url();
            Picasso.get().load(Uri.parse(url)).into(imageb);
            previous=d.findViewById(R.id.previous);
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==previous) {
                        int index = quiz.Oquestions.indexOf(optionsQ);
                        if ((index - 1) < 0) {
                            Toast.makeText(Quiz_One_Question_View.this, "there is no previous question", Toast.LENGTH_SHORT).show();
                        } else {
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
                            Toast.makeText(Quiz_One_Question_View.this, optionsQ.userAns, Toast.LENGTH_SHORT).show();
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
                    String ans=optionsQ.answers.get(j);
                    j++;
                    answers[i].setText(ans);
                }
            }
            d.show();
        }
        int pos=0;
        int answer;
        Button button_grey;
        int final_answer=0;
    }
}