package com.vgaw.scaffolddemo.page;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vgaw.scaffold.page.BaseAc;
import com.vgaw.scaffold.page.BaseFrag;
import com.vgaw.scaffold.util.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout;
import com.vgaw.scaffold.view.bottomnavigation.OnItemCheckedListener;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.CompomentFrag;
import com.vgaw.scaffolddemo.page.demo.example.ExampleFrag;
import com.vgaw.scaffolddemo.page.demo.internalpage.InternalPageFrag;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import timber.log.Timber;

public class MainAc extends BaseAc {
    private static final int REQ_CODE_PERMISSION = 1;

    private BottomNavigationLayout mMainBottomNavLayout;

    private BaseFrag[] mFragmentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ac);
        mMainBottomNavLayout = findViewById(R.id.main_bottom_nav_layout);

        initData();
        initView();
        requestPermission();
    }

    private void initData() {
        mFragmentArray = new BaseFrag[]{new CompomentFrag(), new InternalPageFrag(), new ExampleFrag()};
    }

    private void initView() {
        mMainBottomNavLayout.setOnItemCheckedListener(new OnItemCheckedListener() {
            @Override
            public void onItemChecked(int index) {
                showFragment(index);
            }
        });
        mMainBottomNavLayout.check(0);
    }

    private void showFragment(int index) {
        String tag = DialogUtil.buildTag(mFragmentArray[index]);

        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFrag fragment = (BaseFrag) fragmentManager.findFragmentByTag(tag);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // hide other
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment item : fragmentList) {
                if (item.isAdded()) {
                    fragmentTransaction.hide(item);
                }
            }
        }
        // showDialog index fragment
        if (fragment == null) {
            fragment = mFragmentArray[index];
            fragmentTransaction.add(R.id.main_container, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        StatusBarUtil.setColor(this, getResources().getColor(fragment.getStatusBarColor()));
    }

    private void requestPermission() {
        AndPermission.with(this).runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE, Permission.CAMERA)
                .rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
                        Timber.d("showRationale: %s", data);
                        //String permissionText = TextUtils.join(",\n", permissionNames);
                        new AlertDialog.Builder(getSelf())
                                .setMessage(Permission.transformText(context, data).toString() + "需要授权")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 调用后会进入onDenied回调
                                        executor.execute();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 调用后会进入onDenied回调
                                        //executor.cancel();
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Timber.d("onDenied: %s", data);
                        // 当用户点击不再提示时，此时无法弹出系统授权框，只能让用户进入系统权限设置界面打开权限
                        if (AndPermission.hasAlwaysDeniedPermission(getSelf(), data)) {
                            new AlertDialog.Builder(getSelf())
                                    .setMessage("权限不够，是否设置")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 进入系统权限设置界面
                                            AndPermission.with(MainAc.this)
                                                    .runtime()
                                                    .setting()
                                                    .start(REQ_CODE_PERMISSION);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            requestPermission();
                        }
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // 所有请求的权限被授权时回调
                        Timber.d("onGranted: %s", data);
                    }
                })
                .start();
    }
}
