package com.vnpt.ic.sample.intergrate.ekyc;

import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_BACK_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.OCR_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.QR_CODE_RESULT;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vnpt.ic.sample.intergrate.ekyc.databinding.ActivityLogResultBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;


public class LogActivity extends AppCompatActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final ActivityLogResultBinding binding = ActivityLogResultBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      final Intent intent = getIntent();

      setLogResultSafe(binding.logOcr, intent.getStringExtra(OCR_RESULT));
      setLogResultSafe(binding.logLivenessCardFront, intent.getStringExtra(LIVENESS_CARD_FRONT_RESULT));
      setLogResultSafe(binding.logLivenessCardRear, intent.getStringExtra(LIVENESS_CARD_BACK_RESULT));
      setLogResultSafe(binding.logCompare, intent.getStringExtra(COMPARE_FACE_RESULT));
      setLogResultSafe(binding.logLivenessFace, intent.getStringExtra(LIVENESS_FACE_RESULT));
      setLogResultSafe(binding.logMaskFace, intent.getStringExtra(MASKED_FACE_RESULT));
      setLogResultSafe(binding.logQr, intent.getStringExtra(QR_CODE_RESULT));


      binding.close.setOnClickListener(v -> finish());

      binding.copiedAll.setOnClickListener(v -> {
         copy(intent.getStringExtra(OCR_RESULT));
         copy(intent.getStringExtra(LIVENESS_CARD_FRONT_RESULT));
         copy(intent.getStringExtra(LIVENESS_CARD_BACK_RESULT));
         copy(intent.getStringExtra(COMPARE_FACE_RESULT));
         copy(intent.getStringExtra(LIVENESS_FACE_RESULT));
         copy(intent.getStringExtra(MASKED_FACE_RESULT));
         copy(intent.getStringExtra(QR_CODE_RESULT));
      });
   }

   private void copy(final String data) {
      if (!TextUtils.isEmpty(data)) {
         ShareUtils.copyToClipboard(LogActivity.this, data);
      }
   }

   private void setLogResultSafe(final LogResultView view, final String logResult) {
      view.setVisibility(View.VISIBLE);
      if (!TextUtils.isEmpty(logResult)) {
         if (isJson(logResult)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonReader reader = new JsonReader(new StringReader(logResult));
            reader.setLenient(true); // Allow lenient parsing
            JsonElement je = JsonParser.parseReader(reader);
            String prettyJsonString = gson.toJson(je);
            view.setLogResult(prettyJsonString);
         } else {
            view.setLogResult(logResult);
         }
      } else {
         view.setVisibility(View.GONE);
      }
   }

   private boolean isJson(String str) {
      if (str == null || str.isEmpty()) {
         return false;
      }
      try {
         new JSONObject(str);
         return true;
      } catch (JSONException e) {
         try {
            // Try parsing as a JSON array as well
            new org.json.JSONArray(str);
            return true;
         } catch (JSONException e2) {
            return false;
         }
      }
   }
}