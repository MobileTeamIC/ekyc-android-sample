package com.vnpt.ic.sample.intergrate.ekyc;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class LogResultView extends LinearLayout {
   private TextView titleView;
   private TextView logResultView;
   private TextView copiedView;
   private TextView copyLogIdView;

   public LogResultView(Context context) {
      this(context, null, 0, 0);
   }

   public LogResultView(Context context, @Nullable AttributeSet attrs) {
      this(context, attrs, 0, 0);
   }

   public LogResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      this(context, attrs, defStyleAttr, 0);
   }

   public LogResultView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      init(context, attrs, defStyleAttr);
   }

   private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LogResultView, defStyleAttr, 0);
      try {
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View view = inflater.inflate(R.layout.log_result_view, this, true);
         String title = ta.getString(R.styleable.LogResultView_logTitle);
         String logResult = ta.getString(R.styleable.LogResultView_logResult);

         titleView = view.findViewById(R.id.title);
         logResultView = view.findViewById(R.id.log_result);
         copiedView = view.findViewById(R.id.copied);
         copyLogIdView = view.findViewById(R.id.copy_log_id);

         setTextSafe(titleView, title);
         setTextSafe(logResultView, logResult);

      } finally {
         ta.recycle();
      }
   }

   private void setTextSafe(final @Nullable TextView textView, final String content) {
      if (!TextUtils.isEmpty(content) && textView != null) {
         textView.setText(content);
      }
   }

   public void setTitle(final String content) {
      setTextSafe(titleView, content);
   }

   public void setLogResult(final String content) {
      setTextSafe(logResultView, content);
      copyLogIdView.setVisibility(View.GONE);
      try {
         JSONObject jsonObject = new JSONObject(content);
         attachCopied(jsonObject.getString("logID"));
      } catch (JSONException e) {
         Log.e(LogResultView.class.getSimpleName(), e.getMessage(), e);
      }
      if (copiedView != null) {
         copiedView.setOnClickListener(v -> {
            if (logResultView != null) {
               ShareUtils.copyToClipboard(getContext(), logResultView.getText().toString());
            }
         });
      }
      if (logResultView != null) {
         logResultView.setOnLongClickListener(view -> {
            ShareUtils.copyToClipboard(getContext(), logResultView.getText().toString());
            return true;
         });
      }
   }

   public void attachCopied(final String lodId) {
      if (copyLogIdView != null) {
         copyLogIdView.setVisibility(TextUtils.isEmpty(lodId) ? View.GONE : View.VISIBLE);
         copyLogIdView.setOnClickListener(v -> ShareUtils.copyToClipboard(getContext(), lodId));
      }
   }
}
