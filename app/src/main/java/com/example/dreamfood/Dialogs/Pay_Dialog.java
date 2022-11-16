package com.example.dreamfood.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dreamfood.BusinessLayer.Classes.Coin;
import com.example.dreamfood.BusinessLayer.Classes.Rating.Rating;
import com.example.dreamfood.BusinessLayer.Classes.Rating.rate;
import com.example.dreamfood.BusinessLayer.Classes.Strings;
import com.example.dreamfood.BusinessLayer.Meeting;
import com.example.dreamfood.BusinessLayer.Student;
import com.example.dreamfood.BusinessLayer.summary.Summary;
import com.example.dreamfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pay_Dialog {
    public Strings con=new Strings();
    private boolean isPaid=false;
   public Pay_Dialog(Context context, Meeting meeting,Student student){
       Dialog d = new Dialog(context);
       d.setContentView(R.layout.pay_or_deal);
       LottieAnimationView lottieAnimationView, lottieAnimationView2;
       lottieAnimationView = d.findViewById(R.id.lottie);
       lottieAnimationView.setMinAndMaxProgress(0.5f, 1.0f);
       lottieAnimationView.playAnimation();
       this.st=student;
       lottieAnimationView2 = d.findViewById(R.id.lottie2);
       lottieAnimationView2.setMinAndMaxProgress(0.5f, 1.0f);
       lottieAnimationView2.playAnimation();
       CardView cardView1 = d.findViewById(R.id.coin);
       cardView1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String string = context.getSharedPreferences(con.student, 0).getString(con.student, null);


               meet=meeting;
               isPaid=findCoin(con.Meeting);

               if(isPaid) {
                   student.meetings.add(meeting);
                   Rating rating = new Rating();
                   rating.teacher_email = meeting.email;
                   rating.subject = meeting.type;
                   rating.type = con.Meeting;
                   rate rate1 = new rate("Rate Quality");
                   rating.rates.add(rate1);
                   student.ratings.add(rating);
                   Toast.makeText(context, "meeting saved", Toast.LENGTH_SHORT).show();
                   DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.student);
                   String ema=context.getSharedPreferences("email",0).getString("email",null);
                   databaseReference.child(ema).setValue(student);
               }
           }
       });
       d.show();
   }
   private Student st;
   private Meeting meet;
    private boolean findCoin(String type) {
       for(Coin coin:st.coins){
         if(type.equals(con.Meeting)) {
             if (coin.email.equals(meet.email)) {

                 return true;

             }
         }
       }
       return false;
    }
    private Summary summary;
    public Pay_Dialog(Context context, Summary sum, Student student){
        Dialog d = new Dialog(context);
        d.setContentView(R.layout.pay_or_deal);
        LottieAnimationView lottieAnimationView, lottieAnimationView2;
        lottieAnimationView = d.findViewById(R.id.lottie);
        lottieAnimationView.setMinAndMaxProgress(0.5f, 1.0f);
        lottieAnimationView.playAnimation();
        this.st=student;
        lottieAnimationView2 = d.findViewById(R.id.lottie2);
        lottieAnimationView2.setMinAndMaxProgress(0.5f, 1.0f);
        lottieAnimationView2.playAnimation();
        CardView cardView1 = d.findViewById(R.id.coin);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = context.getSharedPreferences(con.student, 0).getString(con.student, null);


                summary=sum;
                isPaid=findCoin(con.Meeting);

                if(isPaid) {
                    student.summaries.add(sum);
                    Rating rating = new Rating();
                    rating.teacher_email = sum.email;
                    rating.subject = sum.name;
                    rating.type = con.Meeting;
                    rate rate1 = new rate("Rate Quality");
                    rating.rates.add(rate1);
                    student.ratings.add(rating);
                    Toast.makeText(context, "meeting saved", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(con.student);
                    String ema=context.getSharedPreferences("email",0).getString("email",null);
                    databaseReference.child(ema).setValue(student);
                }
            }
        });
        d.show();
    }
}
