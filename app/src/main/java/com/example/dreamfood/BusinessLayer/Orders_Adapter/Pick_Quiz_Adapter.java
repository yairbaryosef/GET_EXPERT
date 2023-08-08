package com.example.dreamfood.BusinessLayer.Orders_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Pick_Quiz_Adapter extends ArrayAdapter<Quiz> {
    /*
   constructor 1
    */
    public Pick_Quiz_Adapter(@NonNull Context context, ArrayList<Quiz> arrayList) {
        super(context, R.layout.pick_materials,R.id.text3, arrayList);
    }
    /*
   constructor 2
    */
    public Pick_Quiz_Adapter(@NonNull Context context, ArrayList<Quiz> arrayList, Set<String> keys, Hashtable<String,Quiz> quizzes) {
        super(context, R.layout.pick_materials,R.id.text3, arrayList);
        this.mat=quizzes;
        this.keys=keys;
    }
   Set<String> keys;
   Hashtable<String,Quiz> mat;
    /*
    generate adapter item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Quiz q=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pick_materials, parent, false);
        }
         text=" Quiz: "+q.type+" "+String.valueOf(q.grade);
        CheckBox checkBox=convertView.findViewById(R.id.checkbox);
        checkBox.setText(text);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    text=checkBox.getText().toString();
                    if(!mat.contains(text))
                    {
                        mat.put(text,q);
                    }
                }
                else{
                    if(mat.contains(text))
                    {
                        mat.remove(text);
                    }
                }
            }
        });




        return super.getView(position,convertView, parent);
    }
    String text;
}
