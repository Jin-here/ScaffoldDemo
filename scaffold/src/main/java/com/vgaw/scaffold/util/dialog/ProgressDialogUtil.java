package com.vgaw.scaffold.util.dialog;

import androidx.fragment.app.FragmentManager;

public class ProgressDialogUtil {
    private static String sLstName;

    public static void showDialog(String msg, FragmentManager fragmentManager) {
        dismissDialog(fragmentManager);

        sLstName = String.valueOf(System.currentTimeMillis());
        ProgressDialog progressDialog = ProgressDialog.newInstance(true, msg);
        DialogUtil.showDialog(progressDialog, fragmentManager, sLstName);
    }

    public static void dismissDialog(FragmentManager fragmentManager) {
        if (sLstName != null) {
            DialogUtil.dismissDialog(fragmentManager, sLstName);

            sLstName = null;
        }
    }
}
