package com.pet.adoption.services.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

public class FirebaseStorageHelper {

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
}
