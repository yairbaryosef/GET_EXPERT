package com.example.dreamfood.BusinessLayer.Profile;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.PresentaionLayer.Materials.Chat.Profile_Adapter;
import com.example.dreamfood.R;

import java.util.ArrayList;

public class Posts_Adapter extends ArrayAdapter<Profile_Adapter.profile> {
    /*
    constructor 1
     */
    public Posts_Adapter(@NonNull Context context, ArrayList<Profile_Adapter.profile> arrayList) {
        super(context, R.layout.senderchatlayout,R.id.text3, arrayList);
    }
    /*
      init posts views
       */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Profile_Adapter.profile post=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.senderchatlayout, parent, false);
        }
        TextView message=convertView.findViewById(R.id.message);
        TextView time=convertView.findViewById(R.id.time);
        message.setText(post.resource);
        String form = String.valueOf(DateFormat.format("MM-dd-yyyy", post.date)) + " " + (String) DateFormat.format("hh:mm aa", post.date);


        time.setText(form);
        return super.getView(position, convertView, parent);
    }
}
