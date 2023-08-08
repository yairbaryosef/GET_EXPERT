package com.example.dreamfood;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments.List_Test_Teacher_Fragment;
import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;

import java.util.ArrayList;

public class Get_Started_Student extends AppCompatActivity implements View.OnClickListener {
    Button price,free,no_Limit;
    AutoCompleteTextView order;

    String[] orders={"Coming soon","recommends","the cheapest"};
    String item="Coming soon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_student);
       initFrame();
       order=findViewById(R.id.order);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,orders);
        order.setAdapter(arrayAdapter);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      initButtons();
    }

    private void initButtons() {
        no_Limit=findViewById(R.id.cancel);
        no_Limit.setOnClickListener(this);
        free=findViewById(R.id.free);
        free.setOnClickListener(this);
        price=findViewById(R.id.price);
        price.setOnClickListener(this);

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
                   selectedFragment=new List_Test_Teacher_Fragment(constants.Meeting,item,change);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("quiz")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.Quiz,change);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("exam")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.test,change);
                }
                else if( tabs.get(selectedIndex).getTabText().equals("recordings")){
                    selectedFragment=new List_Test_Teacher_Fragment(constants.recording,change);
                }
                else {
                    selectedFragment=new List_Test_Teacher_Fragment(constants.summary,change);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });
    }

    Strings constants=new Strings();

    @Override
    public void onClick(View v) {
        if(v==price){
            Set_Price_Dialog();
        }
    }
Dialog d;
    String before="";
    TextView counter;
    EditText input;
    double change=-1;
    private void Set_Price_Dialog() {
        d=new Dialog(this);
        d.setContentView(R.layout.counter_dialog);
        counter=d.findViewById(R.id.counter);
        input=d.findViewById(R.id.input);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before="";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
try{

    double i=Double.valueOf(s.toString());
    if(i>=0) {
        counter.setText(s.toString());
        change = i;
    }
}
catch (Exception e){
    input.setText(before);
}
            }
        });

        d.show();
    }

}