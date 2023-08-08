package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;

import java.util.ArrayList;

public class MessagesAdapter extends ArrayAdapter<Messages> {

    public MessagesAdapter(@NonNull Context context, ArrayList<Messages> arrayList,String user) {
        super(context, R.layout.senderchatlayout,R.id.text3, arrayList);
        this.user=user;
    }
    String user;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Messages message=getItem(position);
        if(convertView==null) {
            if(user.equals(message.getSenderId())) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.senderchatlayout, parent, false);
            }
            else{
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.recieverchatlayout, parent, false);
            }
        }
        TextView time=convertView.findViewById(R.id.time);
        TextView text=convertView.findViewById(R.id.message);
        text.setText(message.getMessage());
        time.setText(message.getCurrenttime());
        return super.getView(position,convertView, parent);
    }


}
