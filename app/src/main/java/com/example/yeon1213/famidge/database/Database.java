package com.example.yeon1213.famidge.database;

import com.google.firebase.firestore.FirebaseFirestore;

public class Database {

    public static FirebaseFirestore mDB;

    public static FirebaseFirestore getInstance(){
        if(mDB ==null){
            mDB=FirebaseFirestore.getInstance();
        }
        return mDB;//
    }
}
