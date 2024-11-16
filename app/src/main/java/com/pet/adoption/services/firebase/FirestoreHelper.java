package com.pet.adoption.services.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pet.adoption.entities.Account;
import com.pet.adoption.entities.PetInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {

    public static Task<List<PetInfo>> loadPetList(){
        TaskCompletionSource<List<PetInfo>> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseFirestore.getInstance()
                .collection("pets")
                .get()
                .addOnFailureListener(e -> {
                    taskCompletionSource.setException(e);
                })
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        taskCompletionSource.setException(new Exception("Pet list is empty"));
                        return;
                    }

                    List<PetInfo> list = new ArrayList<>();

                    for(QueryDocumentSnapshot document : task.getResult())
                        list.add(document.toObject(PetInfo.class));

                    taskCompletionSource.setResult(list);
                });

        return taskCompletionSource.getTask();
    }

    public static Task<List<String>> loadSavedList() {
        TaskCompletionSource<List<String>> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseFirestore.getInstance()
                .collection("save")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnFailureListener(e -> {
                    taskCompletionSource.setException(e);
                })
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        taskCompletionSource.setException(new Exception("Document not found"));
                        return;
                    }

                    // Get the list of maps stored under 'petInfoList' field
                    Object rawData = snapshot.get("list");

                    if (rawData == null) {
                        taskCompletionSource.setException(new Exception("petInfoList not found"));
                        return;
                    }
                    List<String> savedList = (List<String>) rawData;
                    taskCompletionSource.setResult(savedList);
                });

        return taskCompletionSource.getTask();
    }


    public static Task<Account> loadAccount(String collection, String document) {
        TaskCompletionSource<Account> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseFirestore.getInstance()
                .collection(collection)
                .document(document)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        taskCompletionSource.setException(new Exception("Document not found"));
                        return;
                    }

                    // Deserialize the document into the specified class (e.g., Account)
                    Account acc = snapshot.toObject(Account.class);  // Convert the Firestore document to an Account object

                    if (acc == null) {
                        // If deserialization fails (e.g., fields don't match or data is missing)
                        taskCompletionSource.setException(new Exception("Failed to load account data"));
                        return;
                    }

                    // Set the result to the deserialized object, not the raw data
                    taskCompletionSource.setResult(acc);
                })
                .addOnFailureListener(e -> {
                    // If there's a failure (e.g., network error, permission issue)
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

    public static Task<Void> upload(String collection, String document, Object object){
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseFirestore.getInstance()
                .collection(collection)
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

    public static Task<Void> favorite(String uid, String mode){

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DocumentReference ref = FirebaseFirestore.getInstance()
                .collection("save")
                .document(FirebaseAuth.getInstance().getUid());

        ref.get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        List<String> list = new ArrayList<>();
                        list.add(uid);
                        Map<String, Object> map = new HashMap<>();
                        map.put("list", list);
                        ref.set(map)
                                .addOnSuccessListener(aVoid -> {
                                    taskCompletionSource.setResult(aVoid);
                                })
                                .addOnFailureListener(e -> {
                                    taskCompletionSource.setException(e);
                                });
                        return;
                    }
                    List<String> uidList = (List<String>) snapshot.get("list");
                    if (uidList == null)
                        uidList = Collections.emptyList();

                    if (mode.equals("Save")){ // add to firebase
                        if (!uidList.contains(uid)){
                            ref.update("list", FieldValue.arrayUnion(uid))
                                    .addOnSuccessListener(aVoid -> {
                                        taskCompletionSource.setResult(aVoid);
                                    })
                                    .addOnFailureListener(e -> {
                                        taskCompletionSource.setException(e);
                                    });
                        }
                    }
                    else{ // remove from firebase
                        ref.update("list", FieldValue.arrayRemove(uid))
                                .addOnSuccessListener(aVoid -> {
                                    taskCompletionSource.setResult(aVoid);
                                })
                                .addOnFailureListener(e -> {
                                    taskCompletionSource.setException(e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
    }

}