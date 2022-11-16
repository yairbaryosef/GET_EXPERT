package com.example.dreamfood.BusinessLayer.Classes.Rating;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Rating_Adapter extends ArrayAdapter<rate> {

    public Rating_Adapter(@NonNull Context context, ArrayList<rate> arrayList) {
        super(context, R.layout.videos_list,R.id.text3, arrayList);

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        rate rates=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rating_dialog, parent, false);
        }

        TextView rating_text=convertView.findViewById(R.id.rating_text);
        rating_text.setText(rates.name);
        RatingBar ratingBar=convertView.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rates.rate=rating;
            }
        });
        TextInputEditText comments=convertView.findViewById(R.id.comments);
        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  rates.comments=s.toString();
            }
        });
        return  super.getView(position,convertView, parent);
    }
}
