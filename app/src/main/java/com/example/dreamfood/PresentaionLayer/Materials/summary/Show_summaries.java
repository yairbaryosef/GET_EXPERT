package com.example.dreamfood.PresentaionLayer.Materials.summary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.BusinessLayer.summary.Summary_Adapter;
import com.example.dreamfood.R;

public class Show_summaries extends AppCompatActivity implements View.OnClickListener {
    Button back,add_file;
    TextView text;
    String summary_name="";
    ListView list;
    summaries summaries=new summaries();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summary);
     initWidgets();

    }

    EditText name_sum;
    Button add_sum;

    public void initWidgets(){
        back=findViewById(R.id.back);
        text=findViewById(R.id.text);
        list=findViewById(R.id.list);
        back.setOnClickListener(this);
       add_file=findViewById(R.id.add_file);

       add_file.setVisibility(View.INVISIBLE);
        Summary_Adapter summary_adapter=new Summary_Adapter(Show_summaries.this,summaries.summaries);
        list.setAdapter(summary_adapter);

    }
Summary summary=new Summary();
    @Override
    public void onClick(View v) {

        if(v==back){
            finish();
        }


    }




}