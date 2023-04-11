package com.vgaw.scaffold.util.dialog;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vgaw.scaffold.R;

public class DialogUtil {
    public static Fragment getDialogFragemnt(FragmentManager fragmentManager, Class cls) {
        return fragmentManager.findFragmentByTag(buildTag(cls));
    }

    public static void showDialog(@IdRes int containerId, Fragment dialogFragment, FragmentManager fragmentManager, String name) {
        showDialog(containerId, dialogFragment, fragmentManager, name, false);
    }

    public static void showDialog(@IdRes int containerId, Fragment dialogFragment, FragmentManager fragmentManager) {
        String name = buildTag(dialogFragment);
        showDialog(containerId, dialogFragment, fragmentManager, name);
    }

    public static void showDialog(Fragment dialogFragment, FragmentManager fragmentManager, String name) {
        showDialog(android.R.id.content, dialogFragment, fragmentManager, name);
    }

    public static void showDialog(Fragment dialogFragment, FragmentManager fragmentManager) {
        String name = buildTag(dialogFragment);
        showDialog(android.R.id.content, dialogFragment, fragmentManager, name, true);
    }

    public static void showDialog(Fragment dialogFragment, FragmentManager fragmentManager, boolean animate) {
        String name = buildTag(dialogFragment);
        showDialog(android.R.id.content, dialogFragment, fragmentManager, name, animate);
    }

    private static void showDialog(@IdRes int containerId, Fragment dialogFragment, FragmentManager fragmentManager, String name, boolean animate) {
        if (!fragmentManager.isDestroyed()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (animate) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_up, 0, 0, R.anim.slide_down);
            }
            fragmentTransaction
                    .addToBackStack(name)
                    .replace(containerId, dialogFragment, name)
                    .commitAllowingStateLoss();
        }
    }

    public static void dismissDialog(FragmentManager fragmentManager, String name) {
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment != null && !fragmentManager.isDestroyed()) {
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void dismissDialog(Fragment dialogFragment) {
        FragmentActivity activity = dialogFragment.getActivity();
        if (activity != null && !activity.isDestroyed()) {
            String name = buildTag(dialogFragment);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(name);
            if (fragment != null) {
                fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public static String buildTag(Fragment dialogFragment) {
        return dialogFragment.getClass().getName();
    }

    public static String buildTag(Class cls) {
        return cls.getName();
    }
}
