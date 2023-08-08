package com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating;
import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.Video_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.Fragment_Activity_default;
import com.example.dreamfood.R;
import com.example.dreamfood.Student_Controller.scrolling_activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class List_Video_Fragment extends Fragment {
    View v;
    Video_Adapter video_adapter;
    ArrayList<video> videos;
    Strings con=new Strings();
    String s="";
    ListView list;
    public List_Video_Fragment(ArrayList<video> recording_class){
        videos=recording_class;
        s="video";
    }
    Rating rating;
    Teacher teacher;
    public List_Video_Fragment(Rating rating, Teacher teacher){
        this.rating=rating;
        this.teacher=teacher;
        s="rate active";
    }
    ArrayList<String> ratings_names;
    public List_Video_Fragment(ArrayList<Rating> recording_class,int i){
        ratings_names=new ArrayList<>();
        for(Rating r:recording_class){
            ratings_names.add(r.type+" "+r.subject+" by "+r.teacher_email);
        }
        s="rate name";
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.videos_list, container, false);
        list=v.findViewById(R.id.list);
       if( s.equals("video")) {
            video_adapter = new Video_Adapter(getContext(), videos);
            list.setAdapter(video_adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
       else if(s.equals("rate name")){
           ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,ratings_names);
           list.setAdapter(arrayAdapter);
           list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent=new Intent(getContext(), Fragment_Activity_default.class);
                   intent.putExtra("type","rating active");
                   Gson gson=new Gson();
                   SharedPreferences sp=getContext().getSharedPreferences(constants.student,0);
                   String json=sp.getString(constants.student,null);
                   student=gson.fromJson(json, Student.class);
                   Rating rate=new Rating();
                   String item=parent.getItemAtPosition(position).toString();
                   for(Rating r:student.ratings){
                       if(item.equals(r.type+" "+r.subject+" by "+r.teacher_email)){
                           rate=r;
                           break;
                       }
                   }
                   json=gson.toJson(rate);
                   intent.putExtra("Rating",json);
                   startActivity(intent);
               }
           });
       }
       else if(s.equals("rate active")){
         Create_Dialog_For_Rating();

       }

        return v;
    }
Dialog d;
    private void Create_Dialog_For_Rating() {
        d=new Dialog(getContext());
        d.setContentView(R.layout.list_rating_student_active);
        list=d.findViewById(R.id.list);
        Rating_Adapter video_adapter = new Rating_Adapter(getContext(),rating.rates );
        list.setAdapter(video_adapter);
        Button save=d.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==save){

                    rating.date= Calendar.getInstance().getTime();
                    teacher.ratings.add(rating);
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.teacher);
                    databaseReference.child(rating.teacher_email).setValue(teacher);
                    Gson gson=new Gson();
                    SharedPreferences sp=getContext().getSharedPreferences(constants.student,0);
                    String json=sp.getString(constants.student,null);
                    student=gson.fromJson(json,Student.class);
                     for(Rating r:student.ratings){
                         if(r.type.equals(rating)&&r.subject.equals(rating)&&r.teacher_email.equals(rating)){
                             student.ratings.remove(r);
                         }
                     }

                     databaseReference= FirebaseDatabase.getInstance().getReference(constants.student);
                    databaseReference.child(student.getEmail()).setValue(student);
                    Toast.makeText(getContext(), "Rating saved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext(),scrolling_activity.class);
                    startActivity(intent);
                }
            }
        });
        d.show();
    }

    Student student=new Student();
    Strings constants=new Strings();


}
