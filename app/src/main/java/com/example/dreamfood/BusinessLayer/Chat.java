package com.example.dreamfood.BusinessLayer;

import com.example.dreamfood.BusinessLayer.Classes.Messages;

import java.util.ArrayList;

public class Chat {

    public String teacher;
    public String student;
    public ArrayList<Messages> meesages;
    public int teacher_count_messages_not_seen;
    public int student_count_messages_not_seen;
    public Chat(){

        meesages=new ArrayList<>();
        teacher_count_messages_not_seen=0;
        student_count_messages_not_seen=0;
    }

}
