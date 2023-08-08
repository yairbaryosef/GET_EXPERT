package com.example.dreamfood.BusinessLayer.Profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments.List_Test_Teacher_Fragment;
import com.example.dreamfood.R;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_Profile extends AppCompatActivity implements View.OnClickListener {
   Strings con=new Strings();
    Strings constants=new Strings();
   Gson gson=new Gson();
   String email;
   ListView posts;
   Teacher teacher=new Teacher();
   Student student=new Student();
   CircleImageView imageView;
   TextView bio,followers;
   Button follow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        String teacher_email=getIntent().getStringExtra("email");
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.teacher).child(teacher_email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacher=snapshot.getValue(Teacher.class);
                initViews();
                initFrame();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        email=getSharedPreferences("email",0).getString("email",null);
        SharedPreferences sp=getSharedPreferences(con.student,0);
        String json=sp.getString(con.student,"");
        student=gson.fromJson(json,Student.class);


    }
    /*
    init frame
     */
    private void initFrame(){
        FlareBar bottomBar = findViewById(R.id.bottomBar);
        ArrayList<Flaretab> tabs = new ArrayList<>();

        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.meeting),"meeting","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.exam),"exam","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.quiz),"quiz","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.record),"recordings","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.summary),"summary","#80DEEA"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Teacher_Profile.this,"Tab "+ tabs.get(selectedIndex).getTabText()+" Selected.",Toast.LENGTH_SHORT).show();
               Fragment selectedFragment = null;
                if( tabs.get(selectedIndex).getTabText().equals("meeting")) {
                    selectedFragment=new List_Test_Teacher_Fragment(constants.Meeting,teacher.getEmail());
                }
                else if( tabs.get(selectedIndex).getTabText().equals("quiz")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.Quiz,teacher.getEmail());
                }
                else if( tabs.get(selectedIndex).getTabText().equals("exam")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.test,teacher.getEmail());
                }
                else if( tabs.get(selectedIndex).getTabText().equals("recordings")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.recording,teacher.getEmail());
                }
                else {
                    selectedFragment=new List_Test_Teacher_Fragment(constants.summary,teacher.getEmail());
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });
    }
    /*
     init views
       */
    private void initViews() {
        imageView=findViewById(R.id.profile);
        bio=findViewById(R.id.bio);
        followers=findViewById(R.id.followers);
        Picasso.get().load(teacher.profile_url).into(imageView);
        bio.setText(teacher.getEmail());
        followers.setText(String.valueOf(teacher.followers));
        follow=findViewById(R.id.follow);
        if(teacher.followers_names.contains(student.getEmail())){
            follow.setText("Unfollow");
        }
        posts=findViewById(R.id.posts);
        Posts_Adapter posts_adapter=new Posts_Adapter(this,teacher.messages);
        posts.setAdapter(posts_adapter);
        follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==follow){
            /*
            follow
             */
            if(follow.getText().toString().equals("follow")){
                follow.setText("Unfollow");
                student.following.add(teacher.getEmail());
                teacher.followers_names.add(student.getEmail());
                teacher.followers++;
                 FirebaseDatabase.getInstance().getReference(con.student).child(student.getEmail()).setValue(student);
                FirebaseDatabase.getInstance().getReference(con.teacher).child(teacher.getEmail()).setValue(teacher);
            }
              /*
             unfollow
             */
            else {
                follow.setText("follow");
                student.following.remove(teacher.getEmail());
                teacher.followers_names.remove(student.getEmail());
                teacher.followers--;
                FirebaseDatabase.getInstance().getReference(con.student).child(student.getEmail()).setValue(student);
                FirebaseDatabase.getInstance().getReference(con.teacher).child(teacher.getEmail()).setValue(teacher);

            }
        }
    }
}