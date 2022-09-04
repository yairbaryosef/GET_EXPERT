package com.example.dreamfood;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.Fragments.List_Test_Teacher_Fragment;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;

import java.util.ArrayList;

public class Get_Started_Student extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_student);
       initFrame();

    }

    public void initFrame(){
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
                Toast.makeText(Get_Started_Student.this,"Tab "+ tabs.get(selectedIndex).getTabText()+" Selected.",Toast.LENGTH_SHORT).show();
                Fragment selectedFragment = null;
                if( tabs.get(selectedIndex).getTabText().equals("meeting")) {
                   selectedFragment=new List_Test_Teacher_Fragment(constants.Meeting);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("quiz")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.Quiz);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("exam")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.test);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("recordings")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.recording);
                }
                else {
                    selectedFragment=new List_Test_Teacher_Fragment(constants.summary);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });
    }

    Strings constants=new Strings();
}