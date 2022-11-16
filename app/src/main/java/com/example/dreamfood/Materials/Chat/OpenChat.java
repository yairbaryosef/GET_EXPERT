package com.example.dreamfood.Materials.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OpenChat extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
ListView listView;
SearchView searchView;
Button open;
String item;
Strings con=new Strings();
    String job;
Strings strings=new Strings();
String persons="";
boolean student;

ArrayList<Profile_Adapter.profile> profiles;

    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chat);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
       job= getIntent().getStringExtra("job");

        init();
        DatabaseReference myRef=database.getReference(job);
        ArrayList<String> arrayList=new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if(job.equals(strings.student)) {
                        try {
                            arrayList.add(con.emailStart(snapshot1.getValue(Student.class).getEmail()));
                            student=true;
                        } catch (Exception e) {
                            Toast.makeText(OpenChat.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        try {
                            arrayList.add(con.emailStart(snapshot1.getValue(Teacher.class).getEmail())+" subject: "+snapshot1.getValue(Teacher.class).fav);
                            Teacher teacher=snapshot1.getValue(Teacher.class);
                            Profile_Adapter.profile profile=new Profile_Adapter.profile(teacher.profile_url,snapshot1.getKey());
                            profiles.add(profile);
                            student=false;
                        }
                        catch (Exception e){
                            Toast.makeText(OpenChat.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if(job.equals(strings.teacher)){
                 profile_adapter=new Profile_Adapter(OpenChat.this,profiles);
                    listView.setAdapter(profile_adapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                         ArrayList<Profile_Adapter.profile>   array = new ArrayList<Profile_Adapter.profile>();

                            for (Profile_Adapter.profile adapterSubject : profiles) {
                                if (adapterSubject.name.toLowerCase().contains(newText.toLowerCase()) ) {
                                    array.add(adapterSubject);
                                }
                            }
                            profile_adapter=new Profile_Adapter(OpenChat.this,array);
                            listView.setAdapter(profile_adapter);
                            return false;
                        }
                    });
                }
                else {
                    dataAdapter = new ArrayAdapter<String>(OpenChat.this, android.R.layout.simple_list_item_1, arrayList);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            dataAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
                listView.setAdapter(dataAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    Profile_Adapter profile_adapter;
    public void init(){
        profiles=new ArrayList<>();
        open=(Button)findViewById(R.id.chat);
        open.setOnClickListener(this);
        listView=(ListView) findViewById(R.id.list12);
        searchView=(SearchView) findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        listView.setOnItemClickListener(this);

    }
    Profile_Adapter.profile pro;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pro = profiles.get(position);
        item=pro.name;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        persons=item;

    }

    @Override
    public void onClick(View v) {
        if(v==open){
            if(!persons.equals("")) {
                Intent intent = new Intent(this, Chat_Meesage.class);
                intent.putExtra("email", persons);
                startActivity(intent);
            }
        }
    }
}