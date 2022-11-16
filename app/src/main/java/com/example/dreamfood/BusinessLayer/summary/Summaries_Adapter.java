package com.example.dreamfood.BusinessLayer.summary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.PDF_Controller.PDF;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Summaries_Adapter extends ArrayAdapter<Summary> {
    Student student=new Student();
    int i;
    public Summaries_Adapter(@NonNull Context context, ArrayList<Summary> arrayList) {
        super(context, R.layout.card_meeting,R.id.text3, arrayList);
        i=0;
    }
    public Summaries_Adapter(@NonNull Context context, ArrayList<Summary> arrayList, int t) {
        super(context, R.layout.card_meeting,R.id.text3, arrayList);
        i=1;
    }
   Strings con=new Strings();
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Summary meeting=getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_meeting, parent, false);
        }
        CardView cardView=convertView.findViewById(R.id.card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==cardView) {


                    Intent intent=new Intent(getContext(), PDF.class);
                    intent.putExtra("url",meeting.url);
                    getContext().startActivity(intent);
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
        subject.setText(meeting.name);



        TextView price=convertView.findViewById(R.id.price);
        price.setText(String.valueOf(meeting.cost)+"$");

Gson gson=new Gson();
        SharedPreferences sp=getContext().getSharedPreferences(constants.student,0);

String st=sp.getString(constants.student,null);
student=gson.fromJson(st,Student.class);








        return super.getView(position,convertView, parent);
    }
    Strings constants=new Strings();

}
