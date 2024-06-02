package com.example.leaflens;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.leaflens.entity.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirebaseManager {

    private FirebaseFirestore db;
    public FirebaseManager()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addProfile(Profile profile)
    {
        DocumentReference userRef = db.collection("profiles").document(profile.getEmail());
        HashMap<String, String> data = new HashMap<>();

        data.put("Name", profile.getName());
        data.put("Email", profile.getEmail());
        data.put("DOB", profile.getDOB());
        data.put("Phone number", profile.getPhoneNumber());
        data.put("ImageURL", profile.getProfileImageURL());

        userRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("User", "User profile saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("User", "Error in saving profile");
                    }
                });
    }
}
