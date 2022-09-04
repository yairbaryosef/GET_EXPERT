package com.example.dreamfood.BusinessLayer.Classes;

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

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<AdapterSubject>  {


    public MyAdapter(@NonNull Context context, ArrayList<AdapterSubject> arrayList) {
        super(context,R.layout.subject,R.id.text3, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AdapterSubject adapterSubject=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject, parent, false);
        }

        ImageView imageView=(ImageView) convertView.findViewById(R.id.img);

        TextView textView=(TextView) convertView.findViewById(R.id.subject);

        textView.setText(adapterSubject.getSubject());

        return super.getView(position,convertView, parent);
    }


}
