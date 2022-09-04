package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.School;
import com.example.dreamfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Updae_Adapter extends ArrayAdapter<School> {
    public Updae_Adapter(@NonNull Context context, ArrayList<School> arrayList) {
        super(context, R.layout.subject,R.id.text3, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        School school=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject, parent, false);
        }

        CircleImageView imageView= convertView.findViewById(R.id.img);

        TextView textView=(TextView) convertView.findViewById(R.id.subject);

        textView.setText(school.name);
        try {
            Picasso.get().load(school.url).into(imageView);
        }
        catch (Exception e){
            imageView.setImageResource(R.drawable.profile);
        }







        return super.getView(position,convertView, parent);
    }
}
