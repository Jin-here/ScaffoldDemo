package com.vgaw.scaffold.page.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.page.ScaffoldDialog;
import com.vgaw.scaffold.page.ReqCodeConstant;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.phone.SystemPageUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionAc extends ScaffoldAc {
    private static Map<String, String> sPermissionDesMap = new HashMap<>();
    private Map<String, Boolean> mPermissionDeniedMap;

    private String[] mPermissionArray;
    private String mDes;
    private boolean mCheck;

    static {
        sPermissionDesMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储位置");
        sPermissionDesMap.put(Manifest.permission.RECORD_AUDIO, "麦克风");
        sPermissionDesMap.put(Manifest.permission.READ_PHONE_STATE, "设备信息");
        sPermissionDesMap.put(Manifest.permission.CAMERA, "相机");
    }

    public static boolean hasPermission(Activity activity, String[] permissionArray) {
        for (String item : permissionArray) {
            if (ActivityCompat.checkSelfPermission(activity, item) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void startActivityForResult(Activity activity, int reqCode, String[] perssionArray) {
        startActivityForResult(activity, reqCode, perssionArray, null);
    }

    public static void startActivityForResult(Activity activity, int reqCode, String[] perssionArray, String des) {
        Intent intent = new Intent(activity, PermissionAc.class);
        intent.putExtra("permission_array", perssionArray);
        intent.putExtra("des", des);
        activity.startActivityForResult(intent, reqCode);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_ac);
        StatusBarUtil.setColor(this, Color.TRANSPARENT);

        Intent intent = getIntent();
        mPermissionArray = intent.getStringArrayExtra("permission_array");
        mDes = intent.getStringExtra("des");
        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCheck) {
            mCheck = false;
            if (requestPermission()) {
                onSuc();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCheck = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestPermission()) {
            onSuc();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        onFail();
    }

    private void onSuc() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onFail() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private boolean requestPermission() {
        StringBuilder sb = new StringBuilder();
        List<String> permissionList = new ArrayList<>();
        boolean hasAlwaysDenied = false;
        for (String item : mPermissionArray) {
            if (proPermission(getSelf(), item, permissionList, sb)) {
                hasAlwaysDenied = true;
            }
        }
        if (permissionList.size() > 0) {
            String des = (mDes == null ? String.format("为了正常使用%s，请授予以下权限", getString(R.string.app_name)) : mDes);
            ScaffoldDialog baseDialog = ScaffoldDialog.newInstance(String.format("%s：\n%s", des,
                    sb.substring(0, sb.length() - 1)), "取消", hasAlwaysDenied ? "前往设置" : "开始授权");
            baseDialog.setOnLeftBtnClickListener(v -> {
                DialogUtil.dismissDialog(getSupportFragmentManager(), "permission_dialog");

                onFail();
            });
            boolean finalHasAlwaysDenied = hasAlwaysDenied;
            baseDialog.setOnRightBtnClickListener(v -> {
                DialogUtil.dismissDialog(getSupportFragmentManager(), "permission_dialog");

                if (finalHasAlwaysDenied) {
                    SystemPageUtil.jump2PermissoinPage(getSelf());
                } else {
                    String[] tempArray = new String[permissionList.size()];
                    permissionList.toArray(tempArray);
                    ActivityCompat.requestPermissions(getSelf(),
                            tempArray, ReqCodeConstant.PERMISSION);
                }
            });
            DialogUtil.showDialog(baseDialog, getSupportFragmentManager(), "permission_dialog");
            return false;
        }
        return true;
    }

    /**
     * 对于拒绝的权限，是否被永久拒绝（不再提示）
     * @param activity
     * @param permission
     * @param permissionList
     * @param sb
     * @return
     */
    private boolean proPermission(Activity activity, String permission, List<String> permissionList, StringBuilder sb) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            sb.append(sPermissionDesMap.get(permission)).append("，");
            permissionList.add(permission);

            if (mPermissionDeniedMap == null) {
                mPermissionDeniedMap = new HashMap<>();
            }
            Boolean deniedBefore = mPermissionDeniedMap.get(permission);
            boolean deniedNow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (deniedBefore == null) {
                mPermissionDeniedMap.put(permission, deniedNow);
            } else {
                if (!deniedBefore && !deniedNow) {
                    return true;
                }
            }
        }
        return false;
    }
}
