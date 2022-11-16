package com.example.dreamfood.BusinessLayer.Classes.Rating;

import java.util.ArrayList;
import java.util.Date;

public class Rating {
    public String subject;
    public ArrayList<rate> rates;
    public String teacher_email;
    public String type;
    public Date date;

    public Rating(){
rates=new ArrayList<>();
    }
}
