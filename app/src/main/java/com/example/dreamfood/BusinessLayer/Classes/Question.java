package com.example.dreamfood.BusinessLayer.Classes;

public class Question {
    public String Question;
    public String Answer;
    public String userAns;
    private String image_url;

    public String getImage_url() {
        try {
            if(image_url!=null) {
                return image_url;
            }
            else{
                return "";
            }
        }
        catch (Exception e){
            return "";
        }
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String image_url) {
        this.status = image_url;
    }

    public String EmailSubject;
    public Question(){
        Question="";
        Answer="";
        userAns="";
        status="";
        image_url="";
    }

}
