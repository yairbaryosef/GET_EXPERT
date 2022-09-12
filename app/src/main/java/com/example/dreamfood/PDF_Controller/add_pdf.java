package com.example.dreamfood.PDF_Controller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.Materials.Test.Open_Test;
import com.example.dreamfood.R;
import com.example.dreamfood.Student_Controller.scrolling_activity;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Hashtable;

public class add_pdf extends AppCompatActivity {
    ImageView imagebrowse,imageupload,filelogo,cancelfile;
    Uri filepath;
    String email,subject;
    EditText filetitle;
    Hashtable<String,Teacher> teacherHashtable=new Hashtable<String,Teacher>();
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email=getIntent().getStringExtra("email");
        subject=getIntent().getStringExtra("subject");
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("tests");
        DatabaseReference d= FirebaseDatabase.getInstance().getReference("teacher");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    teacherHashtable.put(dataSnapshot.getKey(),dataSnapshot.getValue(Teacher.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        filetitle=(EditText) findViewById(R.id.filetitle);

        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);


        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });


        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupload(filepath);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            filelogo.setVisibility(View.VISIBLE);
            cancelfile.setVisibility(View.VISIBLE);
            imagebrowse.setVisibility(View.INVISIBLE);
        }
    }

   private boolean a=true;
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        String e=getSharedPreferences("email",0).getString("email",null);

        final StorageReference reference=storageReference.child("tests/"+e+"_"+System.currentTimeMillis()+ "_"+subject+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                            if(email.equals("")) {
                                SharedPreferences sp = getSharedPreferences("pdf", 0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("title", filetitle.getText().toString());
                                editor.putString("uri", uri.toString());
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");

                                Intent intent = new Intent(add_pdf.this, Open_Test.class);
                                startActivity(intent);
                            }
                            else if(email.equals("checking")){


                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");

                                Intent intent = new Intent(add_pdf.this, Check_pdf.class);
                                intent.putExtra("title", filetitle.getText().toString());
                                intent.putExtra("uri", uri.toString());
                                intent.putExtra("student",getIntent().getStringExtra("student"));
                                startActivity(intent);
                            }
                            else if(email.equals("summary")){
                                Summary summary=new Summary();
                                String e=getSharedPreferences("email",0).getString("email",null);
                                summary.url=uri.toString();
                                summary.email=e;
                                summary.name=filetitle.getText().toString();

                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.summary).child(e).child(summary.name);
                                databaseReference.setValue(summary);
                                pd.dismiss();
                            }
                            else{
                                DatabaseReference data=FirebaseDatabase.getInstance().getReference("teacher");
                                fileinfomodel fileinfomodel=new fileinfomodel();
                                fileinfomodel.fileurl= uri.toString();
                                fileinfomodel.subject=subject;
                                fileinfomodel.filename=filetitle.getText().toString();
                                fileinfomodel.teacherEmail=getIntent().getStringExtra("student");
                                Teacher teacher=teacherHashtable.get(email);
                                teacher.tests.add(fileinfomodel);
                                data.child(email).setValue(teacher);
                                Intent intent=new Intent(add_pdf.this, scrolling_activity.class);
                                startActivity(intent);
                                Toast.makeText(add_pdf.this, "done", Toast.LENGTH_SHORT).show();
                            }
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
    Strings constants=new Strings();
}