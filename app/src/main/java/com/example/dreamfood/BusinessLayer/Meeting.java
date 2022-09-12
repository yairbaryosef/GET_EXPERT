package com.example.dreamfood.BusinessLayer;

import java.util.ArrayList;
import java.util.Date;

public class Meeting {
    public String link;
    public String ID;
    public int number;
    public String pass;
    public ArrayList<String> teachers;
   public ArrayList<String> students;
public String email;

    public String description;
   public Date startdate;
    public int time;
 public String format;
    public String type;
    public double price;
    public String place;
    public int limitStudents;
    public Meeting(String link,String ID,String pass,ArrayList<String> teachers,String description,Date date,int time,String type,double p,String place,int limit){
        this.link=link;
        this.ID=ID;
        this.pass=pass;
        this.teachers=teachers;
        this.students=new ArrayList<String>();
        this.description=description;
        startdate=date;
        this.time=time;
        this.type=type;
        this.price=p;
        this.place=place;
        limitStudents=limit;

    }
    public Meeting(){
        link="https://us04web.zoom.us/meeting?_x_zm_rtaid=z9mT2rGaQXe9f7z3ZcwXDg.1658856315535.1f19369aea0160c8a01341b934be0ed5&_x_zm_rhtaid=82#/upcoming";
        teachers=new ArrayList<>();
        students=new ArrayList<>();
        description="";
        startdate=new Date();
        time=0;
        type="";

        price=0;
        place="";
        limitStudents=0;
        email="";
        number=0;
    }

}
