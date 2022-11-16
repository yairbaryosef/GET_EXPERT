package com.example.dreamfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.Materials.Pay.Coins_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class List_Activity_With_Search extends AppCompatActivity {
   ArrayList<String> items=new ArrayList<>();
   Strings cons=new Strings();

   String email;
   Hashtable<String, Summary> educational_contentHashtable;
   String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_search);
        list = findViewById(R.id.list);
        type=getIntent().getStringExtra("item");
        educational_contentHashtable=new Hashtable<>();

        SharedPreferences sp=getSharedPreferences("email",0);
         email=sp.getString("email",null);
         if(type.equals(cons.coin)){
             initStudent();
             initCoins();
         }
        if(type.equals(cons.summary)){
           DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(cons.summary).child(email);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            Summary summary = dataSnapshot.getValue(Summary.class);
                        Toast.makeText(List_Activity_With_Search.this, summary.name, Toast.LENGTH_SHORT).show();
                                educational_contentHashtable.put(summary.name+" "+String.valueOf(summary.cost)+" Summary",summary);





                    }
                    initList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
   Student student=new Student();
    private void initStudent(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(cons.student);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(email.equals(dataSnapshot.getKey())){
                        student=dataSnapshot.getValue(Student.class);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initCoins() {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(cons.coin);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coins=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){

                         coins.add(dataSnapshot2.getValue(Coin.class));
                    }
                }
                Coins_Adapter coins_adapter=new Coins_Adapter(List_Activity_With_Search.this,coins);

                list.setAdapter(coins_adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Coin coin=coins.get(position);
                        if(coin.cost==0){
                            coin.StartDate= Calendar.getInstance().getTime();
                            student.coins.add(coin);
                            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(cons.student).child(email);
                            databaseReference1.setValue(student);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   ArrayList<Coin> coins;
    ListView list;
    private void initList() {

        if (type.equals(cons.summary)) {
            for (Summary summary : educational_contentHashtable.values()) {
                Toast.makeText(this, "in", Toast.LENGTH_SHORT).show();
                items.add(summary.name + " " + String.valueOf(summary.cost) + " Summary");

            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            list.setAdapter(arrayAdapter);
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if (type.equals(cons.summary)) {
                        Summary summary = educational_contentHashtable.get(parent.getItemAtPosition(position).toString());

                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(summary.url);
                        storageReference.delete();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(cons.summary).child(email).child(summary.name);
                        databaseReference.setValue(null);

                        Toast.makeText(List_Activity_With_Search.this, "deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return false;
                }
            });
        }
    }
}