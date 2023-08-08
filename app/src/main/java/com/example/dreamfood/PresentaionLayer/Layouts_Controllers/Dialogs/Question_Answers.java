package com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.R;

public class Question_Answers {
    private Strings con;
    private Context context;
    public Question_Answers( Context context){
        con=new Strings();
        this.context=context;

    }

    public void Open_Q(Question question) {
        Toast.makeText(context, "question", Toast.LENGTH_SHORT).show();
    }

    public  void Option_Q(OptionsQ optionsQ){
        Toast.makeText(context, "option", Toast.LENGTH_SHORT).show();
    }
    boolean a=false;
    public void Choose_Q_type(Quiz quiz){
        Dialog d=new Dialog(context);
        d.setContentView(R.layout.ope_or_option_question_dialog);
        Button open,option;
        open=d.findViewById(R.id.open);
        option=d.findViewById(R.id.option);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question=new Question();
                quiz.questions.add(question);
                d.dismiss();
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsQ question=new OptionsQ();
                quiz.Oquestions.add(question);
                d.dismiss();
            }
        });
        d.show();

    }


}
