package com.example.restaurant_firebase.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseServices {
    private static FirebaseAuth firebaseAuth;
    private static StorageReference firebaseStorage;
    private static DatabaseReference databaseReference;

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static StorageReference getFirebaseStorage() {
        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return firebaseStorage;
    }

    public static DatabaseReference getDatabaseReference() {
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
