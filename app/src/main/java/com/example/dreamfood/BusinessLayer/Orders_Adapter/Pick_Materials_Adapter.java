package com.example.dreamfood.BusinessLayer.Orders_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Pick_Materials_Adapter extends ArrayAdapter<grade> {
    public Pick_Materials_Adapter(@NonNull Context context, ArrayList<grade> arrayList, Set<String> strings, Hashtable<String,grade> materials) {
        super(context, R.layout.pick_materials,R.id.text3, arrayList);
        mat=materials;
        keys=strings;
    }
    public Pick_Materials_Adapter(@NonNull Context context, ArrayList<grade> arrayList,ArrayList<grade> grade) {
        super(context, R.layout.pick_materials,R.id.text3, arrayList);
    }
  Hashtable <String,grade> mat;
    Set<String> keys;
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        grade grade=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pick_materials, parent, false);
        }
        if (!(grade.subject==null)) {
            text = grade.subject + " get " + String.valueOf(grade.grade);
        }
        else{
            text =  String.valueOf(grade.grade);
        }
        CheckBox checkBox=convertView.findViewById(R.id.checkbox);
        checkBox.setText(text);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!mat.contains(grade))
                    {
                        text=checkBox.getText().toString();
                        mat.put(text,grade);
                        Toast.makeText(getContext(), String.valueOf(mat.size()+" "+text), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    text=checkBox.getText().toString();
                    if(mat.contains(grade))
                    {

                            mat.remove(text);
                        Toast.makeText(getContext(), String.valueOf(mat.size()+" "+text), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        return super.getView(position,convertView, parent);
    }
    String text;
}
