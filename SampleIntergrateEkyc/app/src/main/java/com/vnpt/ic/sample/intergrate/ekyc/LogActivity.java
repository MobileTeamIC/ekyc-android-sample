package com.vnpt.ic.sample.intergrate.ekyc;

import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_BACK_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.OCR_RESULT;

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
import com.vnpt.ic.sample.intergrate.ekyc.databinding.ActivityLogResultBinding;

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

      binding.close.setOnClickListener(v -> finish());

      binding.copiedAll.setOnClickListener(v -> {
         copy(intent.getStringExtra(OCR_RESULT));
         copy(intent.getStringExtra(LIVENESS_CARD_FRONT_RESULT));
         copy(intent.getStringExtra(LIVENESS_CARD_BACK_RESULT));
         copy(intent.getStringExtra(COMPARE_FACE_RESULT));
         copy(intent.getStringExtra(LIVENESS_FACE_RESULT));
         copy(intent.getStringExtra(MASKED_FACE_RESULT));
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
         Gson gson = new GsonBuilder().setPrettyPrinting().create();
         JsonElement je = new JsonParser().parse(logResult);
         String prettyJsonString = gson.toJson(je);
         view.setLogResult(prettyJsonString);
      } else {
         view.setVisibility(View.GONE);
      }
   }
}