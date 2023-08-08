package com.example.dreamfood.Student_Controller;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.example.dreamfood.BusinessLayer.Classes.Meeting_Adpter.Meetings_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.Updae_Adapter;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.School;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.summary.Summaries_Adapter;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.OpenChat;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Show_Recordings;
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
ImageButton chat,recording,update,deal,meetings,search;
ArrayList<School> schools;
Strings con=new Strings();
Student student;
String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);
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
    /*
    init views
     */
    public void initWidgets(){
        chat= findViewById(R.id.chat);
        chat.setOnClickListener(this);

        search= findViewById(R.id.search);
        search.setOnClickListener(this);
        update= findViewById(R.id.upload);
        update.setOnClickListener(this);
        recording= findViewById(R.id.record);
        recording.setOnClickListener(this);
        meetings= findViewById(R.id.meeting);
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
    /*
    show meetings list
     */
    public void Show_Meeting_List(String item){
        d=new Dialog(this);
        d.setContentView(R.layout.videos_list);
        SearchView searchView=d.findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (item.equals(con.Meeting)) {
                    ArrayList<Meeting> array = new ArrayList<Meeting>();

                    for (Meeting adapterSubject : student.meetings) {
                        if (adapterSubject.email.toLowerCase().contains(newText.toLowerCase()) && !adapterSubject.equals("choose")) {
                            array.add(adapterSubject);
                        }
                    }
                    Meetings_Adapter dataAdapter = new Meetings_Adapter(Student_details.this, array, 0);
                    list.setAdapter(dataAdapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });

                }

            else {
                    ArrayList<Summary> array = new ArrayList<Summary>();

                    for (Summary adapterSubject : student.summaries) {
                        if (adapterSubject.email.toLowerCase().contains(newText.toLowerCase()) && !adapterSubject.equals("choose")) {
                            array.add(adapterSubject);
                        }
                    }
                    Summaries_Adapter dataAdapter = new Summaries_Adapter(Student_details.this, array, 0);
                    list.setAdapter(dataAdapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                }
            return false;
            }


        });


        list=d.findViewById(R.id.list);

       if(item.equals(con.Meeting)) {
           Meetings_Adapter arrayAdapter = new Meetings_Adapter(this, student.meetings, 0);
           list.setAdapter(arrayAdapter);
       }
       else {
           Summaries_Adapter arrayAdapter = new Summaries_Adapter(this, student.summaries, 0);
           list.setAdapter(arrayAdapter);
       }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        d.show();
    }
    @Override
    public void onClick(View v) {
        if(v==meetings){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

            Show_Meeting_List(con.Meeting);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

            Show_Meeting_List(con.summary);
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