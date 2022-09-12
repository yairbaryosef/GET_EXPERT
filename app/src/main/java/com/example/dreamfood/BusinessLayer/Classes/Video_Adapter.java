package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;

import java.util.ArrayList;

public class Video_Adapter  extends ArrayAdapter<video> {
    public Video_Adapter(@NonNull Context context, ArrayList<video> arrayList) {
        super(context, R.layout.video,R.id.text3, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        video adapterSubject=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video, parent, false);
        }
        MediaController mediaController=new MediaController(getContext());
        VideoView videoView=(VideoView) convertView.findViewById(R.id.video);

        TextView textView=(TextView) convertView.findViewById(R.id.text);
        textView.setText(adapterSubject.subject);
        videoView.setVideoURI(Uri.parse(adapterSubject.urlVideo));
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        return super.getView(position,convertView, parent);
    }

}
