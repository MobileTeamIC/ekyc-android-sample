package com.vnpt.ic.sample.intergrate.ekyc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class ShareUtils {
   /**
    * Copy the text to clipboard, and indicate to the user whether the operation was completed
    * successfully using a Toast.
    *
    * @param context the context to use
    * @param text    the text to copy
    */
   public static void copyToClipboard(@NonNull final Context context, final String text) {
      final ClipboardManager clipboardManager =
           ContextCompat.getSystemService(context, ClipboardManager.class);

      if (clipboardManager == null) {
         Toast.makeText(context, "Thao tác bị từ chối bởi hệ thống", Toast.LENGTH_LONG).show();
         return;
      }

      clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
      Toast.makeText(context, "Đã sao chép vào clipboard", Toast.LENGTH_SHORT).show();
   }
}
