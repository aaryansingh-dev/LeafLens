package com.example.leaflens;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.leaflens.entity.Disease;
import com.example.leaflens.entity.News;
import com.example.leaflens.entity.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.DigestException;
import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseManager{

    private FirebaseFirestore db;

    private static final FirebaseManager instance = new FirebaseManager();
    private FirebaseManager()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public static FirebaseManager getInstance()
    {
        return instance;
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

        public interface fetchNewsListener
        {
            public void onNewsFetched(ArrayList<News> newsNewList);
        }
        public void fetchNews(ArrayList<News> newsList, fetchNewsListener callback)
        {
            CollectionReference newsCollection = db.collection("news");

            newsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        newsList.clear();
                        QuerySnapshot queryNewsSnapshot = task.getResult();
                        for(QueryDocumentSnapshot news: queryNewsSnapshot)
                        {
                            String newsId = (String) news.get("newsId");
                            String title = (String) news.get("newsTitle");
                            String author = (String) news.get("author");
                            String date = (String) news.get("date");
                            String text = (String) news.get("text");
                            Log.e("News", title);
                            newsList.add(new News(newsId, title, author, text, date));
                        }
                        callback.onNewsFetched(newsList);
                    }
                }
            });
        }

        public interface OnSearchListFetchListener
        {
            public void onListFetched(ArrayList<Disease> diseaseList);
        }

        public void fetchRelevantSearchDiseases(String query, ArrayList<Disease> diseaseArrayList, OnSearchListFetchListener callback)
        {
            Log.d("Firestore - search", "Starting search: " + query);
            query = query.trim();

            db.collection("diseases")
                    .orderBy("Name")
                    .startAt(query)
                    .endAt(query + "\uf8ff")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult() != null)
                        {
                            Log.d("Firestore - search", "Task successful, documents found: " + task.getResult().size());
                            diseaseArrayList.clear();
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                String name = document.getString("Name");
                                String severity = document.getString("Severity");
                                String diseaseCategory = document.getString("disease_category");

                                Disease disease = new Disease(name, severity);
                                disease.setDiseaseCategory(diseaseCategory);

                                Log.d("Firestore - search", "Document found: " + document.getId() + " Name: " + name);
                                diseaseArrayList.add(disease);
                                Log.e("Firestore-search", disease.getName());
                            }
                        }
                        Log.d("Firestore - search", "Starting callback");
                        callback.onListFetched(diseaseArrayList);
                    });
        }
}
