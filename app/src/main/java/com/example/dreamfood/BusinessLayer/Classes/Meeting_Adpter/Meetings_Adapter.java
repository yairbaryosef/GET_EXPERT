package com.example.dreamfood.BusinessLayer.Classes.Meeting_Adpter;

import android.app.Dialog;
import android.content.Context;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.Teacher;
import com.example.dreamfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Meetings_Adapter extends ArrayAdapter<Meeting> {
    Student student=new Student();
    int i;
    public Meetings_Adapter(@NonNull Context context, ArrayList<Meeting> arrayList) {
        super(context, R.layout.card_meeting,R.id.text3, arrayList);
        i=0;
    }
    public Meetings_Adapter(@NonNull Context context, ArrayList<Meeting> arrayList,int i) {
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
                        Dialog d = new Dialog(getContext());
                        d.setContentView(R.layout.pay_or_deal);
                        LottieAnimationView lottieAnimationView, lottieAnimationView2;
                        lottieAnimationView = d.findViewById(R.id.lottie);
                        lottieAnimationView.setMinAndMaxProgress(0.5f, 1.0f);
                        lottieAnimationView.playAnimation();
                        lottieAnimationView2 = d.findViewById(R.id.lottie2);
                        lottieAnimationView2.setMinAndMaxProgress(0.5f, 1.0f);
                        lottieAnimationView2.playAnimation();
                        CardView cardView1 = d.findViewById(R.id.card);
                        cardView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String string = getContext().getSharedPreferences(con.student, 0).getString(con.student, null);
                                Student st = new Gson().fromJson(string, Student.class);
                                st.meetings.add(meeting);
                                Toast.makeText(getContext(), "meeting saved", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference(con.student).child(con.emailStart(st.getEmail())).setValue(st);

                            }
                        });
                        d.show();
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









        return super.getView(position,convertView, parent);
    }
}
