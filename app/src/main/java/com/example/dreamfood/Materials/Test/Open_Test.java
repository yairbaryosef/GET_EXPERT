package com.example.dreamfood.Materials.Test;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.fileinfomodel;
import com.example.dreamfood.BusinessLayer.Test;
import com.example.dreamfood.PDF_Controller.add_pdf;
import com.example.dreamfood.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Open_Test extends AppCompatActivity implements View.OnClickListener {
    Button pdf,test1,description,updateBUTTON;
    EditText updateEDITTEXT,password,price,limit;
    TextInputEditText subject;
    fileinfomodel obj=null;
    Dialog d;
    Test test=new Test();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_test);
        subject=(TextInputEditText) findViewById(R.id.subject);
        password=(EditText)findViewById(R.id.pass);
        price=(EditText)findViewById(R.id.price);
        limit=(EditText)findViewById(R.id.limit);
        pdf=(Button) findViewById(R.id.pdf);
        pdf.setOnClickListener(this);
        test1=(Button) findViewById(R.id.addtest);
        test1.setOnClickListener(this);
        description=(Button) findViewById(R.id.description);
        description.setOnClickListener(this);
        SharedPreferences sp=getSharedPreferences("pdf",0);

        String title=sp.getString("title",null);
        if(!Objects.equals(title, "")){
            String uri=sp.getString("uri",null);
            obj=new fileinfomodel(title,uri);
            SharedPreferences sp1 = getSharedPreferences("email", 0);
            String email = sp1.getString("email", null);
            obj.teacherEmail = email;

        }


    }

    public void createdialogdetails()
    {
        d= new Dialog(this);
        d.setContentView(R.layout.description);
        d.setCancelable(true);
        updateEDITTEXT=(EditText) d.findViewById(R.id.updescription);
        updateBUTTON=(Button) d.findViewById(R.id.upd);
        updateBUTTON.setOnClickListener(this);
        d.show();

    }
    @Override
    public void onClick(View v) {
        if(v==pdf){
            Intent intent=new Intent(this, add_pdf.class);
            intent.putExtra("email","");
            startActivity(intent);
        }
        if(v==updateBUTTON){
            test.description=updateEDITTEXT.getText().toString();
            d.dismiss();
        }
        if(v==description){
            createdialogdetails();
        }
        if(v==test1){

          if(price.length()>0&&limit.length()>0&&password.length()>0&&subject.length()>0) {
              if (obj != null) {

                  test.file = obj;
                  try {
                      test.limit=Integer.valueOf(limit.getText().toString());
                      test.price=Integer.valueOf(price.getText().toString());
                      test.subject=subject.getText().toString();
                      test.pass=password.getText().toString();
                      DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Test");
                      reference.child(test.file.teacherEmail).child(test.file.filename + test.file.teacherEmail).setValue(test);
                      Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                  } catch (Exception e) {
                      Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                  }
              } else {
                  Toast.makeText(this, "add pdf file", Toast.LENGTH_SHORT).show();
              }
          }
          else{
              Toast.makeText(this, "missing field", Toast.LENGTH_SHORT).show();
          }
        }
    }
}