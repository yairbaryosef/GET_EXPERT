package com.example.dreamfood.Materials.Record;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Question;
import com.example.dreamfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_Recording extends AppCompatActivity implements View.OnClickListener {
    Button add,restart,picture,save;
    ListView list;
    Recording_class recording_class=new Recording_class();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        initViews();
    }
    private void initViews() {
        add=findViewById(R.id.add);
        list=findViewById(R.id.list);
        add.setOnClickListener(this);
        restart=findViewById(R.id.restart);
        restart.setOnClickListener(this);
        picture=findViewById(R.id.picture);
        picture.setOnClickListener(this);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==restart){
            recording_class=new Recording_class();
            list.setAdapter(null);
        }
        if(v==add){
            Add_Video_Dialog();
        }

    }
   Dialog d;
    AutoCompleteTextView subject;
    TextInputEditText price;
    Button add_video,save_record;
    VideoView video;
    /*
    * create a dialog that add a record to the recordings.
     */
    private void Add_Video_Dialog() {
        d=new Dialog(this);
        d.setContentView(R.layout.activity_add_video);
        subject=d.findViewById(R.id.subject);
        price=d.findViewById(R.id.price);
        add_video=d.findViewById(R.id.add_video);
        video=d.findViewById(R.id.video);
        save_record=d.findViewById(R.id.add);
        Buttons_Listeners();
        d.show();
    }
    /*
    * init listeners for buttons of add video dialog
     */
    private void Buttons_Listeners() {
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==add_video){
                    chooseVideo();
                }
            }
        });
    }
    public void chooseVideo(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Pick_VIDEO);
    }
    Uri uri;
    private static final int Pick_VIDEO=1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_VIDEO||data!=null||data.getData()!=null){
            uri=data.getData();
            video.setVideoURI(uri);
        }
    }
    public void processupload(Uri filepath, Question question)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        String email=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference=storageReference.child("videos/"+"recordings/"+email+"/"+subject.getText().toString()+"/"+System.currentTimeMillis()+".png");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

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
}
