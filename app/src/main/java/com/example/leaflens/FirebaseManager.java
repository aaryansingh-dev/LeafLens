package com.example.leaflens;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.leaflens.entity.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.util.HashMap;

public class FirebaseManager{

    private FirebaseFirestore db;
    public FirebaseManager()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addProfile(Profile profile)
    {
        DocumentReference userRef = db.collection("profiles").document(profile.getDeviceID());
        HashMap<String, String> data = new HashMap<>();

        data.put("deviceID", profile.getDeviceID());
        data.put("name", profile.getName());
        data.put("DOB", profile.getDOB());
        data.put("email", profile.getEmail());
        data.put("phoneNumber", profile.getPhoneNumber());
        data.put("imageURL", profile.getProfileImageURL());

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
                        Log.e("User", "Error in saving profile");
                    }
                });
        }

        public interface ProfileFetchListener
        {
            public void onProfileFetched(Profile profile);
            public void onFailure(String error);
        }

        public void fetchProfile(String deviceID, ProfileFetchListener callback)
        {
            DocumentReference profileReference = db.collection("profiles").document(deviceID);

            profileReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                   if(error != null)
                   {
                       Log.e("Firestore", "Error fetching details");
                       callback.onFailure("Error fetching details");
                       return;
                   }
                   if(value != null)
                   {
                        Profile profileFetched = value.toObject(Profile.class);
                        callback.onProfileFetched(profileFetched);
                   }
                }
            });
        }
}
