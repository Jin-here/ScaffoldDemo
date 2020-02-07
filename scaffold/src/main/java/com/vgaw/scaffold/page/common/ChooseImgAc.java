package com.vgaw.scaffold.page.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.vgaw.scaffold.page.BaseAc;
import com.vgaw.scaffold.util.FileUtil;
import com.vgaw.scaffold.util.context.FileManager;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.AppToast;

import java.io.File;
import java.util.List;

public class ChooseImgAc extends BaseAc {
    private static final String IMG_SUFFIX = ".jpg";
    private static final int REQUEST_CODE_CHOOSE_IMG_FROM_FILE = 70;
    private static final int REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA = 71;
    private static final int REQUEST_CODE_IMG_CLIP = 72;
    private static final int REQUEST_CODE_FILE_PERMISSION = 80;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 81;

    private Uri mRawUri = null;
    private Uri mCropUri = null;
    private File mCropFile = null;
    private File mCameraFile = null;

    private boolean mCrop;

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
        startActivityForResult(activity, requestCode, true);
    }

    public static void startActivityForResult(Fragment fragment, int requestCode) {
        startActivityForResult(fragment, requestCode, true);
    }

    public static void startActivityForResult(Activity activity, int requestCode, boolean crop) {
        Intent intent = new Intent(activity, ChooseImgAc.class);
        intent.putExtra("crop", crop);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    public static void startActivityForResult(Fragment fragment, int requestCode, boolean crop) {
        Intent intent = new Intent(fragment.getContext(), ChooseImgAc.class);
        intent.putExtra("crop", crop);
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

        mCrop = getIntent().getBooleanExtra("crop", true);

        chooseImgBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chooseImgFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFile();
            }
        });
        chooseImgFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_IMG_FROM_FILE:
                    if (mCrop) {
                        callCrop(data.getData());
                    } else {
                        onGetResult(FileUtil.getPath(getSelf(), data.getData()), false);
                    }
                    break;
                case REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA:
                    if (mCrop) {
                        callCrop(null);
                    } else {
                        if (mCameraFile != null) {
                            onGetResult(mCameraFile.getAbsolutePath(), false);
                        }
                    }
                    break;
                case REQUEST_CODE_IMG_CLIP:
                    onGetResult(mCropFile.getAbsolutePath(), true);
                    break;
            }
        }
    }

    private void onGetResult(String data, boolean crop) {
        if (!crop) {
            // 图片大小限制
            File file = new File(data);
            long size = file.length();
            if (size > 6 * 1024 * 1024) {
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
            if (requestCode == REQUEST_CODE_FILE_PERMISSION) {
                callFile();
            } else if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
                callCamera();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void callCrop(Uri fileUri) {
        if (fileUri != null) {
            mRawUri = fileUri;
        }
        if (mRawUri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(mRawUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 320);
            intent.putExtra("outputY", 320);

            File photoFile = null;
            photoFile = createImageFile();
            mCropFile = photoFile;

            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mCropUri = FileProvider.getUriForFile(this, getPackageName() + ".scaffold.fileprovider",
                            photoFile);
                    if (fileUri == null) {
                        grantUriPermission(this, intent, mRawUri);
                    }
                    grantUriPermission(this, intent, mCropUri);
                } else {
                    mCropUri = Uri.fromFile(photoFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropUri);
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);

                startActivityForResult(intent, REQUEST_CODE_IMG_CLIP);
            }
        }
    }

    private void grantUriPermission(Context context , Intent intent, Uri uri){
        List<ResolveInfo> resolveInfoList=context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo:resolveInfoList){
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private void callFile() {
        // check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FILE_PERMISSION);
            return;
        }

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
            photoFile = createImageFile();
            mCameraFile = photoFile;
            // Continue only if the File was successfully created
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mRawUri = FileProvider.getUriForFile(this, getPackageName() + ".scaffold.fileprovider",
                            photoFile);
                } else {
                    mRawUri = Uri.fromFile(photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mRawUri);
            }
        }

        startActivityForResult(takePictureIntent, REQUEST_CODE_CHOOSE_IMG_FROM_CAMERA);
    }

    private File createImageFile() {
        File dir = FileManager.getTempDir(this);
        File image = new File(dir, generateFileName());
        return image;
    }

    public static String generateFileName() {
        return String.format("%d%s", System.currentTimeMillis(), IMG_SUFFIX);
    }
}
