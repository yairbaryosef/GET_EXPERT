package com.example.dreamfood.Student_Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.Updae_Adapter;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.School;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.Get_Started_Student;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_details extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
Button chat,recording,update,deal,meetings,search;
ArrayList<School> schools;
Strings con=new Strings();
Student student;
String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
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
        chat=(Button) findViewById(R.id.chat);
        chat.setOnClickListener(this);

        search=(Button) findViewById(R.id.search);
        search.setOnClickListener(this);
        update=(Button) findViewById(R.id.upload);
        update.setOnClickListener(this);
        recording=(Button) findViewById(R.id.record);
        recording.setOnClickListener(this);
        meetings=(Button) findViewById(R.id.meeting);
        meetings.setOnClickListener(this);
        deal=findViewById(R.id.deal);
        deal.setOnClickListener(this);
        schools=new ArrayList<>();
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
    EditText name;
    Button save,upload;
    CircleImageView image;
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        final StorageReference reference=storageReference.child("image/"+name.getText().toString()+".png");
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
                                Toast.makeText(Student_details.this, "success", Toast.LENGTH_SHORT).show();
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
    Dialog d;
    ListView list;
    public void Show_Meeting_List(){
        d=new Dialog(this);
        d.setContentView(R.layout.videos_list);
       list=d.findViewById(R.id.list);
       ArrayList<String> arrayList=new ArrayList<>();
       for(Meeting m:student.meetings){
           arrayList.add(m.type+" "+m.startdate);
       }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        list.setAdapter(arrayAdapter);
        d.show();
    }
    @Override
    public void onClick(View v) {
        if(v==meetings){
          Show_Meeting_List();
        }
        if(v==save){
            try {
                String Uni=name.getText().toString();
                if(Uni.length()>0) {
                    if(!schools.contains(Uni)) {
                        processupload(Uri.parse(school.url));

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
                        student.setUni(item);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.student);
                        databaseReference.child(key).setValue(student);
                        Toast.makeText(Student_details.this, "success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(this, "update all details", Toast.LENGTH_SHORT).show();
            }
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
        if(v==chat){
            Intent intent=new Intent(this, OpenChat.class);
            intent.putExtra("job","teacher");
            startActivity(intent);
        }
        if(v==recording){
            Intent intent=new Intent(this, Show_Recordings.class);
            startActivity(intent);
        }
        if(v==deal){
            Intent intent=new Intent(this, Add_Deal_For_Students.class);
            startActivity(intent);
        }
        if(v==search){
            Intent intent=new Intent(this, Get_Started_Student.class);
            startActivity(intent);
        }
    }
    String item="";
    School school=new School();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item=schools.get(position).name;
        Toast.makeText(this, "School:"+item, Toast.LENGTH_SHORT).show();
        school.name=item;
    }

    ListView lv;
    boolean a=false;
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

        Updae_Adapter updae_adapter=new Updae_Adapter(Student_details.this,schools);
        lv.setAdapter(updae_adapter);
        lv.setOnItemClickListener(this);
        d.show();
    }
}