package com.example.dreamfood.Materials.Record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Classes.video;
import com.example.dreamfood.Fragments.List_Video_Fragment;
import com.example.dreamfood.Fragments.add_Video_Fragment;
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

public class Recordings extends AppCompatActivity implements View.OnClickListener {
     Button back;
     TextView text;
    FrameLayout frameLayout;
     ArrayList<video> videos;
    String email;
     Strings con=new Strings();
     Recording_class recording_class=new Recording_class();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);
        videos=new ArrayList<>();
        back=findViewById(R.id.back);
        text=findViewById(R.id.text);
        FlareBar bottomBar = findViewById(R.id.bottomBar);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        email=getSharedPreferences("email",0).getString("email",null);
        String subject=getIntent().getStringExtra("subject");
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
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.add_video),"add video","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.videos),"show videos","#B39DDB"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(Recordings.this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Recordings.this,"Tab "+ tabs.get(selectedIndex).getTabText()+" Selected.",Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if( tabs.get(selectedIndex).getTabText().equals("show videos")) {
                    selectedFragment = new List_Video_Fragment(recording_class.videoArrayList);
                }
                else {
                    selectedFragment = new add_Video_Fragment(recording_class,email,subject);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==back){
            finish();
        }
    }
}