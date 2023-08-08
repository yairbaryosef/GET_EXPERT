package com.example.dreamfood.BusinessLayer.Profile;

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
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.Profile_Adapter;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class Pick_A_Teacher extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
ListView listView;
SearchView searchView;
Button open;
String item;
Strings con=new Strings();
    String job;
Strings strings=new Strings();
String persons="";
ArrayList<String> arrayList;
boolean student;

ArrayList<Profile_Adapter.profile> profiles;
     Hashtable<String, Profile_Adapter.profile> hashtable;
    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chat);
hashtable=new Hashtable<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
      job=con.teacher;

        init();
        DatabaseReference myRef=database.getReference(job);
       arrayList=new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            /*
            init profiles
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    /*
                    init students array
                     */
                    if(job.equals(strings.student)) {
                        try {
                            arrayList.add(snapshot1.getKey());
                            student=true;
                        } catch (Exception e) {
                            Toast.makeText(Pick_A_Teacher.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    /*
                    init teachers array
                     */
                    else{
                        try {
                            arrayList.add(snapshot1.getKey());
                            Teacher teacher=snapshot1.getValue(Teacher.class);

                            Profile_Adapter.profile profile=new Profile_Adapter.profile(teacher.profile_url,snapshot1.getKey());
                            profiles.add(profile);
                            hashtable.put(snapshot1.getKey(),profile);

                            student=false;
                        }
                        catch (Exception e){
                            Toast.makeText(Pick_A_Teacher.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                /*
                    init teachers list with search
                     */
                if(job.equals(strings.teacher)){
                 profile_adapter=new Profile_Adapter(Pick_A_Teacher.this,profiles);
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

                            profile_adapter=new Profile_Adapter(Pick_A_Teacher.this,array);
                            listView.setAdapter(profile_adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String e=array.get(position).name;
                                    pro = hashtable.get(e);
                                    item=pro.name;
                                    // Showing selected spinner item
                                    Toast.makeText(parent.getContext(), "Selected: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();

                                   Intent intent = new Intent(Pick_A_Teacher.this, Teacher_Profile.class);
                                   intent.putExtra("email", item);
                                   startActivity(intent);
                                }
                            });
                            return false;
                        }
                    });
                }
                /*
                    init students list with search
                     */
                else {
                    dataAdapter = new ArrayAdapter<String>(Pick_A_Teacher.this, android.R.layout.simple_list_item_1, arrayList);
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
    /*
    init views
     */
    public void init(){
        profiles=new ArrayList<>();
        open=(Button)findViewById(R.id.chat);
        open.setText("Pick A Teacher");
        listView=(ListView) findViewById(R.id.list12);
        searchView=(SearchView) findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        listView.setOnItemClickListener(this);

    }
    Profile_Adapter.profile pro;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onClick(View v) {

    }
}