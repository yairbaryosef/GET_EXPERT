package com.example.dreamfood.BusinessLayer.Classes.Quiz_Adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Quiz_Q_Adapter extends ArrayAdapter<question_info_for_adapter> {
    int counter;
    Quiz quiz;
    public Quiz_Q_Adapter(@NonNull Context context, ArrayList<question_info_for_adapter> arrayList) {
        super(context, R.layout.subject,R.id.text3, arrayList);
        counter=0;
    }
    String item;
    public Quiz_Q_Adapter(@NonNull Context context, ArrayList<question_info_for_adapter> arrayList,Quiz quiz){
        super(context, R.layout.subject,R.id.text3, arrayList);

        this.quiz=quiz;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        question_info_for_adapter q=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_option_question, parent, false);
        }

        ImageView imageView=(ImageView) convertView.findViewById(R.id.image);

        TextView question=(TextView) convertView.findViewById(R.id.question);
        AutoCompleteTextView textInputLayout=(AutoCompleteTextView)convertView.findViewById(R.id.answer);

        question.setText(q.question);
        try {
            Uri.parse(q.url);
            Picasso.get().load(q.url).into(imageView);
        }
        catch (Exception e){
            imageView.setVisibility(View.INVISIBLE);
            imageView.setMaxHeight(0);
        }


        ArrayList<String> items =  q.answers;
        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(getContext(),R.layout.list_item,items);
        textInputLayout.setAdapter(adapterItems);

        textInputLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                counter++;
                Toast.makeText(getContext(), textInputLayout.getText().toString(), Toast.LENGTH_SHORT).show();
                if(q.type.equals("option")){
                    quiz.Oquestions.get(q.index).userAns=item;
                    Toast.makeText(getContext(),  quiz.Oquestions.get(q.index).userAns, Toast.LENGTH_SHORT).show();
                }
                else{
                    quiz.questions.get(q.index).userAns=item;
                }
            }
        });

        textInputLayout.addTextChangedListener(new TextWatcher() {
            String item;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                 if(q.type.equals("option")){
                    quiz.Oquestions.get(q.index).userAns=textInputLayout.getText().toString();
                    Toast.makeText(getContext(),  quiz.Oquestions.get(q.index).userAns, Toast.LENGTH_SHORT).show();
                }
                else{
                    quiz.questions.get(q.index).userAns=textInputLayout.getText().toString();
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        return super.getView(position,convertView, parent);
    }

}
