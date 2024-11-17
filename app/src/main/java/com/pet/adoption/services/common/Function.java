package com.pet.adoption.services.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.ArrayRes;

import java.io.File;
import java.util.Objects;

public class Function {

    public static String getFilenameFromUri(Context context, Uri uri){
        String fileName = null;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }
        return fileName;
    }

    public static String getFileTypeFromUri(Context context, Uri uri){
        String mimeType = null;
        ContentResolver contentResolver = context.getContentResolver();

        if (Objects.equals(uri.getScheme(), "content")) {
            mimeType = contentResolver.getType(uri);
        }
        else {
            String filePath = uri.getPath();
            if (filePath != null) {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(filePath)).toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
        }
        assert mimeType != null;
        return "." + mimeType.substring(mimeType.indexOf("/") + 1);
    }

    public static String[] getArray(Context con, @ArrayRes int id){
        return con.getResources().getStringArray(id);
    }
}
