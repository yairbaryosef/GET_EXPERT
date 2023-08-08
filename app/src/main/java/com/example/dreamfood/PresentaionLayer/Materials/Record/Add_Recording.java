package com.example.dreamfood.PresentaionLayer.Materials.Record;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Add_Recording extends AppCompatActivity implements View.OnClickListener {

    Button add,restart,picture,save;
    ListView list;
    int pos=0;
    Dialog d;
    AutoCompleteTextView subject;
    TextInputEditText price;
    Button add_video,save_record;
    VideoView video;
    Recording_class recording_class=new Recording_class();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        initViews();
    }
    /*
    init views
     */
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
    /*
       onClick listeners
        */
    @Override
    public void onClick(View v) {
        if(v==restart){
            recording_class=new Recording_class();
            list.setAdapter(null);
        }
        if(v==add){
            Add_Video_Dialog();
        }
        if(v==save){
            Intent intent=new Intent(this, Open_Recordings.class);
            Gson gson=new Gson();
            String json=gson.toJson(recording_class);
            intent.putExtra("record",json);
            startActivity(intent);
        }
        if(v==picture){
            choose_record();
        }

    }
    /*
  add picture to question
   */
    ImageButton add_picture;
    public  void Add_Picture_Dialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.image_button);
        add_picture=d.findViewById(R.id.add_picture);
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==add_picture){
                    Intent gallery=new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(gallery,2);
                }
            }
        });

        d.show();

    }

    /*
    select a video to upload a picture
     */
    public void choose_record() {
        d = new Dialog(this);
        d.setContentView(R.layout.list_rating_student_active);
        ListView list = d.findViewById(R.id.list);
        ArrayList<String> qs = new ArrayList<>();
        int i = 0;
        for (video video :recording_class.videoArrayList ) {
            try {


                qs.add(video.subject + " video number" + String.valueOf(i));
            } catch (Exception e) {
                qs.add("video number" + String.valueOf(i));
            }
            i++;
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,qs );
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                d.dismiss();
                Add_Picture_Dialog();
                pos=position;

            }
        });
        Button button=d.findViewById(R.id.save);
        button.setText("update picture");
        button.setVisibility(View.INVISIBLE);
        button.setHeight(0);

        d.show();
    }


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
        save_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==save_record){
                    if(uri.toString().equals("")){
                        Toast.makeText(Add_Recording.this, "choose a video", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        processupload(uri);
                    }
                }
            }
        });
    }
    /*
    * choose video from the device
     */
    public void chooseVideo(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Pick_VIDEO);
    }
    Uri uri;
    private static final int Pick_VIDEO=1;
    /*
    * on choosing video
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_VIDEO||data!=null||data.getData()!=null){
            uri=data.getData();
            video.setVideoURI(uri);
        }
        else if(requestCode==2||data!=null||data.getData()!=null){
            try {


                uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                add_picture.setImageBitmap(bitmap);

                processupload(uri, recording_class.videoArrayList.get(pos));
                d.dismiss();
            }
            catch (Exception e){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processupload(Uri uri, com.example.dreamfood.BusinessLayer.Classes.video video) {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        String email=getSharedPreferences("email",0).getString("email",null);
        final StorageReference reference=storageReference.child("image/"+"record/"+email+"/"+video.subject+"/"+System.currentTimeMillis()+".png");
        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               video.urlImage=uri.toString();
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

    /*
    upload the video
     */
    public void processupload(Uri filepath)
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
                                  com.example.dreamfood.BusinessLayer.Classes.video video=new video();
                                  video.subject=subject.getText().toString();

                                  video.price=Double.valueOf(price.getText().toString());
                                  video.urlVideo=uri.toString();
                                  recording_class.videoArrayList.add(video);
                                  Recordings_Adapter recordings_adapter=new Recordings_Adapter(Add_Recording.this,recording_class.videoArrayList);
                                  list.setAdapter(recordings_adapter);
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
}
