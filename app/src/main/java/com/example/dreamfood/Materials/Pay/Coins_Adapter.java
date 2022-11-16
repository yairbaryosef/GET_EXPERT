package com.example.dreamfood.Materials.Pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Coins_Adapter extends ArrayAdapter<Coin> {
    public Coins_Adapter(@NonNull Context context, ArrayList<Coin> arrayList) {
        super(context, R.layout.subject,R.id.text3, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Coin coin=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject, parent, false);
        }

        CircleImageView imageView= convertView.findViewById(R.id.img);

        TextView textView=(TextView) convertView.findViewById(R.id.subject);

        textView.setText(coin.email);
        try {
            if(!coin.url.equals("")) {
                Picasso.get().load(coin.url).into(imageView);
            }
            else{
                imageView.setImageResource(R.drawable.coin);
            }
        }
        catch (Exception e){
            imageView.setImageResource(R.drawable.coin);
        }







        return super.getView(position,convertView, parent);
    }
}
