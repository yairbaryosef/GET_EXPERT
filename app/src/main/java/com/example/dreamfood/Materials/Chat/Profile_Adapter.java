package com.example.dreamfood.Materials.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile_Adapter extends ArrayAdapter<Profile_Adapter.profile> {
    public static class profile{
        public String resource;
        public String name;
        public profile(String resource,String name){
           this.name=name;
           this.resource=resource;
        }
    }
    public Profile_Adapter(@NonNull Context context, ArrayList<profile> arrayList) {
        super(context, R.layout.subject,R.id.text3, arrayList);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        profile profile=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject, parent, false);
        }

        ImageView imageView=(ImageView) convertView.findViewById(R.id.img);

        TextView textView=(TextView) convertView.findViewById(R.id.subject);

        textView.setText(profile.name);
        try {
            if(profile.resource.equals("")){
                imageView.setImageResource(R.drawable.profile);
            }
            else {
                Picasso.get().load(profile.resource).into(imageView);
            }
        }
        catch (Exception e){
            imageView.setImageResource(R.drawable.profile);
        }


        return super.getView(position,convertView, parent);
    }

}
