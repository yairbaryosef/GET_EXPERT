package com.example.dreamfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Person;
import com.example.dreamfood.BusinessLayer.PersonController;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Fragments.init_teacher_Fragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
Spinner spinner;
    Button register;
    TextInputEditText phone;
    HashMap<String, String> teacherHashMap;
    HashMap<String, String> studentHashMap;
    String[] items =  {"teacher","student"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    TextInputEditText
            email, password, name,city;
    PersonController personController;
    String item="";
Strings con=new Strings();
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidgets();

}
boolean isExsist=false;
    public void initFrame(){
        Fragment selectedFragment = null;
        if(!item.equals("")) {
            selectedFragment = new init_teacher_Fragment(isExsist, item,email.getText().toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }
    }
    private void initWidgets() {
        spinner=(Spinner)findViewById(R.id.spinner);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        password =  findViewById(R.id.password);
        email = findViewById(R.id.email);
        name =  findViewById(R.id.name);
        phone = (TextInputEditText) findViewById(R.id.phone);
        city =  findViewById(R.id.city);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });


        personController = new PersonController();
    }

    private List<Person> personList;
    private String it="";
    @Override
    public void onClick(View v) {
        if (v == register) {
         if(email.getText().length()>0&&name.getText().length()>0&&password.getText().length()>0&&city.getText().length()>0&&phone.getText().length()>0&&phone.getText().length()<11&&!item.equals("")) {
             if (!checkEmail_Exsist()) {

                 boolean a = personController.Register(email.getText().toString(), name.getText().toString(), password.getText().toString(), city.getText().toString(), phone.getText().toString(), item);

                 Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                 if (a) {
                     Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(this, MainActivity.class);
                     startActivity(intent);
                 } else {
                     Toast.makeText(this, "register failed", Toast.LENGTH_SHORT).show();
                 }
             }
             else{
                 Toast.makeText(this, "email is already exist", Toast.LENGTH_SHORT).show();
             }
         }
         else{
             Toast.makeText(this, "missing field", Toast.LENGTH_SHORT).show();
         }

        }
    }

    private boolean checkEmail_Exsist() {
        initFrame();
        return isExsist;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
