package com.example.dreamfood.BusinessLayer.Classes;

import android.content.Context;

public class Strings {
    public  final  String teacher="teacher";
    public  final  String student="student";
    public  final  String test="Test";
    public  final  String coin="Coin";
    public final String open_question="Open Question";
    public final String question_with_options="Question With options";
    public final String school="School";
    public  final  String Quiz="Quiz";
    public  final  String subject="subjects";
    public  final  String summary="Summary";
    public final String recording="Recordings";
    public final String video="Videos";
        public  final  String Meeting="Meetings";
    public  final  String chat="Chat";
    public final String rating="Rating";
    public final String follow="follow";

    public String emailStart(String email){
      boolean a=false;
      for(int i=0;i<email.length();i++){
          if(email.charAt(i)=='@'){
              a=true;
              break;
          }
      }
      if(a) {
          System.out.println(email);
          return email.substring(0, email.indexOf('@'));
      }
        return "email";
    }
    public String getEmail(Context context){
        return context.getSharedPreferences("email",0).getString("email",null);
    }



}
