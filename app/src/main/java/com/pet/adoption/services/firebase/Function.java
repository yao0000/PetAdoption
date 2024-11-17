package com.pet.adoption.services.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;


public class Function {

    public static void loadAndSetImage(Context context, ImageView iv, String filename){
        FirebaseStorageHelper.downloadImage(filename)
                .addOnFailureListener(e -> {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                })
                .addOnSuccessListener(file -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    iv.setImageBitmap(bitmap);
                    file.delete();
                });
    }

}
