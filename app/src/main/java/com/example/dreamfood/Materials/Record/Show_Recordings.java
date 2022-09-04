package com.example.dreamfood.Materials.Record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.Fragments.List_Video_Fragment;
import com.example.dreamfood.R;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_Recordings extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button back;
    ArrayAdapter<String> arrayAdapter;
    TextView text;
    FrameLayout frameLayout;
    ArrayList<video> videos;
    ArrayList<String> Records;
    String email,subject,item="";
    ListView lv;
    Strings con=new Strings();
    Recording_class recording_class=new Recording_class();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recordings);
        lv=findViewById(R.id.lv);
        videos=new ArrayList<>();
        Records=new ArrayList<>();

        back=findViewById(R.id.back);
        text=findViewById(R.id.text);
        FlareBar bottomBar = findViewById(R.id.bottomBar);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        email=getSharedPreferences("email",0).getString("email",null);
        String subject=getIntent().getStringExtra("subject");

         tabs.add(new Flaretab(getResources().getDrawable(R.drawable.videos),"show videos","#B39DDB"));
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(con.recording);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                     String e=dataSnapshot.getKey();

                     for(DataSnapshot sub:dataSnapshot.getChildren()){
                         Records.add(e+" "+sub.getKey());

                     }
                }
              arrayAdapter=new ArrayAdapter<String>(Show_Recordings.this, android.R.layout.simple_list_item_1, Records);
                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(Show_Recordings.this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Show_Recordings.this,"Tab "+ tabs.get(selectedIndex).getTabText()+" Selected.",Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if( tabs.get(selectedIndex).getTabText().equals("show videos")) {
                    if(!item.equals("")) {
                         updateRecord();
                        selectedFragment = new List_Video_Fragment(recording_class.videoArrayList);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    }
                    else
                        Toast.makeText(Show_Recordings.this, "select recording", Toast.LENGTH_SHORT).show();
                }


            }
        });
        back.setOnClickListener(this);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==back){
            finish();
        }
    }
    public void updateRecord(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(con.recording);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(con.emailStart(email).equals(dataSnapshot.getKey()))
                        for (DataSnapshot subjects:dataSnapshot.getChildren()) {
                            if (subject.equals(subjects.getKey())) {

                                recording_class = subjects.getValue(Recording_class.class);
                                break;
                            }
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("counter");
        ref.setValue(1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item=parent.getItemAtPosition(position).toString();
        Toast.makeText(this, item+" selected", Toast.LENGTH_SHORT).show();
        email=item.substring(0,item.indexOf(' '));
        subject=item.substring(item.indexOf(' ')+1);
    }
}