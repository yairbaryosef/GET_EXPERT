package com.example.dreamfood.PresentaionLayer.Materials.summary;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.R;
import com.example.dreamfood.Teacher_Controller.teacher_layout;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class Add_summary extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summary);

        initWidgets();
        init_Subjects();
    }
    ArrayList<String> subjects;
    /*
    init subjects
     */
    private void init_Subjects(){
        subjects=new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.subject);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sub:snapshot.getChildren()){
                    subjects.add(sub.getKey());

                }
                ArrayAdapter<String> adapterItems=new ArrayAdapter<String>(Add_summary.this,R.layout.list_item,subjects);
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
    String getItem="";

    AutoCompleteTextView subject;
    TextInputEditText name_sum,price;
    Button save,upload;

    boolean is_pdf_upload;
  TextView add_file;
    // init all widgets
    public void initWidgets(){
      name_sum=findViewById(R.id.name);
      price=findViewById(R.id.price);
      subject=findViewById(R.id.subject);
     upload=findViewById(R.id.upload);
     upload.setOnClickListener(this);
     save=findViewById(R.id.save);
     is_pdf_upload=false;
     add_file=findViewById(R.id.add_file);
     save.setOnClickListener(this);
    }
Summary summary=new Summary();
    Uri filepath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            upload.setBackgroundResource(R.drawable.cancel);
            add_file.setText(filepath.getLastPathSegment()+".pdf");
            is_pdf_upload=true;

        }
    }
    @Override
    public void onClick(View v) {
        if(v==save){
            if(!is_pdf_upload){
                Toast.makeText(this, "upload file", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    double cost=Double.valueOf( price.getText().toString());
                    if(name_sum.getText().length()>0){
                        processupload(filepath);
                    }
                    else{
                        Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(this, "enter valid price", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (v == upload) {
            if (is_pdf_upload) {
              filepath=Uri.EMPTY;
                add_file.setText("add file");
              is_pdf_upload=false;
              upload.setBackgroundResource(R.drawable.upload_file);
            } else {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Pdf Files"), 101);
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
        }




    }
    Strings con=new Strings();
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        String e=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference;
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        reference=storageReference.child("summary/"+e+"/"+ subject.getText().toString()+"/"+name_sum.getText().toString()+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Summary summary=new Summary();
                        String e=getSharedPreferences("email",0).getString("email",null);
                        summary.url=uri.toString();
                        summary.email=e;
                        summary.name=name_sum.getText().toString();
                        SharedPreferences sp=getSharedPreferences(con.teacher,0);
                        Gson gson=new Gson();
                        Teacher teacher=gson.fromJson(sp.getString(con.teacher,null),Teacher.class);
                        teacher.summaries.add(summary);
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.summary).child(e).child(summary.name);
                        databaseReference.setValue(summary);
                        databaseReference=FirebaseDatabase.getInstance().getReference(con.teacher).child(e);
                        databaseReference.setValue(teacher);
                        pd.dismiss();
                        Intent intent=new Intent(Add_summary.this, teacher_layout.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                pd.setMessage("Uploaded :"+(int)percent+"%");
            }
        });
    }

}