package com.example.dreamfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.PersonController;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PDF_Controller.Create_pdf;
import com.example.dreamfood.Student_Controller.scrolling_activity;
import com.example.dreamfood.Student_Controller.search;
import com.example.dreamfood.Teacher_Controller.Teacher_home;
import com.example.dreamfood.Teacher_Controller.teacher_layout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.Task;
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

    Button entrance,ADD,pdf,google;
    EditText email,password;
    String item;
    String email_int,password_int,type_int;
    PersonController personController;

ImageView imageView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
     
      initSpinner();
      try {
           email_int= getSharedPreferences("email",0).getString("email",null);
           password_int=getSharedPreferences("email",0).getString("password",null);
           type_int=getSharedPreferences("email",0).getString("type",null);


      }
      catch (Exception e){

      }


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
         ADD=(Button)findViewById(R.id.register);
         ADD.setOnClickListener(this);
         google=(Button)findViewById(R.id.google);
         google.setOnClickListener(this);

         pdf=(Button)findViewById(R.id.pdf);
         pdf.setOnClickListener(this);
         password=findViewById(R.id.password);
         email= findViewById(R.id.email);
         entrance.setOnClickListener(this);
     }
  private boolean A=false;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    public void onClick(View v) {
       if(v==google){
         gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
           gsc= GoogleSignIn.getClient(this,gso);
         Signin_Google();

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
                                editor.putString("type",item);
                                editor.putString("password",password.getText().toString());
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

    private void Signin_Google() {
        Intent intent=gsc.getSignInIntent();
        startActivityForResult(intent,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

               // Home();
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
Strings con=new Strings();
    private void Home() {
       // finish();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(item);
        try {

        Intent intent=new Intent(this,Google_Signin_Activity.class);
        startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
       /* if(item.equals(con.student)){
            Student student=new Student();

            student.setEmail(account.getEmail());
            student.setPassword(account.getDisplayName());
            databaseReference.child(con.emailStart(account.getEmail())).setValue(student);
        }
        else{
            Teacher student=new Teacher();

            student.setEmail(account.getEmail());
            student.setPassword(account.getDisplayName());
            databaseReference.child(con.emailStart(account.getEmail())).setValue(student);
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
     //   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if(item.equals(type_int)) {
            email.setText(email_int);
            password.setText(password_int);
            Toast.makeText(this, email_int, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}