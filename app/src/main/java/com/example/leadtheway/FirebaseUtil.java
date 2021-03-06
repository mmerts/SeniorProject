package com.example.leadtheway;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
/*
Firebase connection managed more easily using this class.
 */
public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil; // gerekli saçma değil
    public static ArrayList<Museum> museumArray;

    private FirebaseUtil(){};

    public static void openFbReference(String ref){
        if(firebaseUtil == null)
        {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase =FirebaseDatabase.getInstance();
        }
        museumArray = new ArrayList<Museum>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

}
