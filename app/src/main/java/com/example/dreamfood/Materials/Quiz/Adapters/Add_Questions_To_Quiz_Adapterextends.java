package com.example.dreamfood.Materials.Quiz.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.Dialogs.Question_Answers;
import com.example.dreamfood.R;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Add_Questions_To_Quiz_Adapterextends extends ArrayAdapter<Question>    {
        int i;
        int size;
        Quiz q;

public Add_Questions_To_Quiz_Adapterextends(@NonNull Context context, ArrayList<Question> arrayList, Quiz quiz) {
        super(context, R.layout.add_quiz_list_of_questions, R.id.text3, arrayList);
        i=0;
        q=quiz;
        size=arrayList.size();
        }


@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Question ans=getItem(position);
        if(convertView==null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_quiz_list_of_questions, parent, false);
        }
    Question_Answers question_answers=new Question_Answers(getContext());
    image=convertView.findViewById(R.id.image);
    question_textview=convertView.findViewById(R.id.question);
     update=convertView.findViewById(R.id.update);
    delete=convertView.findViewById(R.id.delete);
    answer=convertView.findViewById(R.id.answer);
      try{
          question_textview.setText(ans.Question);
      }
      catch (Exception e){

      }
        if(ans instanceof OptionsQ){
                OptionsQ optionsQ=(OptionsQ) ans;
                option_listeners(optionsQ);
                if(!optionsQ.getImage_url().equals("")){
                    try {


                        Picasso.get().load(Uri.parse(optionsQ.getImage_url())).into(image);
                    }
                    catch (Exception e){

                    }
                }
         }
        else{
            open_Q_listeners(ans);
            if(!ans.getImage_url().equals("")){
                try {


                    Picasso.get().load(Uri.parse(ans.getImage_url())).into(image);
                }
                catch (Exception e){

                }
            }
        }

        return super.getView(position,convertView, parent);
        }

    private void open_Q_listeners(Question question) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.questions.remove(question);
                remove(question);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_Question_details(question);
            }
        });
    }
    TextInputEditText qes,ans,wrong;
Button save,add;
ListView list;
    private void Update_Option_Question_details(OptionsQ question) {
          d=new Dialog(getContext());
          d.setContentView(R.layout.question_update_dialog);
          qes=d.findViewById(R.id.question);
          list=d.findViewById(R.id.list);
          ans=d.findViewById(R.id.answer);
          wrong=d.findViewById(R.id.wrong);
          save=d.findViewById(R.id.save);
          answers=new ArrayList<>();
          save.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(ans.getText().length()>0&&qes.getText().length()>0&&answers.size()>1){
                      question.Question=qes.getText().toString();
                      question.Answer=ans.getText().toString();
                      answers.add(question.Answer);
                      question.answers=answers;
                      d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                          @Override
                          public void onDismiss(DialogInterface dialog) {
                              question_textview.setText(question.Question);
                          }
                      });
                      d.dismiss();
                  }
              }
          });
        add=d.findViewById(R.id.add);
      add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(v==add){
                  if(wrong.getText().length()>0){
                      answers.add(wrong.getText().toString());
                      ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,answers);
                      list.setAdapter(arrayAdapter);
                  }
              }
          }
      });
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              answers.remove(answers.get(position));
              ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,answers);
              list.setAdapter(arrayAdapter);
          }
      });
          d.show();

    }
    private void Update_Question_details(Question question) {
        d=new Dialog(getContext());
        d.setContentView(R.layout.question_update_dialog);
        qes=d.findViewById(R.id.question);
        list=d.findViewById(R.id.list);
        ans=d.findViewById(R.id.answer);
        wrong=d.findViewById(R.id.wrong);
        save=d.findViewById(R.id.save);
        answers=new ArrayList<>();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ans.getText().length()>0&&qes.getText().length()>0){
                    question.Question=qes.getText().toString();
                    question.Answer=ans.getText().toString();
                    d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            question_textview.setText(question.Question);
                        }
                    });
                    d.dismiss();
                }
            }
        });
        add=d.findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        d.show();

    }
   ArrayList<String> answers;
    Dialog d;
ImageButton add_picture;


    private void option_listeners(OptionsQ optionsQ) {
          delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  q.Oquestions.remove(optionsQ);
                  remove(optionsQ);
              }
          });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_Option_Question_details(optionsQ);
            }
        });
    }


    ImageView image;
Button delete,update,question_textview,answer;

}