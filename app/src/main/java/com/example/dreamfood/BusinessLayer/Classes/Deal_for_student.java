package com.example.dreamfood.BusinessLayer.Classes;

import java.util.Calendar;
import java.util.Date;

public class Deal_for_student {
    public String name;
    public String email;
    public Date startDate;
    public Date endDate;
    public Deal_for_student(){
        startDate= Calendar.getInstance().getTime();
    }
}
