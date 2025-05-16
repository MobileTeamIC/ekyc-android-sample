package com.vnpt.ic.sample.intergrate.ekyc;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {
   public interface InputCallback {
      void onInput(String input);
   }

   public static void showInputDialog(Context context, InputCallback callback) {
      MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
      builder.setTitle("Nhập mã hash ảnh mặt trước");

      final EditText input = new EditText(context);
      input.setHint("Nhập mã hash ảnh mặt trước");
//    Sử dụng nếu bạn muốn ngay lập tức lấy hash ảnh front
//       input.setText(AppCode.HASH_FRONT);

      builder.setView(input);

      builder.setPositiveButton("OK", (dialog, which) -> {
         String inputText = input.getText().toString();
         if (inputText.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập mã hash", Toast.LENGTH_SHORT).show();
         } else {
            callback.onInput(inputText);
         }
      });

      builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
      builder.show();
   }
}
