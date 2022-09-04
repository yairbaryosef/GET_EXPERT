package com.example.dreamfood.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.dreamfood.BusinessLayer.Classes.Deal;
import com.example.dreamfood.BusinessLayer.Classes.Deal_for_student;
import com.example.dreamfood.BusinessLayer.Classes.OptionsQ;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.BusinessLayer.Test;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.Materials.Chat.Profile_Adapter;
import com.example.dreamfood.Materials.Quiz.quiz_list;
import com.example.dreamfood.Materials.Record.Recording_class;
import com.example.dreamfood.PDF_Controller.PDF;
import com.example.dreamfood.PDF_Controller.add_pdf;
import com.example.dreamfood.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class List_Test_Teacher_Fragment extends Fragment {
    View v;

    ArrayList<String> tests;
    String con;
    Hashtable<String,ArrayList<String>> save;
    ListView list;
    public List_Test_Teacher_Fragment(ArrayList<String> tests, Hashtable<String,ArrayList<String>> saved,String con){
     save =saved;
        this.tests=tests;
        this.con=con;
        s="add";
    }
OptionsQ optionsQS;
    public List_Test_Teacher_Fragment(OptionsQ optionsQS,String item){
        this.optionsQS=optionsQS;
       s="quiz";
       getItem=item;
    }
    int i;
    String s;
    String getItem;
    Student student=new Student();

    ArrayList<Deal> deals;
    public List_Test_Teacher_Fragment(ArrayList<Deal> deals){
       this.deals=deals;
       s="pick";
    }
    ArrayList<Profile_Adapter.profile> profiles;
    String getCon;
    public List_Test_Teacher_Fragment(String cons){

        getCon=cons;
        s="search";
    }
    String email;
    Hashtable<String,String> images;
    Hashtable<String,Quiz> quizHashtable;
    Hashtable<String,Meeting> meetingHashtable;
    Hashtable<String,Test> testHashtable;
    Student st=new Student();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.videos_list, container, false);
        list=v.findViewById(R.id.list);
        if(s.equals("search")){
            testHashtable=new Hashtable<>();
            meetingHashtable=new Hashtable<>();
            quizHashtable=new Hashtable<>();
            summaryHashtable=new Hashtable<>();
            v = inflater.inflate(R.layout.videos_list, container, false);
            list=v.findViewById(R.id.list);
            profiles=new ArrayList<>();
            images=new Hashtable<String,String>();
           email= getContext().getSharedPreferences("email", 0).getString("email", null);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(constants.student);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (email.equals(dataSnapshot.getKey()) || email.equals(dataSnapshot.getValue(Student.class).getEmail())) {
                            st = dataSnapshot.getValue(Student.class);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference(constants.teacher).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        Teacher teacher = dataSnapshot.getValue(Teacher.class);
                        try {


                            images.put(dataSnapshot.getKey(), teacher.profile_url);
                        }
                        catch (Exception e){
                            images.put(dataSnapshot.getKey(), "");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference(getCon).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                            try {


                                if (getCon.equals(constants.Quiz)) {
                                    Quiz quiz = dataSnapshot1.getValue(Quiz.class);
                                    quizHashtable.put(quiz.type + " by: " + quiz.email,quiz);
                                    profiles.add(new Profile_Adapter.profile(images.get(quiz.email), quiz.type + " by: " + quiz.email));
                                }
                                if (getCon.equals(constants.Meeting)) {
                                    Meeting quiz = dataSnapshot1.getValue(Meeting.class);
                                    meetingHashtable.put(quiz.type + " by: " + quiz.email,quiz);
                                    profiles.add(new Profile_Adapter.profile(images.get(quiz.email), quiz.type + " by: " + quiz.email));
                                }
                                if (getCon.equals(constants.test)) {
                                    Test quiz = dataSnapshot1.getValue(Test.class);
                                    testHashtable.put(quiz.subject + " by: " + quiz.file.teacherEmail,quiz);
                                    profiles.add(new Profile_Adapter.profile(images.get(quiz.file.teacherEmail), quiz.subject + " by: " + quiz.file.teacherEmail));
                                }
                                if (getCon.equals(constants.recording)) {
                                    Recording_class recording_class = dataSnapshot1.getValue(Recording_class.class);
                                    profiles.add(new Profile_Adapter.profile(images.get(dataSnapshot.getKey()), recording_class.subject + " by: " + dataSnapshot.getKey()));
                                }
                                if(getCon.equals(constants.summary)){

                                        Summary sum = dataSnapshot1.getValue(Summary.class);
                                        profiles.add(new Profile_Adapter.profile(images.get(dataSnapshot.getKey()), sum.name + " by: " + dataSnapshot.getKey()));
                                         summaryHashtable.put(sum.name + " by: " + dataSnapshot.getKey(),sum);
                                }
                            }
                            catch (Exception e){

                            }
                        }

                    }
                    Profile_Adapter profile_adapter=new Profile_Adapter(getContext(),profiles);
                    list.setAdapter(profile_adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            SearchView searchView = (SearchView) v.findViewById(R.id.search_bar);
            searchView.setQueryHint("type here to search");


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<Profile_Adapter.profile> array=new ArrayList<>();
                    for (Profile_Adapter.profile adapterSubject : profiles) {
                        if (adapterSubject.name.toLowerCase().contains(newText.toLowerCase()) && !adapterSubject.equals("choose")) {
                            array.add(adapterSubject);
                        }
                    }
                    Profile_Adapter profile_adapter=new Profile_Adapter(getContext(),array);
                    list.setAdapter(profile_adapter);
                    return false;
                }
            });



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    item = profiles.get(position).name;
                    if(getCon.equals(constants.summary)){
                        Summary summary=summaryHashtable.get(item);
                        Intent intent=new Intent(getContext(), PDF.class);
                        intent.putExtra("url",summary.url);

                        startActivity(intent);
                    }
                    else if (testHashtable.containsKey(item)) {
                        JoinTest();
                    } else {


                        createaddquestiondislogQuiz();
                    }

                }
            });
        }
        else if(s.equals("quiz")){
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, optionsQS.answers);
            list.setAdapter(arrayAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  getItem=parent.getItemAtPosition(position).toString();
                    Toast.makeText(getContext(), "selected: "+getItem, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if(s.equals("add")) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tests);
            list.setAdapter(arrayAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                    if (!save.contains(item)) {
                        save.get(con).add(item + " " + con);
                        Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "test is already on the deal", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else{
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(constants.student);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String email=getContext().getSharedPreferences("email",0).getString("email",null);
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if(dataSnapshot.getKey().equals(email)){
                            student=dataSnapshot.getValue(Student.class);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            ArrayList<String> arrayList=new ArrayList<>();
            for(Deal d:deals){
                arrayList.add(d.name+" "+String.valueOf(d.cost)+" per "+d.cost_Time_type);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
            list.setAdapter(arrayAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    item= parent.getItemAtPosition(position).toString();
                    Sign_up_to_a_deal();
                }
            });
        }
        return v;
    }
    String item;
    Dialog d;
    TextView deal_name,deal_cost;
    Spinner spinner;
    Button payment;
    public void Sign_up_to_a_deal(){
        d=new Dialog(getContext());
        d.setContentView(R.layout.activity_show_deal);
        deal_cost=d.findViewById(R.id.deal_cost);
        deal_name=d.findViewById(R.id.deal_name);
        spinner=d.findViewById(R.id.spinner);
     deal_name.setText(item);
     deal=new Deal();
     for(Deal d:deals){
         if((d.name+" "+String.valueOf(d.cost)+" per "+d.cost_Time_type).equals(item)){
             deal=d;
             break;
         }
     }
     deal_cost.setText(deal.cost+" per "+deal.cost_Time_type);
     ArrayList<String> stringArrayList=new ArrayList<>();
     stringArrayList.addAll(deal.quizzes_for_Subscribers);
        stringArrayList.addAll(deal.tests_for_Subscribers);
     ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,stringArrayList);
     spinner.setAdapter(arrayAdapter);
        payment=d.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==payment){
                    Deal_for_student deal_for_student=new Deal_for_student();
                    Date date;
                    date= new Date(System.currentTimeMillis());
                    Toast.makeText(getContext(), date.toString(), Toast.LENGTH_LONG).show();
                    date.setMonth(date.getMonth()+1);
                    deal_for_student.endDate=date;
                    deal_for_student.name= deal.name;
                    deal_for_student.email=deal.email;
                    student.deal_for_students.add(deal_for_student);
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.student);
                    databaseReference.child(constants.emailStart(student.getEmail())).setValue(student);

                }
            }
        });
        d.show();
    }
    Deal deal;
    Strings constants=new Strings();
    TextView subject,description,price,date;
    Button join;
    public void createaddquestiondislogQuiz() {
        d = new Dialog(getContext());
        d.setContentView(R.layout.des);
        subject = (TextView) d.findViewById(R.id.subject);
        description = (TextView) d.findViewById(R.id.description);
        price = (TextView) d.findViewById(R.id.price);
        date = (TextView) d.findViewById(R.id.date);
        TextView teacher = (TextView) d.findViewById(R.id.teacher);
        TextView rate = (TextView) d.findViewById(R.id.rate);
        join = (Button) d.findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == join) {
                    if (i == 1) {
                        d.dismiss();
                        Intent intent = new Intent(getContext() ,quiz_list.class);
                        intent.putExtra("email", q.email);
                        intent.putExtra("subject", q.type);
                        startActivity(intent);
                    }
                    if (i == 0) {
                        d.dismiss();
                        st.meetings.add(meetingHashtable.get(item));
                        Toast.makeText(getContext(), "meeting saved", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference(constants.student).child(constants.emailStart(st.getEmail())).setValue(st);


                    }
                }
            }

        });
        try {
            m = meetingHashtable.get(item);
            subject.setText(m.type);
            description.setText(m.description);
            price.setText(String.valueOf(m.price));
            date.setText(m.startdate.toString());
            teacher.setText(m.email);
            rate.setText("5.0");
            i = 0;
            d.show();
        } catch (Exception e) {
            try {
                q = quizHashtable.get(item);
                subject.setText(q.type);
                description.setText(q.description);
                price.setText(String.valueOf(q.price));
                teacher.setText(q.email);
                rate.setText("5.0");
                i = 1;
                d.show();
            } catch (Exception e1) {

            }
        }
    }
    Meeting m;
    Quiz q;
    PDFView pdf;
    public void JoinTest() {
        d = new Dialog(getContext());
        d.setContentView(R.layout.test_info);
        subject = (TextView) d.findViewById(R.id.subject);
        description = (TextView) d.findViewById(R.id.description);
        price = (TextView) d.findViewById(R.id.price);
        date = (TextView) d.findViewById(R.id.date);
        TextView teacher = (TextView) d.findViewById(R.id.teacher);
        TextView rate = (TextView) d.findViewById(R.id.rate);
        Test m = testHashtable.get(item);
        subject.setText(m.subject);
        description.setText(m.description);
        price.setText(String.valueOf(m.price));
        teacher.setText(m.file.teacherEmail);
        rate.setText("5.0");
        Button test = (Button) d.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == test) {
                    showTest();
                }
            }

        });
        join = (Button) d.findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == join) {
                    d.dismiss();
                    Intent intent = new Intent(getContext(), add_pdf.class);
                    intent.putExtra("email", m.file.teacherEmail);
                    SharedPreferences sp = getContext().getSharedPreferences("email", 0);
                    intent.putExtra("student", sp.getString("email", null));
                    startActivity(intent);
                }
            }

        });
        d.show();
    }
    public void showTest() {
        d = new Dialog(getContext());
        d.setContentView(R.layout.pdf);
        pdf = (PDFView) d.findViewById(R.id.pdfView);

        String url = testHashtable.get(item).file.fileurl;
        new Retrieve().execute(url);
        d.show();
    }
    public class Retrieve extends AsyncTask<String,Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (Exception e){
                return  null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).load();
        }
    }
    Hashtable<String,Summary> summaryHashtable;
}
