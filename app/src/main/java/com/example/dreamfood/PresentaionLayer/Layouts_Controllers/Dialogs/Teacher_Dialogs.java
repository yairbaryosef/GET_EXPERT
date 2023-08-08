package com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PresentaionLayer.Materials.Chat.Profile_Adapter;
import com.example.dreamfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Teacher_Dialogs {
    private Dialog d;
    private Teacher teacher=new Teacher();
    private String email;
    private ArrayList<String> subjects;
    Strings con=new Strings();
    /*
    constructor
     */
    public Teacher_Dialogs(Context context, Teacher teacher,String email){
        d=new Dialog(context);
        this.teacher=teacher;
        this.email=email;
    }
    /*
    add message to teacher to database
     */
    public void add_Message(){

        d.setContentView(R.layout.description);
        EditText editText=d.findViewById(R.id.updescription);
        Button button=d.findViewById(R.id.upd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==button){
                    if(editText.getText().length()>0) {
                        Profile_Adapter.profile profile = new Profile_Adapter.profile();
                        profile.resource =editText.getText().toString();
                        teacher.messages.add(profile);
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
                        databaseReference.setValue(teacher);
                        Toast.makeText(d.getContext(), "message saved", Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                }
            }
        });
        d.show();
    }
    /*
  constructor 2
   */
    public Teacher_Dialogs(Context context, Teacher teacher, String email, ArrayList<String> subjects){
        d=new Dialog(context);
        this.teacher=teacher;
        this.subjects=subjects;
        this.email=email;
    }
    ArrayList<String> array;
    /*
    add favorite subject for teacher
     */
    public void subjects(){
        d.setContentView(R.layout.list_dialog_with_search_and_button);
        ListView list=d.findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(d.getContext(), android.R.layout.simple_list_item_1,subjects);
        list.setAdapter(arrayAdapter);
        SearchView searchView=d.findViewById(R.id.search_bar);
        Button save=d.findViewById(R.id.save);
        searchView.setQueryHint("type here to search");
        /*
        init search
         */
        array=subjects;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                array=new ArrayList<>();
                for (String subject : subjects) {
                    if ((subject.toLowerCase().contains(newText.toLowerCase())))
                      array.add(subject);


                }
                if(array.isEmpty()){
                    save.setText(newText);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(d.getContext(), android.R.layout.simple_list_item_1, array);
                list.setAdapter(adapter);
                return false;
            }
        });
        /*
        on click item listener
         */
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String item=array.get(position);
               save.setText(item);
           }
       });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(v==save){

                     teacher.fav=save.getText().toString();
                     DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.teacher).child(email);
                     databaseReference.setValue(teacher);
                     Toast.makeText(d.getContext(), "saved", Toast.LENGTH_SHORT).show();
                     d.dismiss();

                 }
            }
        });
        d.show();
    }


}
