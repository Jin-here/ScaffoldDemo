package com.vgaw.scaffold.page.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.ScaffoldSetting;
import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.util.FileUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.AppToast;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChooseImgAc extends ScaffoldAc {
    private static final String IMG_SUFFIX = ".png";
    private static final int REQUEST_CODE_CHOOSE_IMG_FROM_FILE = 70;
    private static final int REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA = 71;
    private static final int REQUEST_CODE_IMG_CLIP = 72;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 81;

    private Uri mCameraUri = null;
    private File mCameraFile = null;

    private CropConfig mCropConfig;

    private Uri mCropFileUri;
    private Map<Uri, Integer> mPermissionMap;

    /**
     * intent返回值：
     * data:文件路径，字符串类型
     *
     * 默认对图片进行裁剪
     *
     * @param activity
     * @param requestCode
     */
    public static void startActivityForResult(Activity activity, int requestCode) {
        startActivityForResult(activity, requestCode, null);
    }

    public static void startActivityForResult(Fragment fragment, int requestCode) {
        startActivityForResult(fragment, requestCode, null);
    }

    public static void startActivityForResult(Activity activity, int requestCode, @Nullable CropConfig cropConfig) {
        Intent intent = new Intent(activity, ChooseImgAc.class);
        if (CropConfig.valid(cropConfig)) {
            intent.putExtra("crop_config", cropConfig);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    public static void startActivityForResult(Fragment fragment, int requestCode, @Nullable CropConfig cropConfig) {
        Intent intent = new Intent(fragment.getContext(), ChooseImgAc.class);
        if (CropConfig.valid(cropConfig)) {
            intent.putExtra("crop_config", cropConfig);
        }
        fragment.startActivityForResult(intent, requestCode);
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isDestroyed()) {
            activity.overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_img_ac);
        StatusBarUtil.setColor(this, Color.TRANSPARENT);
        View chooseImgBg = findViewById(R.id.choose_img_bg);
        Button chooseImgFromAlbum = findViewById(R.id.choose_img_from_album);
        Button chooseImgFromCamera = findViewById(R.id.choose_img_from_camera);

        Intent intent = getIntent();
        mCropConfig = intent.getParcelableExtra("crop_config");

        chooseImgBg.setOnClickListener(v -> finish());
        chooseImgFromAlbum.setOnClickListener(v -> callFile());
        chooseImgFromCamera.setOnClickListener(v -> callCamera());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_IMG_FROM_FILE:
                    if (mCropConfig != null) {
                        callCrop(data.getData(), false);
                    } else {
                        onGetResult(FileUtil.getPath(getSelf(), data.getData()), false);
                    }
                    break;
                case REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA:
                    if (mCropConfig != null) {
                        callCrop(mCameraUri, true);
                    } else {
                        if (mCameraFile != null) {
                            onGetResult(mCameraFile.getAbsolutePath(), false);
                        }
                    }
                    break;
                case REQUEST_CODE_IMG_CLIP:
                    if (mCropFileUri != null) {
                        onGetResult(mCropFileUri.toString(), true);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            revokePermission();
        }
    }

    private void onGetResult(String data, boolean crop) {
        if (!crop) {
            // 图片大小限制
            File file = new File(data);
            long size = file.length();
            if (size > ScaffoldSetting.MAX_IMG_UPLOAD_SIZE) {
                AppToast.show(R.string.choose_img_size_limit);
                finish();
                return;
            }
        }
        Intent intent = getIntent();
        intent.putExtra("data", data);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;
        for (int item : grantResults) {
            if (item != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        if (granted) {
            if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
                callCamera();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void grantUriPermission(Intent intent, Uri uri, int permission) {
        if (mPermissionMap == null) {
            mPermissionMap = new HashMap<>();
        }
        mPermissionMap.put(uri, permission);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, permission);
        }
    }

    private void revokePermission() {
        if (mPermissionMap != null) {
            Set<Map.Entry<Uri, Integer>> entries = mPermissionMap.entrySet();
            Iterator<Map.Entry<Uri, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<Uri, Integer> entry = iterator.next();
                revokeUriPermission(entry.getKey(), entry.getValue());
            }
        }
    }

    private void callCrop(Uri fileUri, boolean fromCamera) {
        if (fileUri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(fileUri, "image/*");
            intent.putExtra("crop", "true");
            // X方向上的比例
            intent.putExtra("aspectX", 1);
            // Y方向上的比例
            intent.putExtra("aspectY", 1);
            // scale: 是否保留比例
            // circleCrop: 是否是圆形裁剪区域
            if (mCropConfig != null) {
                intent.putExtra("outputX", mCropConfig.getOutputX());
                intent.putExtra("outputY", mCropConfig.getOutputY());
            }

            File cropFile = createAppSpecificImgFile();
            mCropFileUri = FileProvider.getUriForFile(getSelf(),
                    getApplicationContext().getPackageName() + ".scaffold.fileprovider", cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropFileUri);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            intent.putExtra("noFaceDetection", true);

            if (fromCamera) {
                grantUriPermission(intent, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            grantUriPermission(intent, mCropFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_CODE_IMG_CLIP);
        }
    }

    private void callFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");

        startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMG_FROM_FILE);
    }

    private void callCamera() {
        // check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createAppSpecificImgFile();
            mCameraFile = photoFile;
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraUri = FileProvider.getUriForFile(this, getPackageName() + ".scaffold.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
            }
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA);
    }

    private File createAppSpecificImgFile() {
        File dir = getCacheDir();
        if (!dir.exists()){
            dir.mkdirs();
        }
        File image = new File(dir, generateFileName());
        return image;
    }

    public static String generateFileName() {
        return String.format("%d%s", System.currentTimeMillis(), IMG_SUFFIX);
    }
}
