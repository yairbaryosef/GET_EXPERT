package com.example.dreamfood.Teacher_Controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.Updae_Adapter;
import com.example.dreamfood.BusinessLayer.School;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_details extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
   Button update,upload_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);
       update=findViewById(R.id.update);

       update.setOnClickListener(this);
       upload_details=findViewById(R.id.Upload_box);
       upload_details.setOnClickListener(this);
       Update_Teacher();
       Update_Schools();
      arrayList=new ArrayList<>();
    }
    Dialog d;
    ListView lv;
    Button upload,save;
    ImageView image;
    EditText name;
    Strings con=new Strings();
    ArrayList<String> schools;
    ArrayList<School> arrayList;
    Teacher teacher;
    String key;
    public void Update_Teacher(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.teacher);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(email.equals(dataSnapshot.getKey())||email.equals(dataSnapshot.getValue(Student.class).getEmail())){
                        teacher=dataSnapshot.getValue(Teacher.class);
                        key=dataSnapshot.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void  Update_Schools(){
        schools=new ArrayList<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.school);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    schools.add(dataSnapshot.getValue(School.class).name);
                    arrayList.add(dataSnapshot.getValue(School.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void UpdateDialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.update_student_details);
        lv=(ListView) d.findViewById(R.id.list);
        upload=d.findViewById(R.id.upload);
        save=d.findViewById(R.id.save);
        name=d.findViewById(R.id.name);
        image=d.findViewById(R.id.image);
        save.setOnClickListener(this);
        upload.setOnClickListener(this);

        Updae_Adapter updae_adapter=new Updae_Adapter(Teacher_details.this,arrayList);
        lv.setAdapter(updae_adapter);
        lv.setOnItemClickListener(this);
        d.show();
    }
    String item="";
    private static final int PICK_IM=1;
    Uri uri;
    School school;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IM&&resultCode==RESULT_OK){
            try {
                uri=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image.setImageBitmap(bitmap);
                school=new School();
                school.name=name.getText().toString();
                school.url=uri.toString();

            }
            catch (Exception e){

            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v==upload_details){
            Intent intent=new Intent(this,Teacher_Upload_Box.class);
            startActivity(intent);
        }
        if(v==upload){
            Intent gallery=new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery,"Select picture"),PICK_IM);
        }
        if(v==update){
              UpdateDialog();
        }
        if(v==save){
            try {
                String Uni=name.getText().toString();
                if(Uni.length()>0) {
                    if(!schools.contains(Uni)) {
                        School school = new School();
                        school.name = name.getText().toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.school);
                        databaseReference.child(school.name).setValue(school);
                        teacher.setUni(school.name);
                        databaseReference = FirebaseDatabase.getInstance().getReference(con.teacher);
                        databaseReference.child(key).setValue(teacher);
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(this, "school is already exist", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(item.equals("")) {
                        Toast.makeText(this, "enter school or choose school", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        teacher.setUni(item);
                        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference(con.teacher);
                        databaseReference.child(key).setValue(teacher);
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(this, "update all details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item=arrayList.get(position).name;
        Toast.makeText(this, "School:"+item, Toast.LENGTH_SHORT).show();
    }
}