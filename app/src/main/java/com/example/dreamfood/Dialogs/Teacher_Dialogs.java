package com.example.dreamfood.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.Materials.Chat.Profile_Adapter;
import com.example.dreamfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Teacher_Dialogs {
    private Dialog d;
    private Teacher teacher=new Teacher();
    private String email;
    Strings con=new Strings();
    public Teacher_Dialogs(Context context, Teacher teacher,String email){
        d=new Dialog(context);
        this.teacher=teacher;
        this.email=email;
    }

    public void add_Message(){
        d.setContentView(R.layout.description);
        EditText editText=d.findViewById(R.id.updescription);
        Button button=d.findViewById(R.id.upd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==button){
                    if(editText.getText().length()>0) {
                        Profile_Adapter.profile profile = new Profile_Adapter.profile();
                        profile.resource =editText.getText().toString();
                    teacher.messages.add(profile);
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
                        databaseReference.setValue(teacher);
                        Toast.makeText(d.getContext(), "message saved", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                }
            }
        });
        d.show();
    }
}
