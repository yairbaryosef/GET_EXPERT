package com.example.dreamfood.BusinessLayer.Classes;

public class AdapterSubject {
    public AdapterSubject(String subject,String date,boolean sender) {
        this.subject = subject;
        this.date=date;
        this.sender=sender;
    }
   public boolean sender;
    public String date;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String subject;


}
