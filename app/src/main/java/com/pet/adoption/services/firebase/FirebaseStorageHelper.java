package com.pet.adoption.services.firebase;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class FirebaseStorageHelper {

    public static Task<UploadTask.TaskSnapshot> uploadImage(String filename, Uri uri){
        TaskCompletionSource<UploadTask.TaskSnapshot> taskCompletionSource = new TaskCompletionSource<>();

        FirebaseStorage.getInstance()
                .getReference("images")
                .child(filename)
                .putFile(uri)
                .addOnFailureListener(e -> {
                    taskCompletionSource.setException(e);
                })
                .addOnSuccessListener(taskSnapshot -> {
                    taskCompletionSource.setResult(taskSnapshot);
                });

        return taskCompletionSource.getTask();
    }

    public static Task<File> downloadImage(String filename) {
        TaskCompletionSource<File> taskCompletionSource = new TaskCompletionSource<>();

        try {
            File file = File.createTempFile(filename, "");
            FirebaseStorage.getInstance()
                    .getReference("images")
                    .child(filename)
                    .getFile(file)
                    .addOnFailureListener(e -> {
                        taskCompletionSource.setException(e);
                    })
                    .addOnSuccessListener(task -> {
                        taskCompletionSource.setResult(file);
                    });
        } catch (IOException e) {
            taskCompletionSource.setException(e);
            throw new RuntimeException(e);
        }

        return taskCompletionSource.getTask();
    }

    public static void deleteImage(String filename){
        FirebaseStorage.getInstance()
                .getReference("images")
                .child(filename)
                .delete();
    }
}
