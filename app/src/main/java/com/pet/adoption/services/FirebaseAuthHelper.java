package com.pet.adoption.services;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthHelper {

    private FirebaseAuth instance;

    public FirebaseAuthHelper(){
        instance = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> signUp(String email, String password){
        TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();

        instance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        taskCompletionSource.setException(task.getException());
                        return;
                    }
                    taskCompletionSource.setResult(task.getResult());
                });

        return taskCompletionSource.getTask();
    }

    public Task<AuthResult> login(String email, String password){
        TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();

        instance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        taskCompletionSource.setException(task.getException());
                        return;
                    }
                    taskCompletionSource.setResult(task.getResult());
                });

        return taskCompletionSource.getTask();
    }

    public void signOut(){
        instance.signOut();
    }
}
