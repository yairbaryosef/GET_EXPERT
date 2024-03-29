package com.example.dreamfood.Teacher_Controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Dialogs.Teacher_Dialogs;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments.init_teacher_Fragment;
import com.example.dreamfood.PresentaionLayer.Materials.Pay.Add_Coin_Activity;
import com.example.dreamfood.R;
import com.example.dreamfood.open_course;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class teacher_layout extends AppCompatActivity implements View.OnClickListener {
Button add,course,details,sub,delete,update;
Strings con=new Strings();
JSONObject jsonObject;
CircleImageView image,profile_teacher;
String email="";
FirebaseDatabase firebaseDatabase;
Teacher teacher=new Teacher();
    private ArrayList<String> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_layout);
        init();
        email=getIntent().getStringExtra("email");


        initNavigation();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                teacher=snapshot.getValue(Teacher.class);
                Toast.makeText(teacher_layout.this, teacher.getEmail(), Toast.LENGTH_SHORT).show();
                 Picasso.get().load(teacher.profile_url).into(profile_teacher);
                 Gson gson=new Gson();
                 String teacher_json=gson.toJson(teacher);
                 SharedPreferences sharedPreferences=getSharedPreferences(con.teacher,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(con.teacher,teacher_json);
              //  Toast.makeText(teacher_layout.this, teacher_json, Toast.LENGTH_SHORT).show();
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*
    init the fragment for uploading details
     */
    public void initFrame(){
               Fragment selectedFragment = null;
               subjects=new ArrayList<>();
               selectedFragment=new init_teacher_Fragment(email,teacher,subjects);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


    }
    DrawerLayout drawer;
    NavigationView navigationView;
     /*
     init the side navigation bar
      */
    public void initNavigation(){
        drawer = findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.menu);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().toString().equals("add coin")){
                    Intent intent=new Intent(teacher_layout.this, Add_Coin_Activity.class);
                    Gson gson=new Gson();
                    intent.putExtra(con.teacher,gson.toJson(teacher));
                    startActivity(intent);
                }
                else if(item.getTitle().toString().equals("add message")){
                    Teacher_Dialogs teacher_dialogs=new Teacher_Dialogs(teacher_layout.this,teacher,email);
                    teacher_dialogs.add_Message();
                }
                else if(item.getTitle().toString().equals("my subjects")){
                    initFrame();
                    Teacher_Dialogs teacher_dialogs=new Teacher_Dialogs(teacher_layout.this,teacher,email,subjects);
                    teacher_dialogs.subjects();
                }
                else if(item.getTitle().toString().equals("my ratings")){

                }
                return false;
            }
        });

    }
    /*
    init activity
     */
    public void init(){
        add=(Button) findViewById(R.id.add);
        add.setOnClickListener(this);
       update=findViewById(R.id.update);
       update.setOnClickListener(this);
       delete=findViewById(R.id.delete);
       delete.setOnClickListener(this);
        sub=(Button) findViewById(R.id.sub);
        sub.setOnClickListener(this);
        profile_teacher= findViewById(R.id.textView);
        profile_teacher.setOnClickListener(this);

       profile_teacher.setImageResource(R.drawable.profile);

        details=(Button) findViewById(R.id.details);
        details.setOnClickListener(this);
        SharedPreferences sp=getSharedPreferences("course",0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("subject","");

        editor.commit();
    }
    Dialog d;
    Button upload,save;
    /*
    upload profile picture dialog
     */
    public void UpdateDialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.update_student_details);

        upload=d.findViewById(R.id.upload);
        save=d.findViewById(R.id.save);

        image=d.findViewById(R.id.image);
        save.setOnClickListener(this);
        upload.setOnClickListener(this);
        d.show();
    }
    private static final int PICK_IM=1;
    Uri uri;
    /*
    on profile picture pick
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IM&&resultCode==RESULT_OK){
            try {
                uri=data.getData();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image.setImageBitmap(bitmap);


            }
            catch (Exception e){

            }
        }
    }
    /*
    upload profile picture to database
     */
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        final StorageReference reference=storageReference.child("image/"+email+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                teacher.profile_url=uri.toString();
                               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(con.teacher);
                                databaseReference.child(email).setValue(teacher);
                                Toast.makeText(teacher_layout.this, "success", Toast.LENGTH_SHORT).show();
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
        if(v==delete){
            Intent intent=new Intent(this,Teacher_home.class);
            intent.putExtra("type","delete");
            startActivity(intent);
        }
        if(v==save){
            processupload(uri);
            d.dismiss();
        }
        if(v==upload){
            Intent gallery=new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery,"Select picture"),PICK_IM);
        }
        if(v==profile_teacher){
            UpdateDialog();
        }
        if(v==sub){
            Intent intent=new Intent(this, Subscribes_teacher.class);
            startActivity(intent);
        }
        if(v==details){

            Intent intent=new Intent(this, Teacher_details.class);
            startActivity(intent);
        }
        if(v==add){
            Intent intent=new Intent(this, Teacher_home.class);
            intent.putExtra("type","add");
            startActivity(intent);
        }
        if(v==course){
            FileOutputStream fos = null;
            try {
                Quiz q=new Quiz();
                q.type="";
                fos = this.openFileOutput("quiz", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(q);
                os.close();
                fos.close();

                Intent intent=new Intent(this, open_course.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}