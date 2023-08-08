package com.example.dreamfood.Teacher_Controller;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.List_Activity_With_Search;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.OpenChat;
import com.example.dreamfood.PresentaionLayer.Materials.Meeting.OpenZoom;
import com.example.dreamfood.PresentaionLayer.Materials.Quiz.Add_Quiz;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Add_Recording;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Recording_class;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Recordings;
import com.example.dreamfood.PresentaionLayer.Materials.Test.Open_Test;
import com.example.dreamfood.PresentaionLayer.Materials.summary.Add_summary;
import com.example.dreamfood.Message;
import com.example.dreamfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Teacher_home extends AppCompatActivity implements View.OnClickListener {
ImageButton open,quiz,chat,test,record,summary;
String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        open= findViewById(R.id.openMeet);
        open.setOnClickListener(this);
        try {

            type=getIntent().getStringExtra("type");

        }
        catch (Exception e){

        }
        quiz= findViewById(R.id.quizz);
        quiz.setOnClickListener(this);
        chat= findViewById(R.id.chat);
        chat.setOnClickListener(this);
        test= findViewById(R.id.test);
        test.setOnClickListener(this);
        record=findViewById(R.id.record);
        record.setOnClickListener(this);
        summary=findViewById(R.id.summary);
        summary.setOnClickListener(this);
    }
    Dialog d;
    Button add;
    EditText name;
    public void DialogAddRecord(){
        d=new Dialog(this);
        d.setContentView(R.layout.add_recording);
        name=d.findViewById(R.id.name);
        add=d.findViewById(R.id.add);
        add.setOnClickListener(this);


        d.show();
    }
    Strings con=new Strings();
    @Override
    public void onClick(View v) {
        if(v==summary){
            if(type.equals("delete")){
                Intent intent = new Intent(this, List_Activity_With_Search.class);
                intent.putExtra("item", con.summary);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, Add_summary.class);
                intent.putExtra("email", "summary");
                startActivity(intent);
            }
        }
        if(v==record){
            if(type.equals("delete")){

            }
            else {
                Intent intent = new Intent(this, Add_Recording.class);
                intent.putExtra("email", "summary");
                startActivity(intent);
            }
        }
        if(v==add){
            if(type.equals("delete")){

            }
            else {
                if (!name.getText().toString().equals("")) {
                    Recording_class recording_class = new Recording_class();
                    recording_class.subject = name.getText().toString();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(con.recording);
                    reference.child(getSharedPreferences("email", 0).getString("email", null)).child(name.getText().toString()).setValue(recording_class);
                    Intent intent = new Intent(this, Recordings.class);
                    intent.putExtra("subject", name.getText().toString());
                    startActivity(intent);
                    Message.message(this, name.getText().toString() + "saved");
                } else
                    Message.message(this, "name can't be empty ");
            }
        }
        if(v==test){
            if(type.equals("delete")){

            }
            else {
                SharedPreferences sp = getSharedPreferences("pdf", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("title", "");

                editor.commit();
                Intent intent = new Intent(this, Open_Test.class);
                startActivity(intent);
            }
        }
        if(v==chat){
            if(type.equals("delete")){

            }
            else {
                Intent intent = new Intent(this, OpenChat.class);
                intent.putExtra("job", "student");
                startActivity(intent);
            }
        }
        if(v==open){
            if(type.equals("delete")){

            }
            else {
                Intent intent = new Intent(this, OpenZoom.class);
                startActivity(intent);
            }
        }
        if(v==quiz){
            if(type.equals("delete")){

            }else {
                Intent intent = new Intent(this, Add_Quiz.class);
                startActivity(intent);
            }
        }
    }
}