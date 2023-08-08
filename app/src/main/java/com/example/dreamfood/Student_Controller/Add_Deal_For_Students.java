package com.example.dreamfood.Student_Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
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

import java.util.ArrayList;

public class Add_Deal_For_Students extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list;
    FrameLayout frameLayout;
    ArrayList<String> teachers,arrayList;
    ArrayAdapter<String> arrayAdapter;
    Strings con=new Strings();
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deal_for_students);
        teachers=new ArrayList<>();
        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        arrayList = new ArrayList<String>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

              ArrayList<String>  array = new ArrayList<String>();
                array.add("choose");
                for (String adapterSubject : teachers) {
                    if (adapterSubject.toLowerCase().contains(newText.toLowerCase()) && !adapterSubject.equals("choose")) {
                        array.add(adapterSubject);
                    }
                }
               ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Add_Deal_For_Students.this, android.R.layout.simple_list_item_1, array);
                list.setAdapter(dataAdapter);
                list.setOnItemClickListener(Add_Deal_For_Students.this);
                return false;
            }
        });
        FlareBar bottomBar = findViewById(R.id.bottomBar);
        frameLayout=(FrameLayout)findViewById(R.id.fragment_container);
        bottomBar.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.deal_),"deal","#80DEEA"));
        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3
                Toast.makeText(Add_Deal_For_Students.this, "Tab " + tabs.get(selectedIndex).getTabText() + " Selected.", Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if(tabs.get(selectedIndex).getTabText().equals("deal")) {
                    if(item.equals("")){
                        Toast.makeText(Add_Deal_For_Students.this, "choose a teacher", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(con.teacher);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                                    if (item.equals(dataSnapshot.getKey())){
                                        teacher=dataSnapshot.getValue(Teacher.class);
                                        break;
                                    }
                                }


                               Fragment selectedFragment=new List_Test_Teacher_Fragment(teacher.deals);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.teacher);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    teachers.add(dataSnapshot.getKey());
                }
                arrayAdapter=new ArrayAdapter<String>(Add_Deal_For_Students.this, android.R.layout.simple_list_item_1,teachers);
                list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list=findViewById(R.id.list);
        list.setOnItemClickListener(this);
    }
    Teacher teacher;
    String item="";
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item=parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Selected: "+item, Toast.LENGTH_SHORT).show();
    }
}