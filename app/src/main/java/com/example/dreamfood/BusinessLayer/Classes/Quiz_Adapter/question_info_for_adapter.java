package com.example.dreamfood.BusinessLayer.Classes.Quiz_Adapter;

import java.util.ArrayList;

public class question_info_for_adapter {

    public String question;
    public ArrayList<String> answers;
    public String answer;
    public String url;
    public String type;
    public int index;
    public String userAns;
    public question_info_for_adapter(String q,String ans,ArrayList<String> answers,String url,String type,int index){
        this.url=url;
        question=q;
        answer=ans;
        this.answers=answers;
        this.index=index;
        this.type=type;
    }

}
