package com.example.dreamfood.viewmodel;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Deal;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.BusinessLayer.Test;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments.List_Test_Teacher_Fragment;
import com.example.dreamfood.PresentaionLayer.Materials.Record.Recording_class;
import com.example.dreamfood.R;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class Add_deal extends AppCompatActivity implements View.OnClickListener {

    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    String item="";
    FrameLayout frameLayout;
    Hashtable<String,ArrayList<String>> saved;
    Button show,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deal);
        show=findViewById(R.id.show);
        show.setOnClickListener(this);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
        saved=new Hashtable<>();
        saved.put(con.test,new ArrayList<>());
        saved.put(con.Meeting,new ArrayList<>());
        saved.put(con.Quiz,new ArrayList<>());
        saved.put(con.recording,new ArrayList<>());


        FlareBar bottomBar = findViewById(R.id.bottomBar);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.exam),"exam","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.videos),"videos","#B39DDB"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.meeting),"meeting","#B39DDB"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.quiz),"quiz","#B39DDB"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.qa),"question and answers","#B39DDB"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Add_deal.this, "Tab " + tabs.get(selectedIndex).getTabText() + " Selected.", Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if(tabs.get(selectedIndex).getTabText().equals("exam")){
                    selectedFragment=new List_Test_Teacher_Fragment(tests,saved,con.test);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                }
                if(tabs.get(selectedIndex).getTabText().equals("quiz")){
                    selectedFragment=new List_Test_Teacher_Fragment(quizzes,saved,con.Quiz);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                }
                if(tabs.get(selectedIndex).getTabText().equals("meeting")){
                    selectedFragment=new List_Test_Teacher_Fragment(meetings,saved,con.Meeting);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                }
                if(tabs.get(selectedIndex).getTabText().equals("videos")){
                    selectedFragment=new List_Test_Teacher_Fragment(records,saved,con.recording);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                }
            }
        });
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(con.test);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot data:snapshot.getChildren()) {
                    if (email.equals(data.getKey())) {
                        for (DataSnapshot dataSnapshot : data.getChildren()) {
                            Test test = dataSnapshot.getValue(Test.class);
                            if (test.file.teacherEmail.equals(email)) {
                                tests.add("test number " + String.valueOf(i) + test.subject);
                                i++;

                            }
                        }
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference= FirebaseDatabase.getInstance().getReference(con.Quiz);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot data:snapshot.getChildren()) {
                    if (email.equals(data.getKey())) {
                        for (DataSnapshot dataSnapshot : data.getChildren()) {
                            Quiz quiz = dataSnapshot.getValue(Quiz.class);
                            quizzes.add(quiz.type);
                        }
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference= FirebaseDatabase.getInstance().getReference(con.recording);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot data:snapshot.getChildren()) {
                    if (email.equals(data.getKey())) {
                        for (DataSnapshot dataSnapshot : data.getChildren()) {
                            Recording_class record = dataSnapshot.getValue(Recording_class.class);
                            records.add(record.subject);
                        }
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference= FirebaseDatabase.getInstance().getReference(con.Meeting);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                String email=getSharedPreferences("email",0).getString("email",null);
                for(DataSnapshot data:snapshot.getChildren()) {
                    Meeting m=data.getValue(Meeting.class);
                    if(m.email.equals(email)){
                        meetings.add(m.type);
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tests=new ArrayList<>();
        quizzes =new ArrayList<>();
        records=new ArrayList<>();
        meetings =new ArrayList<>();

        String email=getSharedPreferences("email",0).getString("email",null);
        reference=FirebaseDatabase.getInstance().getReference(con.Quiz).child(email);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Quiz_arraylist=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Quiz_arraylist.add(snapshot1.getValue(Quiz.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference=FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacher=snapshot.getValue(Teacher.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    ArrayList<Quiz> Quiz_arraylist;
    Teacher teacher;
    ArrayList<String> tests;
    ArrayList<String> quizzes;
    ArrayList<String> records;
    ArrayList<String> meetings;
    Strings con=new Strings();
    Dialog d;
    ListView list;
    public void ShowList(){
        d=new Dialog(this);
        d.setContentView(R.layout.videos_list);
        list=d.findViewById(R.id.list);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.addAll(saved.get(con.test));
        arrayList.addAll(saved.get(con.Quiz));
        arrayList.addAll(saved.get(con.recording));
        arrayList.addAll(saved.get(con.Meeting));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_gallery_item,arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String element=parent.getItemAtPosition(position).toString();
                if(element.contains(con.test)){
                    saved.get(con.test).remove(element);
                }
                else if(element.contains(con.Quiz)){
                    saved.get(con.Quiz).remove(element);
                }
                else if(element.contains(con.recording)){
                    saved.get(con.recording).remove(element);
                }
                else if(element.contains(con.Meeting)){
                    saved.get(con.Meeting).remove(element);
                }
                Toast.makeText(Add_deal.this, element+" deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        d.show();
    }
    @Override
    public void onClick(View v) {
        if(v==show){
              ShowList();
        }
        if(v==save){
           Save_Dialog();
        }
        if(v==save_Deal){
            if(element.equals("")){
                Toast.makeText(this, "choose Time type for the deal", Toast.LENGTH_SHORT).show();
            }
            else {
                if(description.getText().length()>0&&number.getText().length()>0&&name.getText().length()>0){
                    try {
                        double num=Double.valueOf(number.getText().toString());
                        String email=getSharedPreferences("email",0).getString("email",null);

                        Deal deal=new Deal();
                        deal.name=name.getText().toString();
                        deal.des= description.getText().toString();
                        deal.cost_Time_type=element;
                        deal.cost=num;
                        deal.email=email;
                        deal.quizzes_for_Subscribers=quizzes;
                       teacher.deals.add(deal);
                       DatabaseReference reference=FirebaseDatabase.getInstance().getReference(con.teacher);
                        reference.child(email).setValue(teacher);
                        Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else
                    Toast.makeText(this, "missing field", Toast.LENGTH_SHORT).show();
            }
        }
    }
   TextInputEditText number,name;
    EditText description;
    Button save_Deal;
    String[] items =  {"year","month","day","minute"};
    ArrayAdapter<String> Time;
    String element="";
    public void Save_Dialog(){
        d=new Dialog(this);
        d.setContentView(R.layout.save_deal);
        autoCompleteTxt=d.findViewById(R.id.auto_complete_txt);
        description=d.findViewById(R.id.description);
        number=d.findViewById(R.id.number);
        name=d.findViewById(R.id.name);
        save_Deal=d.findViewById(R.id.save);
        save_Deal.setOnClickListener(this);
        Time = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(Time);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                element = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+element,Toast.LENGTH_SHORT).show();
            }
        });
        d.show();
    }
}