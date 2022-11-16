package com.example.dreamfood.BusinessLayer;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;
import java.util.Objects;
import java.util.regex.Pattern;

public class PersonController {
    Hashtable<String,Person> persons;


    public PersonController(){
persons=new Hashtable<String,Person>();

    }

    public Hashtable<String,Person> getPersons() {
        LoadData();
        return persons;
    }
    public void LoadData(){

        try {



            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child("t").setValue("t");
            reference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {


                            Person person = dataSnapshot.getValue(Person.class);
                            String child=person.getEmail().substring(0,person.getEmail().indexOf('@'));

                            persons.put(child,person);

                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }

        catch (Exception e){

        }

    }
    public boolean Register(String email,String name,String Password,String city,String phone,String job){


            if (validate(email)&&validPhone(phone)) {
                persons.put(email, new Person(0, email, Password, name,city,phone));

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(job);


                if(job.equals("student")) {
                    Student student = new Student(email,name, Password, city, phone);
                    myRef.child(email).setValue(student);
                    return true;
                }
                else{
                    Teacher student = new Teacher(email, name,Password, city, phone);
                    myRef.child(email).setValue(student);
                    return true;
                }
            }
            return false;


    }
    public boolean RemovePerson(String email,String name,String Password,String phone,String city){
        if(persons.get(email)!=null) {
            persons.remove(new Person(0, email, Password, name,city,phone));
            return true;
        }
        return false;
    }
    public boolean Login(String email,String Password){
        Person person=persons.get(email);
        if(person!=null){
            if(Objects.equals(person.getPassword(), Password)){
                if(!person.isLoggedin()){
                     person.setLoggedin(true);
                     return true;
                }
            }
        }
        return false;
    }
    public boolean Logout(String email,String Password){
        Person person=persons.get(email);
        if(person!=null){
            if(Objects.equals(person.getPassword(), Password)){
                if(person.isLoggedin()){
                    person.setLoggedin(false);
                    return true;

                }
            }
        }
        return false;
    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        return emailStr.matches("[a-zA-Z0-9]+");


    }
    public boolean validPhone(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }


}
