package com.example.dreamfood.Materials.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dreamfood.BusinessLayer.Chat;
import com.example.dreamfood.BusinessLayer.Classes.Chat_Adapter;
import com.example.dreamfood.BusinessLayer.Classes.Messages;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class Chat_Meesage extends AppCompatActivity implements View.OnClickListener {
    Strings constants=new Strings();
    EditText mgetmessage;
    ImageButton msendmessagebutton;
    String emailUsers,users;

    CardView msendmessagecardview;

    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;

    private String enteredmessage;

    FirebaseDatabase firebaseDatabase;


    ImageButton mbackbuttonofspecificchat;

    ListView mmessagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    Chat_Adapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;
    ArrayList<String> arrayList;
    Chat chat=new Chat();
    Hashtable<String, Student> students=new Hashtable<String,Student>();
    boolean isExsist=false;
    boolean shape=false;
    Strings getConstants=new Strings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_meesage);
        users=getIntent().getStringExtra("email");
        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.carviewofsendmessage);
        msendmessagebutton=(ImageButton) findViewById(R.id.imageviewsendmessage);

        mnameofspecificuser=findViewById(R.id.Nameofspecificuser);
       mnameofspecificuser.setText(users);
        mimageviewofspecificuser=findViewById(R.id.specificuserimageinimageview);

        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);
        mbackbuttonofspecificchat.setOnClickListener(this);
        messagesArrayList=new ArrayList<Messages>();
        mmessagerecyclerview=(ListView)findViewById(R.id.recyclerviewofspecific);
        SharedPreferences sp=getSharedPreferences("email",0);
        String email=sp.getString("email",null);
        messagesAdapter=new Chat_Adapter(Chat_Meesage.this,messagesArrayList,email);

        mmessagerecyclerview.setAdapter(messagesAdapter);



        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.chat);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    try {
                       chat=dataSnapshot.getValue(Chat.class);


                       if(dataSnapshot.getKey().equals(email+" "+constants.emailStart(users))||dataSnapshot.getKey().equals(constants.emailStart(users)+" "+email)){
                           if(dataSnapshot.getKey().equals(constants.emailStart(users)+" "+email)){
                               shape=true;

                           }
                           isExsist=true;
                           break;
                       }

                    }
                    catch (Exception e){

                    }
                }
                if(!isExsist){
                    chat=new Chat();
                    if(!shape) {
                        chat.people1 = email;
                        chat.people2 = constants.emailStart(users);
                        chat.isGroup = false;
                        chat.meesages=new ArrayList<>();
                        databaseReference.child(chat.people1 + " " + chat.people2).setValue(chat);
                    }
                    else {
                        chat.people2 = email;
                        chat.people1 = constants.emailStart(users);
                        chat.isGroup = false;
                        chat.meesages=new ArrayList<>();
                        databaseReference.child(chat.people1 + " " + chat.people2).setValue(chat);
                    }

                }
                else {

                        chat.isGroup = false;


                }

                if(!chat.isGroup){
                    messagesAdapter=new Chat_Adapter(Chat_Meesage.this, chat.meesages,email);
                    mmessagerecyclerview.setAdapter(messagesAdapter);

                    msendmessagebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            enteredmessage=mgetmessage.getText().toString();
                            if(enteredmessage.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                            }

                            else

                            {
                                calendar=Calendar.getInstance();
                                simpleDateFormat=new SimpleDateFormat("hh:mm a");
                                Date date=new Date();
                                currenttime=simpleDateFormat.format(calendar.getTime());
                                Messages messages=new Messages(enteredmessage,email,date.getTime(),currenttime);
                                chat.meesages.add(messages);
                                firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference reference=firebaseDatabase.getReference(constants.chat);

                                reference.child(chat.people1 + " " + chat.people2).setValue(chat);

                                messagesArrayList.add(messages);
                                mmessagerecyclerview.setAdapter(messagesAdapter);
                                mgetmessage.setText(null);




                            }




                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
        if(v==mbackbuttonofspecificchat){
            finish();
        }
    }
}