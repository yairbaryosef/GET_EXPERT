package com.example.dreamfood.Materials.Record;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import com.example.dreamfood.R;

public class Add_Video extends AppCompatActivity {
    View v;
    Recording_class recording_class;
    EditText subject,price;
    Button add,add_video;
    VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        video=v.findViewById(R.id.video);
        subject=v.findViewById(R.id.subject);
        price=v.findViewById(R.id.price);
        add=v.findViewById(R.id.add);
        add_video=v.findViewById(R.id.add_video);
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==add_video){
                    chooseVideo();
                }
            }
        });
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==add) {
                    Upload_Video();
                }
            }
        });
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
    public void chooseVideo(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Pick_VIDEO);
    }
    public void Upload_Video(){

    }
    private String getExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}