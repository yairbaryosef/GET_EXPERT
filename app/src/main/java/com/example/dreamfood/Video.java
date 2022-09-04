package com.example.dreamfood;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video extends AppCompatActivity {
VideoView videoView;
MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mediaController=new MediaController(this);
        videoView=findViewById(R.id.video);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        Uri uri=Uri.parse(getIntent().getStringExtra("uri"));
        videoView.setVideoURI(uri);
        videoView.start();
    }
}