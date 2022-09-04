package com.example.dreamfood.Materials.Test;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.grade;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Fragments.Exam_fragment;
import com.example.dreamfood.Fragments.Graph_fragment;
import com.example.dreamfood.R;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class Test_result extends AppCompatActivity {
    FrameLayout frameLayout;
    ArrayList<String> grades;
    Student student;
    Hashtable<String, grade> ListGrades;
    Strings con=new Strings();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        FlareBar bottomBar = findViewById(R.id.bottomBar);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
         grades=new ArrayList<>();
         ListGrades=new Hashtable<>();
         grades.add("choose test");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.student);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sp=getSharedPreferences("email",0);
                String email=sp.getString("email",null);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(email)||dataSnapshot.getValue(Student.class).getEmail().equals(email)){
                        student=dataSnapshot.getValue(Student.class);
                        ArrayList<grade> gradeArrayList=student.grades;
                        int i=0;
                        for(grade g:gradeArrayList){
                            grades.add(g.teacher+String.valueOf(i));
                            ListGrades.put(g.teacher+String.valueOf(i),g);
                            i++;
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.exam),"exam","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.graph),"graphs","#B39DDB"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(Test_result.this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Test_result.this,"Tab "+ tabs.get(selectedIndex).getTabText()+" Selected.",Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if( tabs.get(selectedIndex).getTabText().equals("graphs")) {
                    selectedFragment = new Graph_fragment(ListGrades);
                }
                else {
                    selectedFragment = new Exam_fragment(grades,ListGrades);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });

    }

}