package com.example.dreamfood.Student_Controller;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dreamfood.BusinessLayer.Classes.AdapterSubject;
import com.example.dreamfood.BusinessLayer.Classes.MyAdapter;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Quiz;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Test;
import com.example.dreamfood.PDF_Controller.add_pdf;
import com.example.dreamfood.R;
import com.example.dreamfood.Materials.Quiz.quiz_list;
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
import java.util.Hashtable;

public class search extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MyAdapter myAdapter;
    Dialog d;
    Quiz q;
    int i = 0;

    Spinner spinner, spinner2;
    String item = "";
    Button join;
    ArrayList<String> jobs = new ArrayList<>();
    ArrayList<String> quizz = new ArrayList<>();
    Hashtable<String, Meeting> meetingHashtable = new Hashtable<String, Meeting>();
    Hashtable<String, Quiz> quizHashtable = new Hashtable<String, Quiz>();
    Hashtable<String, Test> testHashtable = new Hashtable<String, Test>();
    String meet = "", test = "", quiz = "";
    ArrayList<String> array = new ArrayList<String>();
    ArrayList<String> array2 = new ArrayList<String>();
    ArrayList<AdapterSubject> arrayList;
    SearchView searchView;
    PDFView pdf;
    ArrayAdapter<String> dataAdapter, dataAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        spinner = (Spinner) findViewById(R.id.spinner);
        meet = getIntent().getStringExtra("meet");
        test = getIntent().getStringExtra("test");
        quiz = getIntent().getStringExtra("quiz");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(con.student);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = getSharedPreferences("email", 0).getString("email", null);
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
        searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setQueryHint("type here to search");
        arrayList = new ArrayList<AdapterSubject>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                array = new ArrayList<String>();
                array.add("choose");
                for (String adapterSubject : jobs) {
                    if (adapterSubject.toLowerCase().contains(newText.toLowerCase()) && !adapterSubject.equals("choose")) {
                        array.add(adapterSubject);
                    }
                }
                dataAdapter = new ArrayAdapter<String>(search.this, android.R.layout.simple_list_item_1, array);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(search.this);
                return false;
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;


        myRef = database.getReference("Meetings");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        try {


                            Meeting m = snapshot1.getValue(Meeting.class);

                            meetingHashtable.put(m.type + " " + m.email + " meeting", m);

                        } catch (Exception e) {
                            Toast.makeText(search.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (Meeting m : meetingHashtable.values()) {
                        jobs.add(m.type + " " + m.email + " meeting");
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef = database.getReference("Quiz");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    try {

                        for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                            Quiz q = dataSnapshot.getValue(Quiz.class);

                            quizHashtable.put(q.type + " " + q.email + " quiz", q);
                        }
                    } catch (Exception e) {
                        Toast.makeText(search.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
                for (Quiz m : quizHashtable.values()) {
                    jobs.add(m.type + " " + m.email + " quiz");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef = database.getReference("Test");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                        try {


                            Test m = dataSnapshot.getValue(Test.class);

                            testHashtable.put(m.subject + " " + m.file.teacherEmail + " test", m);

                        } catch (Exception e) {
                            Toast.makeText(search.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                for (Test m : testHashtable.values()) {
                    jobs.add(m.subject + " " + m.file.teacherEmail + " test");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        jobs = new ArrayList<String>();
        jobs.add("choose");


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jobs);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
       String[] permission= {Manifest.permission.RECEIVE_SMS};
       requestPermissions(permission,1000);

    }

    TextView subject, description, price, date;

    public void showTest() {
        d = new Dialog(this);
        d.setContentView(R.layout.pdf);
        pdf = (PDFView) d.findViewById(R.id.pdfView);

        String url = testHashtable.get(item).file.fileurl;
        new Retrieve().execute(url);
        d.show();
    }

    public void JoinTest() {
        d = new Dialog(this);
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
                    Intent intent = new Intent(search.this, add_pdf.class);
                    intent.putExtra("email", m.file.teacherEmail);
                    SharedPreferences sp = getSharedPreferences("email", 0);
                    intent.putExtra("student", sp.getString("email", null));
                    startActivity(intent);
                }
            }

        });
        d.show();
    }

    public void createaddquestiondislogQuiz() {
        d = new Dialog(this);
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
                        Intent intent = new Intent(search.this, quiz_list.class);
                        intent.putExtra("email", q.email);
                        intent.putExtra("subject", q.type);
                        startActivity(intent);
                    }
                    if (i == 0) {
                        d.dismiss();
                   st.meetings.add(meetingHashtable.get(item));
                   Toast.makeText(search.this, "meeting saved", Toast.LENGTH_SHORT).show();
                   FirebaseDatabase.getInstance().getReference(con.student).child(con.emailStart(st.getEmail())).setValue(st);
                   Toast.makeText(search.this, st.meetings.get(0).link, Toast.LENGTH_SHORT).show();

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
    public void checkPremission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(search.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(search.this, new String[]{Manifest.permission.SEND_SMS}, 123);
                String message = "hey " + st.getEmail() + ". you are You signed up for the class at " + m.startdate.toString() + " with " + m.email + ". Here is the link to the meeting." + "\n" + m.link;

                sendSms(st.getPhone(),message);

            } else {

                ActivityCompat.requestPermissions(search.this, new String[]{Manifest.permission.SEND_SMS}, 123);
            }
        }
    }

    Meeting m;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void sendSms(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    public void sendMessage() {

        SmsManager smsManager = SmsManager.getDefault();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String message = "hey " + st.getEmail() + ". you are You signed up for the class at " + m.startdate.toString() + " with " + m.email + ". Here is the link to the meeting." + "\n" + m.link;
        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                      int[] grantResults);
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(search.this,new String[]{Manifest.permission.READ_SMS},102);
            }
            String phone = telephonyManager.getLine1Number();
            Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
            smsManager.sendTextMessage(st.getPhone(),null,message,null,null);
            //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "phone is not legal", Toast.LENGTH_SHORT).show();
        }
    }
    Student st;
    Strings con=new Strings();

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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            item = parent.getItemAtPosition(position).toString();
            if (!item.equals("choose")) {
                if(testHashtable.containsKey(item)){
                    JoinTest();
                }
                else {


                    createaddquestiondislogQuiz();
                }
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}