package com.vgaw.scaffold.util;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class DialogUtil {
    public static void showDialog(@IdRes int containerId, Fragment dialogFragment, FragmentManager fragmentManager, String name) {
        fragmentManager.beginTransaction()
                .addToBackStack(name)
                .replace(containerId, dialogFragment, name)
                .commit();
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
        showDialog(dialogFragment, fragmentManager, name);
    }

    public static void dismissDialog(FragmentManager fragmentManager, String name) {
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void dismissDialog(Fragment dialogFragment) {
        FragmentActivity activity = dialogFragment.getActivity();
        if (activity != null) {
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
