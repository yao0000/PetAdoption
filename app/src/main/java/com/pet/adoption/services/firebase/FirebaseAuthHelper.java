package com.pet.adoption.services.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthHelper {

    public static boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static Task<AuthResult> signUp(String email, String password){
        TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        taskCompletionSource.setException(task.getException());
                        return;
                    }
                    taskCompletionSource.setResult(task.getResult());
                });

        return taskCompletionSource.getTask();
    }

    public static Task<AuthResult> login(String email, String password){
        TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        taskCompletionSource.setException(task.getException());
                        return;
                    }
                    taskCompletionSource.setResult(task.getResult());
                });

        return taskCompletionSource.getTask();
    }

    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

}