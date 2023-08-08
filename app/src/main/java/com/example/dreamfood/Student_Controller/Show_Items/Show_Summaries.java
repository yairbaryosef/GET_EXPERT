package com.example.dreamfood.Student_Controller.Show_Items;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.PresentaionLayer.Extend_Fitchers.PDF_Controller.PDF;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

public class Show_Summaries extends AppCompatActivity {
    Student student=new Student();
    Hashtable<String,Teacher> teacherHashtable;
    ArrayList<Summary> summaries;
    Strings con=new Strings();
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_summaries);
        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        email=con.getEmail(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        initStudent();

        initSummaries();
        initTeachers();
    }
   /*
   init teachers
    */
    private void initTeachers() {
    }

    /*
    init summaries
     */
    private void initSummaries() {
    }

    /*
    init student
     */
    private void initStudent() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(con.student).child(email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student=snapshot.getValue(Student.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    init grid layout
     */
    public class Show_Summaries_Adapter extends BaseAdapter{
         Context context;
        LayoutInflater inflater;
         ArrayList<Summary> summaries;
         Hashtable<String,Teacher> teacherHashtable;
         public Show_Summaries_Adapter(Context context, ArrayList<Summary> summaries, Hashtable<String, Teacher> teacherHashtable){
                this.context=context;
                this.summaries=summaries;
                this.teacherHashtable=teacherHashtable;
         }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return summaries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
             Summary summary=(Summary) getItem(position);
            if (inflater == null)
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null){

              convertView = inflater.inflate(R.layout.show_summary_item,null);

            }
            TextView subject=convertView.findViewById(R.id.subject);
            TextView teacher=convertView.findViewById(R.id.teacher);
            ImageView image=convertView.findViewById(R.id.image);
            Teacher teacher1=teacherHashtable.get(summary.email);
            subject.setText(summary.name);
            teacher.setText(summary.email);
            Picasso.get().load(teacher1.profile_url).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==image){
                        Intent intent=new Intent(context, PDF.class);
                        intent.putExtra("url",summary.url);
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }
    }
}