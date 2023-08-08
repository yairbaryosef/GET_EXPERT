package com.example.dreamfood.PresentaionLayer.Materials.Record;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Recordings_Adapter extends ArrayAdapter<video> {

    public Recordings_Adapter(@NonNull Context context, ArrayList<video> arrayList) {
        super(context, R.layout.pick_materials,R.id.text3, arrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        video record=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recording_adapter, parent, false);
        }
        ImageView imageView=convertView.findViewById(R.id.image);
        try {
            Picasso.get().load(record.urlImage).into(imageView);
        }
       catch (Exception e){

       }
        TextView textView=convertView.findViewById(R.id.text);
        textView.setText(record.subject);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==imageView){
                    Dialog dialog=new Dialog(getContext());
                    dialog.setContentView(R.layout.activity_video);
                    VideoView video=dialog.findViewById(R.id.video);
                    video.setVideoURI(Uri.parse(record.urlVideo));
                    video.start();
                    dialog.show();
                }
            }
        });
        return super.getView(position, convertView, parent);
    }
}
