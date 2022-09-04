package com.example.dreamfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

public class Exam_fragment extends Fragment {
    View v;
    TextView test,grade,com;
    Spinner spinner;
    Strings con=new Strings();
    ArrayList<String> arrayList;
    Hashtable<String,grade> grades;
    public Exam_fragment(ArrayList<String> array, Hashtable<String, grade> grades){
        arrayList=array;
        this.grades=grades;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.exam_details, container, false);
        spinner=(Spinner) v.findViewById(R.id.spinner);
        test=v.findViewById(R.id.test);
        grade=v.findViewById(R.id.grade);
        com=v.findViewById(R.id.comments);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_multichoice, arrayList);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                if(!item.equals("choose test")) {
                    grade grade1 = grades.get(item);
                    try {


                        grade.setText(String.valueOf(grade1.grade));


                        com.setText(grade1.comments);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }
}
