package com.example.dreamfood.Materials.Quiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.R;
import com.example.dreamfood.Teacher_Controller.Teacher_home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Open_Quiz extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
     Dialog d;
     Button save,adda,save2,addQ,description,updateBUTTON,addquiz,show;
     Switch sw;
     ListView lv;
     TextView TV;
     EditText qe,ans,updateEDITTEXT,answer;
    ArrayList<String> answe;
    String item;
   ArrayList<String> subjects;
   Strings con=new Strings();
 TextInputEditText price,password;
 AutoCompleteTextView subject;
     Quiz quiz=new Quiz();
    Question q=new Question();
     boolean a=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_quiz);
        Gson gson=new Gson();
       String json=getIntent().getStringExtra("quiz");
       quiz=gson.fromJson(json,Quiz.class);
subjects=new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.subject);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot sub:snapshot.getChildren()){
                   subjects.add(sub.getKey());

               }
                ArrayAdapter<String> adapterItems=new ArrayAdapter<String>(Open_Quiz.this,R.layout.list_item,subjects);
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

        addquiz=(Button)findViewById(R.id.addquiz);
        addquiz.setOnClickListener(this);
        subject=(AutoCompleteTextView) findViewById(R.id.subject);
        password=(TextInputEditText) findViewById(R.id.pass);
        price=(TextInputEditText) findViewById(R.id.price);
        description=(Button) findViewById(R.id.description);
        description.setOnClickListener(this);
    }
    String getItem="";
    Button image;
    int i=0;
    String filename="";
    public void processupload(Uri filepath,Question question)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        String email=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference=storageReference.child("image/"+"quiz/"+email+"/"+subject.getText().toString()+"/"+System.currentTimeMillis()+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                  question.setImage_url(uri.toString());

                                pd.dismiss();




                            }


                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
    }

    ImageButton add_picture;

    @Override
    public void onClick(View v) {

        if(v==show){
            Intent intent=new Intent(this, Quiz_activity.class);
           startActivity(intent);
        }
        if(v==addquiz){
            try {

                String sub = subject.getText().toString();
                if (!sub.equals("")) {
                    quiz.email = "a";
                    quiz.pass = password.getText().toString();
                    int p = Integer.valueOf(price.getText().toString());
                    quiz.price = p;
                    SharedPreferences sp = getSharedPreferences("email", 0);
                    quiz.email = sp.getString("email", null);
                    if (!subjects.contains(sub)) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.subject);
                        databaseReference.child(sub).setValue("");
                    }
                    quiz.type = subject.getText().toString();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Quiz");
                    reference.child(quiz.email).child(quiz.type).setValue(quiz);
                    Toast.makeText(Open_Quiz.this, "quiz", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=getSharedPreferences("delete",0);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    Strings con=new Strings();
                    Date date= Calendar.getInstance().getTime();
                    Gson gson=new Gson();
                    String d=gson.toJson(date);
                    editor.putString(con.Quiz+" "+quiz.email+quiz.type,d);

                    Intent intent = new Intent(Open_Quiz.this, Teacher_home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Open_Quiz.this, "pick a subject", Toast.LENGTH_SHORT).show();



                }



            }
            catch (Exception e){
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }

        }
        if(v==updateBUTTON){
            quiz.description=updateEDITTEXT.getText().toString();
            d.dismiss();
        }


           if(v==description){

           }
    }
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
    OptionsQ op;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView==sw){
            if (isChecked){
                TV.setText("edit answers for question");
                a=true;
            }
            else{
                TV.setText("question with custom answers");
                a=false;
            }
        }
    }
    public void createanswerdislog(){
        d=new Dialog(this);
        d.setContentView(R.layout.add_answer);
        d.setCancelable(false);
        lv=(ListView)d.findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        save2=(Button)d.findViewById(R.id.save);
        save2.setOnClickListener(this);
        ans=(EditText)d.findViewById(R.id.addans);
        adda=(Button)d.findViewById(R.id.adda);
        answe=new ArrayList<String>();
        adda.setOnClickListener(this);
        d.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        for (int i=0;i<answe.size();i++){
            if (item.equals(answe.get(i))){
                answe.remove(i);
                i=answe.size();
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, answe);

                lv.setAdapter(dataAdapter);
            }
        }
    }
}