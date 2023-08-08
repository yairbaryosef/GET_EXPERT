package com.example.dreamfood.PresentaionLayer.Materials.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Chat;
import com.example.dreamfood.BusinessLayer.Classes.Chat_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Messages;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.example.dreamfood.databinding.ActivityChatMeesageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chat_Meesage extends AppCompatActivity implements View.OnClickListener {


    String receiver ="",sender="";
    Chat chat;
    Chat_Adapter messagesAdapter;
    String job="";

    Strings con=new Strings();
    Student student=new Student();
    boolean is_init_student=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_meesage);
        //init receiver and sender
        job=getSharedPreferences("email",0).getString("type",null);
        receiver=getIntent().getStringExtra("email");
        sender=getSharedPreferences("email",0).getString("email",null);
        chat=new Chat();
        chat.meesages=new ArrayList<Messages>();
        messagesAdapter=new Chat_Adapter(this,chat.meesages,sender);
        initViews();
        init_Chat();
       if(job.equals(con.teacher)){
           init_Student();
       }
    }
    DatabaseReference databaseReference;
    /*
   init the fragment for uploading details
    */
    public void init_Student(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.student).child(receiver);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /*
    init chat
     */
    private void init_Chat() {
        try{
            if(job.equals(con.teacher)) {
                databaseReference = FirebaseDatabase.getInstance().getReference(con.chat).child(sender+" "+receiver);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chat=snapshot.getValue(Chat.class);
                        messagesAdapter=new Chat_Adapter(Chat_Meesage.this,chat.meesages,sender);
                        binding.listMessages.setAdapter(messagesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else{
                databaseReference = FirebaseDatabase.getInstance().getReference(con.chat).child(receiver+" "+sender);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chat=snapshot.getValue(Chat.class);
                        messagesAdapter=new Chat_Adapter(Chat_Meesage.this,chat.meesages,sender);
                        binding.listMessages.setAdapter(messagesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
        catch (Exception e){
            chat=new Chat();
            if(job.equals(con.teacher)) {
                chat.teacher=sender;
                chat.student=receiver;
            }
            else{
                chat.teacher=receiver;
                chat.student=sender;
            }
        }
    }

    /*
    declare views
     */
   ActivityChatMeesageBinding binding;
    /*
    init views
     */
    private void initViews() {
        binding=ActivityChatMeesageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(this);
        binding.send.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==binding.back){
            Intent intent=new Intent(this,OpenChat.class);
            if(job.equals(con.teacher)) {
                intent.putExtra("job",con.student);
            }
            else{
                intent.putExtra("job",con.teacher);
            }
            startActivity(intent);
        }
        if(v==binding.send){
            if(binding.getmessage.getText().length()>0) {
                save_Message();
            }
            else{
                Toast.makeText(this, "enter a message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void save_Message() {
        Messages message=new Messages();
        message.setMessage(binding.getmessage.getText().toString());
        Date date= Calendar.getInstance().getTime();
        message.setTimestamp(date);
        message.setCurrenttime(String.valueOf(DateFormat.format("MM-dd-yyyy", date)) + " " + (String) DateFormat.format("hh:mm aa", date));
        message.setSenderId(sender);
        if(chat==null){
            chat=new Chat();
        }
        if(job.equals(con.teacher)) {
            if(student.teacher_send_count==null){
                student.teacher_send_count=new ArrayList<>();
                student.teachers_send_username=new ArrayList<>();
            }
            if (student.teachers_send_username.contains(sender)){
                int get_position=student.teachers_send_username.indexOf(sender);
                int count=student.teacher_send_count.get(get_position);
                count++;
                student.teacher_send_count.set(get_position,count);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.student).child(receiver);
                databaseReference.setValue(student);
            }
            else{
                student.teachers_send_username.add(sender);
                student.teacher_send_count.add(1);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.student).child(receiver);
                databaseReference.setValue(student);
            }
        }
        else {


        }

        chat.meesages.add(message);
        messagesAdapter=new Chat_Adapter(this,chat.meesages,sender);
        binding.listMessages.setAdapter(messagesAdapter);
        databaseReference.setValue(chat);
        binding.getmessage.setText("");
    }
}