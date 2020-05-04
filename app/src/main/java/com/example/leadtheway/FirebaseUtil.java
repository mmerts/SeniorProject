package com.example.leadtheway;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil; // gerekli saçma değil
    public static ArrayList<myObject> mDeals;

    private FirebaseUtil(){};

    public static void openFbReference(String ref){
        if(firebaseUtil == null)
        {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase =FirebaseDatabase.getInstance();
            mDeals = new ArrayList<myObject>();
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

}
