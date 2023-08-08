package com.example.dreamfood.PresentaionLayer.Materials.Record;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.PresentaionLayer.Materials.Quiz.Quiz_activity;
import com.example.dreamfood.R;
import com.example.dreamfood.Teacher_Controller.Teacher_home;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Open_Recordings extends AppCompatActivity implements View.OnClickListener {
     Dialog d;
     Button save,adda,save2,addQ,description,updateBUTTON,addquiz,show;
    Recording_class recording_class=new Recording_class();
     EditText qe,ans,updateEDITTEXT,answer;

   ArrayList<String> subjects;
   Strings con=new Strings();
 TextInputEditText price,password;
 AutoCompleteTextView subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_quiz);
       Gson gson=new Gson();
       String record_json=getIntent().getStringExtra("record");
       recording_class=gson.fromJson(record_json,Recording_class.class);
        init_Subjects();
        initViews();
    }
    String getItem="";
    /*
    init views
     */
    private void initViews(){

        addquiz=(Button)findViewById(R.id.addquiz);
        addquiz.setText("add record");
        addquiz.setOnClickListener(this);
        subject=(AutoCompleteTextView) findViewById(R.id.subject);
        init_Subjects();
        password=(TextInputEditText) findViewById(R.id.pass);
        price=(TextInputEditText) findViewById(R.id.price);
        description=(Button) findViewById(R.id.description);
        description.setOnClickListener(this);
    }
    /*
    init subjects for subjects spinner
     */
    private void init_Subjects() {
        subjects=new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.subject);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sub:snapshot.getChildren()){
                    subjects.add(sub.getKey());

                }
                ArrayAdapter<String> adapterItems=new ArrayAdapter<String>(Open_Recordings.this,R.layout.list_item,subjects);
                subject.setAdapter(adapterItems);

                subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        getItem = parent.getItemAtPosition(position).toString();
                        Toast.makeText(getApplicationContext(),"Item: "+getItem,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v==show){
            Intent intent=new Intent(this, Quiz_activity.class);
           startActivity(intent);
        }
        if(v==addquiz){

                if (!getItem.equals("")) {
                    recording_class.email=getSharedPreferences("email",0).getString("email",null);
                    try {

                        recording_class.price=Double.valueOf(price.getText().toString());
                        recording_class.subject=getItem;
                       // recording_class.description=updateEDITTEXT.getText().toString();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.recording);
                        databaseReference.child(recording_class.email).child(recording_class.subject).setValue(recording_class);
                        Intent intent = new Intent(Open_Recordings.this, Teacher_home.class);
                        startActivity(intent);
                    }
                    catch (Exception e){
                        Toast.makeText(this, "invalid input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Open_Recordings.this, "pick a subject", Toast.LENGTH_SHORT).show();



                }





        }
        if(v==updateBUTTON){
            recording_class.description=updateEDITTEXT.getText().toString();
            d.dismiss();
        }


           if(v==description){
              createdialogdetails();
           }
    }
    /*
    description dialog
     */
        public void createdialogdetails()
        {
            d= new Dialog(this);
            d.setContentView(R.layout.description);
            d.setCancelable(true);
            updateEDITTEXT=(EditText) d.findViewById(R.id.updescription);
            updateBUTTON=(Button) d.findViewById(R.id.upd);
            updateBUTTON.setOnClickListener(this);
            d.show();

        }




}