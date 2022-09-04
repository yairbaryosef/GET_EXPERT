package com.example.dreamfood.Teacher_Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.R;
import com.example.dreamfood.viewmodel.Add_deal;

public class Subscribes_teacher extends AppCompatActivity implements View.OnClickListener {
  Button add_deal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribes_teacher);
        add_deal=(Button) findViewById(R.id.add_deal);
        add_deal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==add_deal){
            Intent intent=new Intent(this, Add_deal.class);
            startActivity(intent);
        }
    }
}