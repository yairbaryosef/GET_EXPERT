package com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class init_student_Fragment extends Fragment {


    /*
    constructor 1
     */
    public init_student_Fragment(Student student, String email,String job,String sender,String receiver){
       this.email=email;
       this.student=student;
       this.job=job;
       this.sender=sender;
       this.receiver=receiver;
    }
    Student student;
    String email,job,sender,receiver;
    Strings con=new Strings();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.exam_details, container, false);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.student).child(email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);
                if(job.equals(con.teacher)) {
                    if(student.teacher_send_count==null){
                        student.teacher_send_count=new ArrayList<>();
                        student.teachers_send_username=new ArrayList<>();
                    }
                    if (student.teachers_send_username.contains(sender)){
                        int get_position=student.teachers_send_username.indexOf(sender);
                        int count=student.teacher_send_count.get(get_position);
                        count++;
                        student.teacher_send_count.set(get_position,count);

                    }
                    else{
                        student.teachers_send_username.add(sender);
                        student.teacher_send_count.add(1);

                    }
                }
                else{
                    if (student.teachers_send_username.contains(receiver)){
                        int get_position=student.teachers_send_username.indexOf(receiver);
                        int count=student.teacher_send_count.get(get_position);
                        count++;
                        student.teacher_send_count.set(get_position,count);

                    }
                    else{
                        student.teachers_send_username.add(receiver);
                        student.teacher_send_count.add(1);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}
