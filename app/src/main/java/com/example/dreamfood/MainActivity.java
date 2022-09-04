package com.example.dreamfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.PersonController;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PDF_Controller.Create_pdf;
import com.example.dreamfood.PDF_Controller.Draw;
import com.example.dreamfood.Student_Controller.scrolling_activity;
import com.example.dreamfood.Student_Controller.search;
import com.example.dreamfood.Teacher_Controller.Teacher_home;
import com.example.dreamfood.Teacher_Controller.teacher_layout;
import com.google.android.gms.maps.MapView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spinner;
       MapView mapView;
    Button entrance,ADD,pdf,draw;
    TextInputEditText email,password;
    String item;
    PersonController personController;

ImageView imageView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();

       initImage(imageView);
      initSpinner();



    }
    public void initImage(ImageView imageView){
       imageView.setImageResource(R.drawable.class_1);
    }
    public void initSpinner(){
        ArrayList<String> jobs=new ArrayList<String>();
        jobs.add("teacher");jobs.add("student");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_multichoice, jobs);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
    }
     public void initWidgets(){
         personController=new PersonController();
         imageView=findViewById(R.id.image);
         spinner = (Spinner) findViewById(R.id.spinner);
         entrance=(Button)findViewById(R.id.entrance);
         ADD=(Button)findViewById(R.id.register);
         ADD.setOnClickListener(this);
         draw=(Button)findViewById(R.id.draw);
         draw.setOnClickListener(this);
         pdf=(Button)findViewById(R.id.pdf);
         pdf.setOnClickListener(this);
         password=(TextInputEditText) findViewById(R.id.password);
         email=(TextInputEditText) findViewById(R.id.email);
         entrance.setOnClickListener(this);
     }
  private boolean A=false;
    @Override
    public void onClick(View v) {
        if(v==draw){
            Intent intent=new Intent(this, Draw.class);
            startActivity(intent);
        }
        if(v==pdf){
            Intent intent=new Intent(this, Create_pdf.class);
            startActivity(intent);
        }
        if(v==ADD){
            Intent intent=new Intent(this, Register.class);
            startActivity(intent);
        }
        else if(v==entrance) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef=database.getReference(item);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        try {
                            if(Objects.equals(item, "student")) {
                                String key = snapshot.getKey();
                                 Student person = snapshot.getValue(Student.class);

                                if (email.getText().toString().equals(key) || email.getText().toString().equals(person.getEmail())) {
                                    if (password.getText().toString().equals(person.getPassword())) {
                                        A = true;
                                         }


                                } else {
                                    A = false;
                                }
                            }
                            else {
                                String key = snapshot.getKey();

                                Teacher person = snapshot.getValue(Teacher.class);
                                if (email.getText().toString().equals(key) || email.getText().toString().equals(person.getEmail())) {
                                    if (password.getText().toString().equals(person.getPassword())) {
                                        A = true;
                                    }


                                } else {
                                    A = false;
                                }
                            }
                            if(A) {
                                SharedPreferences sp=getSharedPreferences("email",0);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("email",email.getText().toString());
                                editor.commit();

                                if (item.equals("student")) {
                                    Intent intent = new Intent(MainActivity.this, scrolling_activity.class);

                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, teacher_layout.class);

                                    startActivity(intent);
                                }
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });

           if(A) {
               SharedPreferences sp=getSharedPreferences("email",0);
               SharedPreferences.Editor editor=sp.edit();
               editor.putString("email",email.getText().toString());
               editor.commit();
               Toast.makeText(MainActivity.this, "a", Toast.LENGTH_SHORT).show();

               if (item.equals("student")) {
                   Intent intent = new Intent(this, search.class);

                   startActivity(intent);
               } else {
                   Intent intent = new Intent(this, Teacher_home.class);

                   startActivity(intent);
               }
           }



        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        SharedPreferences sp=getSharedPreferences("roll",0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("roll",item);
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}