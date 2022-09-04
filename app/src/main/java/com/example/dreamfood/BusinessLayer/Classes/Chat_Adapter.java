package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;

import java.util.ArrayList;

public class Chat_Adapter extends ArrayAdapter<Messages>  {

   String sender;
    public Chat_Adapter(@NonNull Context context, ArrayList<Messages> arrayList,String sender) {

        super(context, R.layout.senderchatlayout, R.id.text3, arrayList);
        this.sender=sender;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Messages adapterSubject=getItem(position);
        if(convertView==null) {

            if(adapterSubject.senderId.equals(sender))
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.senderchatlayout, parent, false);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.recieverchatlayout, parent, false);
        }

        TextView message=(TextView)convertView.findViewById(R.id.sendermessage);

        TextView date=(TextView) convertView.findViewById(R.id.timeofmessage);
TextView textView=(TextView)convertView.findViewById(R.id.text3);
textView.setText(null);
        message.setText(adapterSubject.getMessage());
        date.setText(adapterSubject.getCurrenttime());

        return super.getView(position,convertView, parent);
    }


}
