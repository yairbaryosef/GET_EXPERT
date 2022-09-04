package com.example.dreamfood.Student_Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.School;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Materials.Chat.OpenChat;
import com.example.dreamfood.Materials.Record.Show_Recordings;
import com.example.dreamfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Student_home extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {
Dialog d;
CheckBox meet,quiz,test;
Strings con=new Strings();
Button search,s,details,chat,record,update;
String m="false",t="false",q="false";
ArrayList<School> schools;
Student student;
String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        initWidgets();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.student);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(email.equals(dataSnapshot.getKey())||email.equals(dataSnapshot.getValue(Student.class).getEmail())){
                        student=dataSnapshot.getValue(Student.class);
                        key=dataSnapshot.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(con.school);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    schools.add(dataSnapshot.getValue(School.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initWidgets(){
        school=new School();
        s=(Button) findViewById(R.id.search);
        s.setOnClickListener(this);
        schools=new ArrayList<>();
        details=(Button) findViewById(R.id.details);
        details.setOnClickListener(this);
        chat=(Button) findViewById(R.id.chats);
        chat.setOnClickListener(this);
        record=(Button) findViewById(R.id.record);
        record.setOnClickListener(this);
        update=(Button) findViewById(R.id.upload);
        update.setOnClickListener(this);
    }
    public void createaddquestiondislog(){
        d=new Dialog(this);
        d.setContentView(R.layout.search);
        search=(Button)d.findViewById(R.id.search);
        meet=(CheckBox) d.findViewById(R.id.meet);
        meet.setOnCheckedChangeListener(this);
        quiz=(CheckBox) d.findViewById(R.id.quiz);
        quiz.setOnCheckedChangeListener(this);
        test=(CheckBox) d.findViewById(R.id.test);
        test.setOnCheckedChangeListener(this);
        search.setOnClickListener(this);

        d.show();
    }
    ListView lv;
    Button upload,save;
    ImageView image;
    EditText name;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==meet){
            if(isChecked){
                m="true";
            }
            else {
                m="false";
            }
        }
        if(buttonView==test){
            if(isChecked){
                t="true";
            }
            else {
                t="false";
            }
        }
        if(buttonView==quiz){
            if(isChecked){
                q="true";
                Toast.makeText(this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
            }
            else {
                q="false";
            }
        }
    }
   private static final int PICK_IM=1;
   Uri uri;
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




    School school;
    public String getExe(Uri uri){
        ContentResolver c=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(c.getType(uri));
    }
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
       StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        final StorageReference reference=storageReference.child("image/"+System.currentTimeMillis()+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                school.url=uri.toString();

                                school.name = name.getText().toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.school);
                                databaseReference.child(school.name).setValue(school);
                                student.setUni(school.name);
                                databaseReference = FirebaseDatabase.getInstance().getReference(con.student);
                                databaseReference.child(key).setValue(student);
                                Toast.makeText(Student_home.this, "success", Toast.LENGTH_SHORT).show();
                                finish();
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
    @Override
    public void onClick(View v) {

        if(v==record){
            Intent intent=new Intent(this, Show_Recordings.class);
            startActivity(intent);
        }
        if(v==s){
            createaddquestiondislog();
        }
        if(v==search){
            Intent intent=new Intent(this, com.example.dreamfood.Student_Controller.search.class);

                intent.putExtra("meet", m);


            intent.putExtra("test",t);
            intent.putExtra("quiz",q);
            startActivity(intent);
        }
        if(v==details){
            Intent intent=new Intent(this, Student_details.class);
            startActivity(intent);
        }
        if(v==chat){
            Intent intent=new Intent(this, OpenChat.class);
            intent.putExtra("job","teacher");
            startActivity(intent);
        }
    }
   String item="";
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          item=schools.get(position).name;
        Toast.makeText(this, "School:"+item, Toast.LENGTH_SHORT).show();
        school.name=item;
    }
}