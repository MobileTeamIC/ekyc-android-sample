package com.vnpt.ic.sample.intergrate.ekyc;

import static com.vnptit.idg.sdk.utils.KeyIntentConstants.ACCESS_TOKEN;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CAMERA_POSITION_FOR_PORTRAIT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHALLENGE_CODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHECK_LIVENESS_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_LIVENESS_CARD;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_MASKED_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_COMPARE_FLOW;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_GOT_IT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_SHOW_TUTORIAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_VALIDATE_POSTCODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.LANGUAGE_SDK;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_ID;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_KEY;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TYPE_VALIDATE_DOCUMENT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VERSION_SDK;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.INFO_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_REAR_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vnpt.ic.sample.intergrate.ekyc.databinding.ActivitySampleBinding;
import com.vnptit.idg.sdk.activity.VnptIdentityActivity;
import com.vnptit.idg.sdk.activity.VnptOcrActivity;
import com.vnptit.idg.sdk.activity.VnptPortraitActivity;
import com.vnptit.idg.sdk.utils.SDKEnum;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

   private ActivitySampleBinding binding;
   private ActivityResultLauncher<Intent> eKYCResultLauncher;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      binding = ActivitySampleBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      eKYCResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
         if (result.getResultCode() == Activity.RESULT_OK) {
            final Intent data = result.getData();
            if (data != null) {
               final Intent intent = new Intent(this, LogActivity.class);

               /*
                * vi: Dữ liệu bóc tách thông tin OCR
                * en: OCR info extraction
                * OCR Result
                */
               final String strOCRResult = data.getStringExtra(INFO_RESULT);
               intent.putExtra(INFO_RESULT, strOCRResult);

               /*
                * vi: Dữ liệu bóc tách thông tin Liveness card mặt trớc
                * en: Liveness card front info extraction
                * LIVENESS CARD FRONT result
                */
               final String strLivenessCardFrontResult = data.getStringExtra(LIVENESS_CARD_FRONT_RESULT);
               intent.putExtra(LIVENESS_CARD_FRONT_RESULT, strLivenessCardFrontResult);

               /*
                * vi: Dữ liệu bóc tách thông tin liveness card mặt sau
                * en: Liveness card rear info extraction
                * LIVENESS CARD REAR Result
                */
               final String strLivenessCardRearResult = data.getStringExtra(LIVENESS_CARD_REAR_RESULT);
               intent.putExtra(LIVENESS_CARD_REAR_RESULT, strLivenessCardRearResult);

               /*
                * vi: Dữ liệu bóc tách thông tin compare face
                * en: Compare face info extraction
                * COMPARE Result
                */
               final String strCompareResult = data.getStringExtra(COMPARE_RESULT);
               intent.putExtra(COMPARE_RESULT, strCompareResult);

               /*
                * vi: Dữ liệu bóc tách thông tin liveness face
                * en: Liveness face info extraction
                * LIVENESS FACE Result
                */
               final String strLivenessFaceResult = data.getStringExtra(LIVENESS_FACE_RESULT);
               intent.putExtra(LIVENESS_FACE_RESULT, strLivenessFaceResult);

               /*
                * vi: Dữ liệu bóc tách thông tin mask face
                * en: Mask face info extraction
                * MASKED FACE Result
                */
               final String strMaskFaceResult = data.getStringExtra(MASKED_FACE_RESULT);
               intent.putExtra(MASKED_FACE_RESULT, strMaskFaceResult);

               startActivity(intent);
            }
         }
      });

      initListeners();
   }

   @Override
   public void onClick(View view) {
      if (view == binding.ekycFull) {
         startEkycFull();
      } else if (view == binding.ekycFace) {
         startEkycFace();
      } else if (view == binding.ekycOcr) {
         startEkycOcr();
      }
   }

   private void initListeners() {
      binding.ekycFull.setOnClickListener(this);
      binding.ekycOcr.setOnClickListener(this);
      binding.ekycFace.setOnClickListener(this);
   }

   private void deinitListeners() {
      binding.ekycFull.setOnClickListener(null);
      binding.ekycOcr.setOnClickListener(null);
      binding.ekycFace.setOnClickListener(null);
   }


   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh chân dung xa gần
   // Bước 2 - hiển thị kết quả
   private void startEkycFace() {
      final Intent intent = getBaseIntent(VnptPortraitActivity.class);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ADVANCED: chụp ảnh chân dung xa gần
      intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      // Bật/[Tắt] chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(IS_COMPARE_FLOW, false);

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - IBeta: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      eKYCResultLauncher.launch(intent);
   }


   // Phương thức thực hiện eKYC luồng "Chụp ảnh giấy tờ"
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - hiển thị kết quả
   private void startEkycOcr() {
      final Intent intent = getBaseIntent(VnptOcrActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      eKYCResultLauncher.launch(intent);
   }



   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - chụp ảnh chân dung xa gần
   // Bước 3 - hiển thị kết quả
   private void startEkycFull() {
      final Intent intent = getBaseIntent(VnptIdentityActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(IS_COMPARE_FLOW, true);

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - iBETA: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      // Giá trị này xác định việc có xác thực số ID với mã tỉnh thành, quận huyện, xã phường tương ứng hay không.
      intent.putExtra(IS_VALIDATE_POSTCODE, true);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ProOval: chụp ảnh chân dung xa gần
      intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      eKYCResultLauncher.launch(intent);
   }

   private Intent getBaseIntent(final Class<?> clazz) {
      final Intent intent = new Intent(this, clazz);

      // Nhập thông tin bộ mã truy cập. Lấy tại mục Quản lý Token https://ekyc.vnpt.vn/admin-dashboard/console/project-manager
      intent.putExtra(ACCESS_TOKEN, "");
      intent.putExtra(TOKEN_ID, "");
      intent.putExtra(TOKEN_KEY, "");

      // Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
      intent.putExtra(CHALLENGE_CODE, "INNOVATIONCENTER");

      // Ngôn ngữ sử dụng trong SDK
      // - VIETNAMESE: Tiếng Việt
      // - ENGLISH: Tiếng Anh
      intent.putExtra(LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.getValue());

      // Bật/Tắt Hiển thị màn hình hướng dẫn
      intent.putExtra(IS_SHOW_TUTORIAL, true);

      // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
      intent.putExtra(IS_ENABLE_GOT_IT, true);

      // Sử dụng máy ảnh mặt trước
      // - FRONT: Camera trước
      // - BACK: Camera trước
      intent.putExtra(CAMERA_POSITION_FOR_PORTRAIT, SDKEnum.CameraTypeEnum.FRONT.getValue());

      return intent;
   }

   @Override
   protected void onDestroy() {
      deinitListeners();
      super.onDestroy();
   }
}