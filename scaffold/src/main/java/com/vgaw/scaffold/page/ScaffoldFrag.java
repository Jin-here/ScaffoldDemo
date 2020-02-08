package com.vgaw.scaffold.page;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.vgaw.scaffold.R;

public class ScaffoldFrag extends Fragment {
    protected FragmentActivity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    protected Fragment getSelf() {
        return this;
    }

    /**
     * 当本fragment显示时，状态栏的颜色
     * @return
     */
    public int getStatusBarColor() {
        return R.color.white;
    }
}
