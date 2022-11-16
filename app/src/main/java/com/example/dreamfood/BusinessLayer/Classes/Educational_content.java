package com.example.dreamfood.BusinessLayer.Classes;

import java.util.ArrayList;

public class Educational_content {
    public Educational_content(){
        targets=new ArrayList<>();
    }
    private ArrayList<String> targets;
    private String country;
    private String city;
    private String uni;

    public ArrayList<String> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<String> targets) {
        this.targets = targets;
    }
}
