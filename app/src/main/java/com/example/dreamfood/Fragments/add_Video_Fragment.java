package com.example.dreamfood.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.R;
import com.example.dreamfood.Materials.Record.Recording_class;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add_Video_Fragment extends Fragment {
    View v;
    Recording_class recording_class;
   EditText subject,price;
   Button add,add_video;
   VideoView video;
   Strings con=new Strings();
   MediaController mediaController;
   String email,sub;
    public add_Video_Fragment(Recording_class recording_class,String email,String sub){
      this.recording_class=recording_class;
      this.email=email;
      this.sub=sub;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.add_video, container, false);
        video=v.findViewById(R.id.video);
        subject=v.findViewById(R.id.subject);
        price=v.findViewById(R.id.price);
        add=v.findViewById(R.id.add);
        mediaController=new MediaController(getContext());
        add_video=v.findViewById(R.id.add_video);
        video.setMediaController(mediaController);
        video.start();
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==add_video){
                  chooseVideo();
                }
            }
        });
       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (view==add) {
                   Upload_Video();
               }
           }
       });
        return v;
    }
    Uri uri;
   private static final int Pick_VIDEO=1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_VIDEO||data!=null||data.getData()!=null){
            uri=data.getData();
            video.setVideoURI(uri);
            Toast.makeText(getContext(), "in", Toast.LENGTH_SHORT).show();
        }
    }
    public void chooseVideo(){
        Intent intent=new Intent();
     intent.setType("video/*");
     intent.setAction(intent.ACTION_GET_CONTENT);
     startActivityForResult(intent,Pick_VIDEO);
    }
    String filepath;
    public void Upload_Video(){
          if(uri!=null&&price.getText().length()!=0&&subject.getText().length()!=0){
              StorageReference storageReference= FirebaseStorage.getInstance().getReference(con.video);

              StorageReference store=storageReference.child(sub).child(System.currentTimeMillis()+"."+getExt(uri));
              UploadTask uploadTask=store.putFile(uri);
              Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                  @Override
                  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                      if(!task.isSuccessful()){
                          throw task.getException();
                      }
                      return store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              filepath=uri.toString();
                          }
                      });
                  }
              }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                  @Override
                  public void onComplete(@NonNull Task<Uri> task) {
                      DatabaseReference reference= FirebaseDatabase.getInstance().getReference(con.recording);
                      video video=new video();
                      video.urlVideo=filepath;
                      try {

                          video.subject=subject.getText().toString();
                          video.price = Double.valueOf(price.getText().toString());
                          recording_class.videoArrayList.add(video);
                          recording_class.videos.add(sub);
                          reference.child(email).child(sub).setValue(recording_class);
                      }
                      catch (Exception e){
                          Toast.makeText(getContext(), "price is a number", Toast.LENGTH_SHORT).show();
                      }
                  }

              });

          }
          else
              Toast.makeText(getContext(), "upload video or enter price or subject", Toast.LENGTH_SHORT).show();
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
