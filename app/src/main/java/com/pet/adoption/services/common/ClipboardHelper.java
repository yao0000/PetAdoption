package com.pet.adoption.services.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class ClipboardHelper {

    public static void copyTextToClipboard(Context context, String text){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard == null){
            Toast.makeText(context, "Failed to copy form", Toast.LENGTH_LONG).show();
            return;
        }

        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Form copied to clipboard", Toast.LENGTH_SHORT).show();
    }

}