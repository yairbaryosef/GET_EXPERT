package com.example.dreamfood.Animation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

import com.example.dreamfood.R;
import com.example.dreamfood.Student_Controller.scrolling_activity;
public class anim {
    Dialog d;
    TextView grade;
    public anim(Context context,double number){
        d=new Dialog(context);
        d.setContentView(R.layout.grade_dialog);
        grade=d.findViewById(R.id.grade);
        startCounterAnim(number);
        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent=new Intent(context,scrolling_activity.class);
                context.startActivity(intent);
            }
        });
        d.show();

    }
    public void startCounterAnim(double number){
        final ValueAnimator animator=ValueAnimator.ofFloat(0, (float) number);
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(Double.valueOf(animator.getAnimatedValue().toString())>=56){
                    grade.setTextColor(R.color.colorgreen);
                }
                grade.setText(animator.getAnimatedValue().toString());
            }
        });
        animator.start();
    }
}
