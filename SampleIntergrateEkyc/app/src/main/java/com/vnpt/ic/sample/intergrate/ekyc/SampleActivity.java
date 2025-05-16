package com.vnpt.ic.sample.intergrate.ekyc;

import static com.vnptit.idg.sdk.utils.KeyIntentConstants.ACCESS_TOKEN;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CAMERA_POSITION_FOR_PORTRAIT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHALLENGE_CODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHANGE_BASE_URL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHECK_LIVENESS_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.HASH_FRONT_OCR;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.HASH_IMAGE_COMPARE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_LIVENESS_CARD;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_MASKED_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_COMPARE_GENERAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_COMPARE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_GOT_IT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_SHOW_TUTORIAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_VALIDATE_POSTCODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.LANGUAGE_SDK;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_ID;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_KEY;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VALIDATE_DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VERSION_SDK;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.HASH_IMAGE_FRONT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_BACK_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.OCR_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.QR_CODE_RESULT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.vnpt.ic.sample.intergrate.ekyc.databinding.ActivitySampleBinding;
import com.vnptit.idg.sdk.activity.VnptFrontActivity;
import com.vnptit.idg.sdk.activity.VnptIdentityActivity;
import com.vnptit.idg.sdk.activity.VnptOcrActivity;
import com.vnptit.idg.sdk.activity.VnptPortraitActivity;
import com.vnptit.idg.sdk.activity.VnptQRCodeActivity;
import com.vnptit.idg.sdk.activity.VnptRearActivity;
import com.vnptit.idg.sdk.utils.KeyIntentConstants;
import com.vnptit.idg.sdk.utils.SDKEnum;
import com.vnptit.nfc.activity.VnptScanNFCActivity;
import com.vnptit.nfc.utils.KeyIntentConstantsNFC;
import com.vnptit.nfc.utils.KeyResultConstantsNFC;
import com.vnptit.nfc.utils.SDKEnumNFC;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

   private ActivitySampleBinding binding;
   private ActivityResultLauncher<Intent> eKYCResultLauncher;
   private ActivityResultLauncher<Intent> resultLauncher;

   public static final String EXTRA_LOG_RESULT = "EXTRA_LOG_RESULT";


   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      binding = ActivitySampleBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      eKYCResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
         if (result.getResultCode() == Activity.RESULT_OK) {
            final Intent data = result.getData();
            if (data != null) {
               if (data.getStringExtra(HASH_IMAGE_FRONT) != null) {
                  AppCode.HASH_FRONT = data.getStringExtra(HASH_IMAGE_FRONT);
               }
               final Intent intent = new Intent(this, LogActivity.class);

               /*
                * vi: Dữ liệu bóc tách thông tin OCR
                * en: OCR info extraction
                * OCR Result
                */
               final String strOCRResult = data.getStringExtra(OCR_RESULT);
               intent.putExtra(OCR_RESULT, strOCRResult);

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
               final String strLivenessCardRearResult = data.getStringExtra(LIVENESS_CARD_BACK_RESULT);
               intent.putExtra(LIVENESS_CARD_BACK_RESULT, strLivenessCardRearResult);

               /*
                * vi: Dữ liệu bóc tách thông tin compare face
                * en: Compare face info extraction
                * COMPARE Result
                */
               final String strCompareResult = data.getStringExtra(COMPARE_FACE_RESULT);
               intent.putExtra(COMPARE_FACE_RESULT, strCompareResult);

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

               final String qrCodeResult = data.getStringExtra(QR_CODE_RESULT);
               intent.putExtra(QR_CODE_RESULT, qrCodeResult);

               startActivity(intent);
            }
         }
      });

      resultLauncher = registerForActivityResult(
         new ActivityResultContracts.StartActivityForResult(),
         result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
               Intent data = result.getData();

               String avatarPath = data.getStringExtra(KeyResultConstantsNFC.PATH_IMAGE_AVATAR);
               String clientSession = data.getStringExtra(KeyResultConstantsNFC.CLIENT_SESSION_RESULT);
               String logNFC = data.getStringExtra(KeyResultConstantsNFC.DATA_NFC_RESULT);
               String hashAvatar = data.getStringExtra(KeyResultConstantsNFC.HASH_IMAGE_AVATAR);
               String postCodeOriginalLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT);
               String postCodeRecentLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT);
               String timeScanNfc = data.getStringExtra(KeyResultConstantsNFC.TIME_SCAN_NFC);
               String checkAuthChipResult = data.getStringExtra(KeyResultConstantsNFC.STATUS_CHIP_AUTHENTICATION);
               String qrCodeResult = data.getStringExtra(KeyResultConstantsNFC.QR_CODE_RESULT);

               ArrayList<LogResult> results = new ArrayList<>();
               addNotNullOrEmpty(results, "Avatar NFC", avatarPath);
               addNotNullOrEmpty(results, "Client session", clientSession);
               addNotNullOrEmpty(results, "Log NFC", logNFC);
               addNotNullOrEmpty(results, "Hash avatar", hashAvatar);
               addNotNullOrEmpty(results, "Postcode original location", postCodeOriginalLocation);
               addNotNullOrEmpty(results, "Postcode recent location", postCodeRecentLocation);
               addNotNullOrEmpty(results, "Time scan NFC", timeScanNfc);
               addNotNullOrEmpty(results, "Check auth chip", checkAuthChipResult);
               addNotNullOrEmpty(results, "Qrcode", qrCodeResult);

               if (!results.isEmpty()) {
                  Intent intent = new Intent(this, LogResultActivity.class);
                  intent.putExtra(EXTRA_LOG_RESULT, results);
                  startActivity(intent);
               }
            }
         }
      );

      initListeners();
   }

   @Override
   public void onClick(View view) {
      if (view == binding.ekycFace) {
         startEkycFace();
      } else if (view == binding.ekycOcr) {
         startEkycOcr();
      } else if (view == binding.nfcQr) {
         startEkycQR();
      } else if (view == binding.ekycOcrFront) {
         startEkycOcrFront();
      } else if (view == binding.ekycOcrBack) {
         startEkycOcrBack();
      } else if (view == binding.ekycNfcFull) {
         startEkycFull();
      }
   }


   private void initListeners() {
      binding.ekycNfcFull.setOnClickListener(this);
      binding.ekycOcr.setOnClickListener(this);
      binding.ekycFace.setOnClickListener(this);
      binding.ekycOcrFront.setOnClickListener(this);
      binding.ekycOcrBack.setOnClickListener(this);
      binding.nfcQr.setOnClickListener(this);
   }

   private void deinitListeners() {
      binding.ekycNfcFull.setOnClickListener(null);
      binding.ekycOcr.setOnClickListener(null);
      binding.ekycFace.setOnClickListener(null);
      binding.ekycOcrFront.setOnClickListener(null);
      binding.ekycOcrBack.setOnClickListener(null);
      binding.nfcQr.setOnClickListener(null);
   }


   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh chân dung xa gần
   // Bước 2 - hiển thị kết quả

   /* TODO: Add dialog to insert hash image */
   private void startEkycFace() {
      DialogUtils.showInputDialog(this, hashValue -> {
         final Intent intent = getBaseIntent(VnptPortraitActivity.class);

         // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
         // - Normal: chụp ảnh chân dung 1 hướng
         // - ADVANCED: chụp ảnh chân dung xa gần
         intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

         // Bật/[Tắt] chức năng So sánh ảnh trong thẻ và ảnh chân dung
         intent.putExtra(IS_ENABLE_COMPARE, true);

         /**
          * Giá trị này xác định việc có thực hiện so sánh khuôn mặt chân dung trong giấy tờ dạng đầy đủ (ví dụ ảnh thẻ) với ảnh chân dung sau khi chụp ảnh từ SDK.
          * Mặc định là false
          */
         intent.putExtra(IS_COMPARE_GENERAL, true);

         /**
          * Giá trị này là mã ảnh chứa ảnh chân dung (có thể là ảnh mặt trước giấy tờ), được truyền vào để so sánh với ảnh chân dung sau khi chụp ảnh từ SDK.
          * Mặc định giá trị rỗng ("")
          */
         intent.putExtra(HASH_IMAGE_COMPARE, hashValue);

         // Bật/Tắt chức năng kiểm tra che mặt
         intent.putExtra(IS_CHECK_MASKED_FACE, true);

         // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
         // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
         // - IBeta: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
         // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
         intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

         eKYCResultLauncher.launch(intent);
      });
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
      // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

      // Bật/Tắt hướng dẫn
      intent.putExtra(IS_SHOW_TUTORIAL, true);

      //Bật/Tắt xác thực thông tin postal code
      intent.putExtra(IS_VALIDATE_POSTCODE, true);

      eKYCResultLauncher.launch(intent);
   }

   private void startEkycFull() {
      final Intent intent = getBaseIntent(VnptIdentityActivity.class);

      // Version SDK
      intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      // Bật/Tắt Hiển thị màn hình hướng dẫn
      intent.putExtra(IS_SHOW_TUTORIAL, true);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng so sánh
      intent.putExtra(IS_ENABLE_COMPARE, true);

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(IS_CHECK_MASKED_FACE, true);

      // Bật/Tắt chức năng kiểm tra liveness khuôn mặt
      intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      // Bật/Tắt chức năng xác thực thông tin postal code
      intent.putExtra(IS_VALIDATE_POSTCODE, true);

      // Bật/Tắt chức năng kiểm tra liveness giấy tờ
      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

      eKYCResultLauncher.launch(intent);
   }

   private void startEkycQR() {
      final Intent intent = getBaseIntent(VnptQRCodeActivity.class);
      eKYCResultLauncher.launch(intent);
   }

   private void startEkycOcrFront() {
      final Intent intent = getBaseIntent(VnptFrontActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      intent.putExtra(IS_VALIDATE_POSTCODE, true);

      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

      intent.putExtra(IS_SHOW_TUTORIAL, true);

      eKYCResultLauncher.launch(intent);
   }

   private void startEkycOcrBack() {
      DialogUtils.showInputDialog(this, hashValue -> {
         final Intent intent = getBaseIntent(VnptRearActivity.class);
         // Truyền mã hash mặt trước của ảnh giấy tờ
         intent.putExtra(HASH_FRONT_OCR, hashValue);

         // Giá trị này xác định kiểu giấy tờ để sử dụng:
         // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
         // - IDCardChipBased: Căn cước công dân gắn Chip
         // - Passport: Hộ chiếu
         // - DriverLicense: Bằng lái xe
         // - MilitaryIdCard: Chứng minh thư quân đội
         intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

         // Bật/Tắt chức năng kiểm tra
         intent.putExtra(IS_VALIDATE_POSTCODE, true);

         // Bật/Tắt chức năng kiểm tra liveness giấy tờ
         intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

         // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
         // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
         // - Basic: Kiểm tra sau khi chụp ảnh
         // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
         // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
         intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

         // Bật/Tắt hướng dẫn
         intent.putExtra(IS_SHOW_TUTORIAL, true);

         eKYCResultLauncher.launch(intent);
      });
   }


   // Base intent cho ekyc
   private Intent getBaseIntent(final Class<?> clazz) {
      final Intent intent = new Intent(this, clazz);

      // Nhập thông tin bộ mã truy cập. Lấy tại mục Quản lý Token https://ekyc.vnpt.vn/admin-dashboard/console/project-manager
      intent.putExtra(ACCESS_TOKEN, AppCode.ACCESS_TOKEN);
      intent.putExtra(TOKEN_ID, AppCode.TOKEN_ID);
      intent.putExtra(TOKEN_KEY, AppCode.TOKEN_KEY);

      // Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
      intent.putExtra(CHALLENGE_CODE, "INNOVATIONCENTER");

      // Ngôn ngữ sử dụng trong SDK
      // - VIETNAMESE: Tiếng Việt
      // - ENGLISH: Tiếng Anh
      intent.putExtra(LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.getValue());

      // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
      intent.putExtra(IS_ENABLE_GOT_IT, true);

      // Sử dụng máy ảnh mặt trước
      // - FRONT: Camera trước
      // - BACK: Camera trước
      intent.putExtra(CAMERA_POSITION_FOR_PORTRAIT, SDKEnum.CameraTypeEnum.FRONT.getValue());

      // Chức năng thay đổi tên miền
      intent.putExtra(CHANGE_BASE_URL, "");
      wrapThemeIntentEkyc(intent);

      return intent;
   }

   public static void addNotNullOrEmpty(ArrayList<LogResult> list, String key, @Nullable String value) {
      if (!TextUtils.isEmpty(value)) {
         list.add(new LogResult(key, value));
      }
   }




   private static void wrapThemeIntentEkyc(Intent intent) {
      /**
       * Thuộc tính: MODE_BUTTON_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Vị trí nút đóng SDK trên thanh tiêu đề. Mặc định LeftButton.
       *       SDKEnumNFC.ModeButtonHeaderBar.LeftButton.getValue() - nút đóng bên trái
       *       SDKEnumNFC.ModeButtonHeaderBar.RightButton.getValue() - nút đóng bên phải.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề chứa nút Đóng SDK
       */
      intent.putExtra(KeyIntentConstants.MODE_BUTTON_HEADER_BAR, AppCode.modeButtonHeaderBar);

      /**
       * Thuộc tính: CONTENT_COLOR_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Màu nội dung thanh tiêu đề: bao gồm màu chữ và màu nút đóng. Mặc định 0xFF142730.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề chứa nút Đóng SDK, Tiêu đề màn hình.
       */
      intent.putExtra(KeyIntentConstants.CONTENT_COLOR_HEADER_BAR, AppCode.contentColorHeaderBar);

      /**
       * Thuộc tính: BACKGROUND_COLOR_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền thanh tiêu đề. Mặc định 0x00000000.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_HEADER_BAR, AppCode.backgroundColorHeaderBar);

      /**
       * Thuộc tính: TEXT_COLOR_CONTENT_MAIN
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ nội dung chính. Mặc định 0xFF142730.
       *       Áp dụng cho toàn bộ các màn hình.
       */
      intent.putExtra(KeyIntentConstants.TEXT_COLOR_CONTENT_MAIN, AppCode.textColorContentMain);

      /**
       * Thuộc tính: TITLE_COLOR_MAIN
       * Kiểu dữ liệu: int
       * Mô tả: Màu tiêu đề chính. Mặc định 0xFF00A96F.
       *       Áp dụng cho Tiêu đề: CHỤP MẶT TRƯỚC, ẢNH MẶT TRƯỚC, chấm tròn nhỏ ở màn hình Hướng dẫn, màn hình xem trước.
       */
      intent.putExtra(KeyIntentConstants.TITLE_COLOR_MAIN, AppCode.titleColorMain);

      /**
       * Thuộc tính: BACKGROUND_COLOR_MAIN_SCREEN
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền chính. Mặc định 0xFFFFFFFF.
       *       Áp dụng cho màn Hướng dẫn, màn xem trước, màn hình xử lý dữ liệu.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_MAIN_SCREEN, AppCode.backgroundColorMainScreen);

      /**
       * Thuộc tính: BACKGROUND_COLOR_LINE
       * Kiểu dữ liệu: int
       * Mô tả: Đường kẻ ngang ngăn cách các nội dung. Mặc định 0xFFE8E8E8.
       *       Áp dụng trên các màn hình Hướng dẫn, các màn hình Cảnh báo.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_LINE, AppCode.backgroundColorLine);

      /**
       * Thuộc tính: BACKGROUND_COLOR_CAPTURE_DOCUMENT_SCREEN
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền chụp ảnh giấy tờ, quét mã QR. Mặc định 0xBFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_CAPTURE_DOCUMENT_SCREEN, AppCode.backgroundColorCaptureDocumentScreen);

      /**
       * Thuộc tính: BACKGROUND_COLOR_CAPTURE_FACE_SCREEN
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền chụp ảnh chân dung. Mặc định 0xBFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_CAPTURE_FACE_SCREEN, AppCode.backgroundColorCaptureFaceScreen);

      /**
       * Thuộc tính: BACKGROUND_COLOR_ACTIVE_BUTTON
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền nút bấm ở trạng thái hoạt động. Mặc định 0xFF00A96F.
       *       Áp dụng cho tất cả các màn hình và màn hình cảnh báo.
       *       Lưu ý: Màu tiêu đề và viền của nút bấm phụ là màu nền của nút chính. Màu nền nút bấm phụ là màu trong suốt.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_ACTIVE_BUTTON, AppCode.backgroundColorActiveButton);

      /**
       * Thuộc tính: TITLE_COLOR_ACTIVE_BUTTON
       * Kiểu dữ liệu: int
       * Mô tả: Màu tiêu đề nút bấm ở trạng thái hoạt động. Mặc định 0xFFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.TITLE_COLOR_ACTIVE_BUTTON, AppCode.titleColorActiveButton);

      /**
       * Thuộc tính: BACKGROUND_COLOR_DEACTIVE_BUTTON
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền nút bấm ở trạng thái không hoạt động. Mặc định 0xFFB8C1C6.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_DEACTIVE_BUTTON, AppCode.backgroundColorDeactiveButton);

      /**
       * Thuộc tính: EFFECT_COLOR_NOTICE_FACE
       * Kiểu dữ liệu: int
       * Mô tả: Màu hiệu ứng cảnh báo ở màn chụp khuôn mặt Oval. Mặc định 0xFF00A96F.
       */
      intent.putExtra(KeyIntentConstants.EFFECT_COLOR_NOTICE_FACE, AppCode.effectColorNoticeFace);

      /**
       * Thuộc tính: TEXT_COLOR_NOTICE_FACE
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ nội dung trong ô cảnh báo ở màn chụp khuôn mặt Oval. Mặc định 0xFFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.TEXT_COLOR_NOTICE_FACE, AppCode.textColorNoticeFace);

      /**
       * Thuộc tính: EFFECT_COLOR_NOTICE_VALID_DOCUMENT
       * Kiểu dữ liệu: int
       * Mô tả: Màu hiệu ứng cảnh báo giấy tờ hợp lệ. Mặc định 0xFF00A96F.
       */
      intent.putExtra(KeyIntentConstants.EFFECT_COLOR_NOTICE_VALID_DOCUMENT, AppCode.effectColorNoticeValidDocument);

      /**
       * Thuộc tính: TEXT_COLOR_NOTICE_VALID_DOCUMENT
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ nội dung trong ô cảnh báo giấy tờ hợp lệ. Mặc định 0xFFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.TEXT_COLOR_NOTICE_VALID_DOCUMENT, AppCode.textColorNoticeValidDocument);

      /**
       * Thuộc tính: EFFECT_COLOR_NOTICE_INVALID_DOCUMENT
       * Kiểu dữ liệu: int
       * Mô tả: Màu hiệu ứng cảnh báo giấy tờ không hợp lệ. Mặc định 0xFFCA2A2A.
       */
      intent.putExtra(KeyIntentConstants.EFFECT_COLOR_NOTICE_INVALID_DOCUMENT, AppCode.effectColorNoticeInvalidDocument);

      /**
       * Thuộc tính: TEXT_COLOR_NOTICE_INVALID_DOCUMENT
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ nội dung trong ô cảnh báo giấy tờ không hợp lệ. Mặc định 0xFFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.TEXT_COLOR_NOTICE_INVALID_DOCUMENT, AppCode.textColorNoticeInvalidDocument);

      /**
       * Thuộc tính: TINT_COLOR_BUTTON_CAPTURE
       * Kiểu dữ liệu: int
       * Mô tả: Màu nút chụp ảnh giấy tờ, chụp ảnh chân dung cơ bản. Mặc định 0xFF142730.
       */
      intent.putExtra(KeyIntentConstants.TINT_COLOR_BUTTON_CAPTURE, AppCode.tintColorButtonCapture);

      /**
       * Thuộc tính: BACKGROUND_COLOR_BORDER_CAPTURE_FACE
       * Kiểu dữ liệu: int
       * Mô tả: Màu đường viền khung chụp khuôn mặt Oval, chụp khuôn mặt cơ bản. Mặc định 0xFF00A96F.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_BORDER_CAPTURE_FACE, AppCode.backgroundColorBorderCaptureFace);
      /**
       * Thuộc tính: BACKGROUND_COLOR_POPUP
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền của các màn hình dạng cảnh báo. Mặc định 0xFFFFFFFF.
       */
      intent.putExtra(KeyIntentConstants.BACKGROUND_COLOR_POPUP, AppCode.backgroundColorPopup);

      /**
       * Thuộc tính: TEXT_COLOR_CONTENT_POPUP
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ của các màn hình dạng cảnh báo. Mặc định 0xFF142730.
       */
      intent.putExtra(KeyIntentConstants.TEXT_COLOR_CONTENT_POPUP, AppCode.textColorContentPopup);
   }

   private static void wrapThemeIntentNfc(Intent intent) {
      /**
       * Thuộc tính: MODE_BUTTON_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Vị trí nút đóng SDK trên thanh tiêu đề. Mặc định LeftButton.
       *       SDKEnumNFC.ModeButtonHeaderBar.LeftButton.getValue() - nút đóng bên trái
       *       SDKEnumNFC.ModeButtonHeaderBar.RightButton.getValue() - nút đóng bên phải.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề chứa nút Đóng SDK
       */
      intent.putExtra(KeyIntentConstantsNFC.MODE_BUTTON_HEADER_BAR, AppCode.modeButtonHeaderBar);

      /**
       * Thuộc tính: CONTENT_COLOR_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Màu nội dung thanh tiêu đề: bao gồm màu chữ và màu nút đóng. Mặc định 0xFF142730.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề chứa nút Đóng SDK, Tiêu đề màn hình
       */
      intent.putExtra(KeyIntentConstantsNFC.CONTENT_COLOR_HEADER_BAR, AppCode.contentColorHeaderBar);

      /**
       * Thuộc tính: BACKGROUND_COLOR_HEADER_BAR
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền thanh tiêu đề. Mặc định 0x00000000.
       *       Áp dụng cho toàn bộ các màn hình có thanh tiêu đề
       */
      intent.putExtra(KeyIntentConstantsNFC.BACKGROUND_COLOR_HEADER_BAR, AppCode.backgroundColorHeaderBar);

      /**
       * Thuộc tính: TEXT_COLOR_CONTENT_MAIN
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ nội dung chính. Mặc định 0xFF142730.
       *       Áp dụng cho toàn bộ các màn hình
       */
      intent.putExtra(KeyIntentConstantsNFC.TEXT_COLOR_CONTENT_MAIN, AppCode.textColorContentMain);

      /**
       * Thuộc tính: BACKGROUND_COLOR_MAIN_SCREEN
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền chính. Mặc định 0xFFFFFFFF.
       *       Áp dụng cho màn Hướng dẫn (Help), màn xem trước (Preview), màn hình xử lý dữ liệu
       */
      intent.putExtra(KeyIntentConstantsNFC.BACKGROUND_COLOR_MAIN_SCREEN, AppCode.backgroundColorMainScreen);

      /**
       * Thuộc tính: BACKGROUND_COLOR_LINE
       * Kiểu dữ liệu: int
       * Mô tả: Đường kẻ ngang ngăn cách các nội dung. Mặc định 0xFFE8E8E8.
       *       Áp dụng trên các màn hình Hướng dẫn, các màn hình Cảnh báo
       */
      intent.putExtra(KeyIntentConstantsNFC.BACKGROUND_COLOR_LINE, AppCode.backgroundColorLine);

      /**
       * Thuộc tính: BACKGROUND_COLOR_POPUP
       * Kiểu dữ liệu: int
       * Mô tả: Màu nền của các màn hình dạng cảnh báo. Mặc định 0xFFFFFFFF
       */
      intent.putExtra(KeyIntentConstantsNFC.BACKGROUND_COLOR_POPUP, AppCode.backgroundColorPopup);

      /**
       * Thuộc tính: TEXT_COLOR_CONTENT_POPUP
       * Kiểu dữ liệu: int
       * Mô tả: Màu chữ của các màn hình dạng cảnh báo. Mặc định 0xFF142730
       */
      intent.putExtra(KeyIntentConstantsNFC.TEXT_COLOR_CONTENT_POPUP, AppCode.textColorContentPopup);

   }


   @Override
   protected void onDestroy() {
      deinitListeners();
      super.onDestroy();
   }
}