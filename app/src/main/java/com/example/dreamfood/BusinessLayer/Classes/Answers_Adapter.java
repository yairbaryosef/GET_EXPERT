package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;

import java.util.ArrayList;

public class Answers_Adapter extends ArrayAdapter<answers_adapter_item> {
   int i;
   int size;
    public Answers_Adapter(@NonNull Context context, ArrayList<answers_adapter_item> arrayList) {
        super(context, R.layout.subject,R.id.text3, arrayList);
        i=1;
        size=arrayList.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        answers_adapter_item ans=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.answers_for_quiz_right_or_wrong, parent, false);
        }

        TextView number=(TextView) convertView.findViewById(R.id.question_number);
        TextView right_answer=(TextView) convertView.findViewById(R.id.right);
        TextView your_answer=(TextView) convertView.findViewById(R.id.your);

        if(ans.right){
            number.setBackgroundResource(R.color.colorgreen);
        }
        else{
           number.setBackgroundResource(R.color.red);
        }
        if(i<=size) {
            number.setText(String.valueOf(i));
            i++;
        }
        right_answer.setText("right answer: "+ans.right_answer);
        right_answer.setTextColor(getContext().getColor(R.color.colorgreen));
        your_answer.setText("your answer: "+ans.your_answer);
        if(ans.right){
            your_answer.setTextColor(getContext().getColor(R.color.colorgreen));
        }
        else {
            your_answer.setTextColor(getContext().getColor(R.color.red));
        }
        return super.getView(position,convertView, parent);
    }
}
