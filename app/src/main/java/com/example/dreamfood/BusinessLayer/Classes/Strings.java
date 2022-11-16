package com.example.dreamfood.BusinessLayer.Classes;

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
    public String emails(String users){

        String emailUsers="";
        for(int i=0;i<users.length();i++){

            boolean a=true;
            while(!users.substring(i,i+1).equals(" ")){
                if(users.substring(i,i+1).equals("@")){
                    a=false;
                }
                if(a){
                    emailUsers=emailUsers+users.substring(i,i+1);
                }

                i++;
            }
            emailUsers=emailUsers+" ";

        }
        return  emailUsers;
    }


}
