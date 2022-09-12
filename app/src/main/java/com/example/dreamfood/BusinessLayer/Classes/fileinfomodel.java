package com.example.dreamfood.BusinessLayer.Classes;

public class fileinfomodel
{

    public String filename,fileurl,teacherEmail,subject;
    public int id=0,ans;

    public fileinfomodel(String filename, String fileurl) {
        this.filename = filename;
        this.fileurl = fileurl;

    }
    public fileinfomodel(){
         ans=0;
         subject="";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
