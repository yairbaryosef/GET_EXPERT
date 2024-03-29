package com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class init_teacher_Fragment extends Fragment {
   private String email;
   Teacher teacher=new Teacher();
    public init_teacher_Fragment(String email, Teacher teacher, ArrayList<String> subjects){
       this.email=email;
       this.teacher=teacher;
      suBjects=subjects;
     i=2;
    }
    ArrayList<String> suBjects;
    int i=0;
    boolean isExsist=false;
    String person="";
    /*
    constructor 1
     */
    public init_teacher_Fragment(boolean isExsist,String person,String email){
        i=1;
        this.isExsist=isExsist;
        this.person=person;
        this.email=email;
    }
    CircleImageView imageView;
    Strings con=new Strings();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.exam_details, container, false);
       if(i==1){
           try {
               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(person).child(email);
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       isExsist=true;

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }
           catch (Exception e){
               isExsist=false;
           }
       }
       else if(i==2){
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.subject);
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                       suBjects.add(dataSnapshot.getKey());
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
       else {
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   teacher = snapshot.getValue(Teacher.class);
                   Toast.makeText(getContext(), teacher.getEmail(), Toast.LENGTH_SHORT).show();
                   try {


                       Picasso.get().load(teacher.profile_url).into(imageView);
                   } catch (Exception e) {

                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
        return v;
    }
}
