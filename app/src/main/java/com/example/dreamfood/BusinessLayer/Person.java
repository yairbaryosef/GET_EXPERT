package com.example.dreamfood.BusinessLayer;

public class Person {

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    private String uni;

    private int id;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Person(int id, String email, String pass, String name,String city,String phone) {
        this.id = id;
        this.email = email;
        this.name=name;
        password=pass;
        Loggedin=false;
        this.city=city;
        this.phone=phone;

    }
    public Person(int id, String email, String pass, String name,boolean Log,String city,String phone) {
        this.id = id;
        this.email = email;
        this.name=name;
        password=pass;
        Loggedin=Log;
        this.city=city;
        this.phone=phone;
    }
    public Person(){
        this.id = 0;
        this.email = "";
        this.name="";
        password="";
        city="";

        Loggedin=false;
    }

    private boolean Loggedin;

    public boolean isLoggedin() {
        return Loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        Loggedin = loggedin;
    }




}
