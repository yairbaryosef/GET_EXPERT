package com.example.dreamfood.BusinessLayer.Classes;

public class Strings {
    public  final  String teacher="teacher";
    public  final  String student="student";
    public  final  String test="Test";
    public final String school="School";
    public  final  String Quiz="Quiz";
    public  final  String subject="subjects";
    public  final  String summary="Summary";
    public final String recording="Recordings";
    public final String video="Videos";
        public  final  String Meeting="Meetings";
    public  final  String chat="Chat";
    public String emailStart(String email){
        if(email.contains("@")) {
            return email.substring(0, email.indexOf('@'));
        }
        return email;
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
