package com.pet.adoption.services;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHelper {

    private final FirebaseFirestore instance;

    public FirestoreHelper(){
        instance = FirebaseFirestore.getInstance();
    }

    public Task<Void> upload(String collection, String document, Object object){
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        instance.collection(collection)
                .document(document)
                .set(object)
                .addOnSuccessListener(aVoid -> {
                    taskCompletionSource.setResult(aVoid);
                })
                .addOnFailureListener(e -> {
                   taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }
}
