package com.example.dreamfood.DataBase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Data {
    public DatabaseReference data;
    public StorageReference storage;
    public Data(String pathData){
        data= FirebaseDatabase.getInstance().getReference(pathData);
    }
}
