package com.vgaw.scaffold.util.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.vgaw.scaffold.R;

/**
 *  操作性的等待弹窗
 * 2. 推荐通过newInstance()方式使用，set方法中除了各种listener可以直接使用外，其他set方法建议只在继承类中使用;
 */
public class ProgressDialog extends DialogFragment {
    private static final boolean DEFAULT_SHOW_DESCRIPTION = true;
    private static final @StringRes int DEFAULT_DESCRIPTION = -1;

    private TextView mBaseDialogDescription;

    private boolean mShowDescription;
    private String mDescription;

    public static ProgressDialog newInstance(boolean showDescription, String description) {
        Bundle args = new Bundle();
        args.putBoolean("show_description", showDescription);
        if (description != null) {
            args.putString("description", description);
        }
        ProgressDialog fragment = new ProgressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        mShowDescription = args.getBoolean("show_description", DEFAULT_SHOW_DESCRIPTION);
        mDescription = args.getString("description", DEFAULT_DESCRIPTION == -1 ? null : getString(DEFAULT_DESCRIPTION));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progress_dialog, container, false);
        mBaseDialogDescription = view.findViewById(R.id.base_dialog_description);
        if (mShowDescription) {
            mBaseDialogDescription.setText(mDescription);
            mBaseDialogDescription.setVisibility(View.VISIBLE);
        } else {
            mBaseDialogDescription.setVisibility(View.GONE);
        }
        return view;
    }

    /**
     * 是否显示位于顶部的描述
     * @return
     */
    protected void setShowDescription(boolean showDescription) {
        mShowDescription = showDescription;
    }

    /**
     * 设置位于顶部的描述
     * @return
     */
    protected void setDescription(String description) {
        mDescription = description;
    }

}
