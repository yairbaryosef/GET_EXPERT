package com.example.dreamfood.Materials.Record;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.R;

public class Add_Recording extends AppCompatActivity implements View.OnClickListener {
    Button add,restart,picture,save;
    ListView list;
    Recording_class recording_class=new Recording_class();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        initViews();
    }
    private void initViews() {
        add=findViewById(R.id.add);
        list=findViewById(R.id.list);
        add.setOnClickListener(this);
        restart=findViewById(R.id.restart);
        restart.setOnClickListener(this);
        picture=findViewById(R.id.picture);
        picture.setOnClickListener(this);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==restart){
            recording_class=new Recording_class();
            list.setAdapter(null);
        }
        if(v==add){
            Add_Video_Dialog();
        }

    }
   Dialog d;
    private void Add_Video_Dialog() {
        d=new Dialog(this);
        d.setContentView(R.layout.activity_add_video);

        d.show();
    }
}
