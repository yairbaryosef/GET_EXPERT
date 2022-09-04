package com.example.dreamfood;

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void message(Context context, String meesage){
        Toast.makeText(context, meesage, Toast.LENGTH_SHORT).show();
    }
}
