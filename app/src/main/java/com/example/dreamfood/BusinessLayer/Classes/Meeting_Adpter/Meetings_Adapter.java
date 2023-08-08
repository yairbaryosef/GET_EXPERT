package com.example.dreamfood.BusinessLayer.Classes.Meeting_Adpter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PresentaionLayer.Layouts_Controllers.Dialogs.Pay_Dialog;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Meetings_Adapter extends ArrayAdapter<Meeting> {
    Student student=new Student();
    int i;
    public Meetings_Adapter(@NonNull Context context, ArrayList<Meeting> arrayList) {
        super(context, R.layout.card_meeting,R.id.text3, arrayList);
        i=0;
    }
    public Meetings_Adapter(@NonNull Context context, ArrayList<Meeting> arrayList,int t) {
        super(context, R.layout.card_meeting,R.id.text3, arrayList);
        i=1;
    }
   Strings con=new Strings();
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Meeting meeting=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_meeting, parent, false);
        }
        CardView cardView=convertView.findViewById(R.id.card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==cardView) {
                    if (i == 0) {
                        Pay_Dialog pay_dialog=new Pay_Dialog(getContext(),meeting,student);
                    }
                    else {

                        createMyPDF(meeting);
                    }
                }
                else{

                }
            }
        });
        CircleImageView imageView= convertView.findViewById(R.id.image);
        FirebaseDatabase.getInstance().getReference(con.teacher).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(meeting.email)){
                        Picasso.get().load(dataSnapshot.getValue(Teacher.class).profile_url).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView subject=(TextView) convertView.findViewById(R.id.subject);
        subject.setText(meeting.type);
        TextView des=convertView.findViewById(R.id.description);
        des.setText(meeting.description);
        TextView date=convertView.findViewById(R.id.date);
        String form = String.valueOf(DateFormat.format("MM-dd-yyyy", meeting.startdate)) + " " + (String) DateFormat.format("hh:mm aa", meeting.startdate);


        date.setText(form);
        TextView price=convertView.findViewById(R.id.price);
        price.setText(String.valueOf(meeting.price)+"$");

Gson gson=new Gson();
        SharedPreferences sp=getContext().getSharedPreferences(constants.student,0);

String st=sp.getString(constants.student,null);
student=gson.fromJson(st,Student.class);








        return super.getView(position,convertView, parent);
    }
    Strings constants=new Strings();
    public void createMyPDF(Meeting meeting){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = "meeting subject:"+meeting.type+"\n"+" start at:"+meeting.format+"\n"+" by: "+meeting.email+"."+"\n"+"meeting link: "+meeting.link+"."+"\n"+"meeting password"+meeting.pass+".";
        int x = 10, y=25;

        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/"+meeting.email+" "+meeting.type+".pdf";

        File myFile = new File(myFilePath);


        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
            student.meetings.remove(meeting);
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(constants.student).child(student.getEmail());
            databaseReference.setValue(student);
            Toast.makeText(getContext(), myFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        myPdfDocument.close();
    }
}
