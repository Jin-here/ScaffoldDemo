package com.vgaw.scaffold.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.vgaw.scaffold.R;

/**
 * 1. 如果有自定义View，推荐使用继承方式;
 * 2. 推荐通过newInstance()方式使用，set方法中除了各种listener可以直接使用外，其他set方法建议只在继承类中使用;
 */
public class BaseDialog extends DialogFragment {
    private static final boolean DEFAULT_SHOW_DESCRIPTION = true;
    private static final @StringRes
    int DEFAULT_DESCRIPTION = -1;
    private static final boolean DEFAULT_SINGLE_BTN = false;
    private static final @StringRes
    int DEFAULT_SINGLE_BTN_DESCRIPTION = -1;
    private static final @StringRes
    int DEFAULT_LEFT_BTN_DESCRIPTION = R.string.base_dialog_cancel;
    private static final @StringRes
    int DEFAULT_RIGHT_BTN_DESCRIPTION = R.string.base_dialog_ok;

    private TextView mBaseDialogDescription;
    private TextView mBaseDialogCancel;
    private TextView mBaseDialogOk;
    private TextView mBaseDialogSingle;
    private View.OnClickListener mLeftBtnClickListener;
    private View.OnClickListener mRightBtnClickListener;
    private View.OnClickListener mSingleBtnClickListener;

    private boolean mShowDescription;
    private String mDescription;
    private boolean mSingleBtn;
    private String mSingleBtnDescription;
    private String mLeftBtnDescription;
    private String mRightBtnDescription;
    private View mMainContent;
    private DialogInterface.OnDismissListener mDismissListener;

    public static BaseDialog newInstance(String description) {
        return newInstance(true, description, false, null, null, null);
    }

    public static BaseDialog newInstance(String description, String leftBtnDescription, String rightBtnDescription) {
        return newInstance(true, description, false, null, leftBtnDescription, rightBtnDescription);
    }

    public static BaseDialog newInstance(String description, String singleBtnDescription) {
        return newInstance(true, description, true, singleBtnDescription, null, null);
    }

    private static BaseDialog newInstance(boolean showDescription, String description,
                                          boolean singleBtn, String singleBtnDescription,
                                          String leftBtnDescription, String rightBtnDescription) {
        Bundle args = new Bundle();
        args.putBoolean("show_description", showDescription);
        if (description != null) {
            args.putString("description", description);
        }
        args.putBoolean("single_btn", singleBtn);
        if (singleBtnDescription != null) {
            args.putString("single_btn_description", singleBtnDescription);
        }
        if (leftBtnDescription != null) {
            args.putString("left_btn_description", leftBtnDescription);
        }
        if (rightBtnDescription != null) {
            args.putString("right_btn_description", rightBtnDescription);
        }

        BaseDialog fragment = new BaseDialog();
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
        mSingleBtn = args.getBoolean("single_btn", DEFAULT_SINGLE_BTN);
        mSingleBtnDescription = args.getString("single_btn_description", DEFAULT_SINGLE_BTN_DESCRIPTION == -1 ? null : getString(DEFAULT_SINGLE_BTN_DESCRIPTION));
        mLeftBtnDescription = args.getString("left_btn_description", DEFAULT_LEFT_BTN_DESCRIPTION == -1 ? null : getString(DEFAULT_LEFT_BTN_DESCRIPTION));
        mRightBtnDescription = args.getString("right_btn_description", DEFAULT_RIGHT_BTN_DESCRIPTION == -1 ? null : getString(DEFAULT_RIGHT_BTN_DESCRIPTION));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_dialog, container, false);
        mBaseDialogDescription = view.findViewById(R.id.base_dialog_description);
        mBaseDialogCancel = view.findViewById(R.id.base_dialog_cancel);
        mBaseDialogOk = view.findViewById(R.id.base_dialog_ok);
        mBaseDialogSingle = view.findViewById(R.id.base_dialog_single);
        LinearLayout baseDialogContent = view.findViewById(R.id.base_dialog_content);
        if (mShowDescription) {
            mBaseDialogDescription.setText(mDescription);
            mBaseDialogDescription.setVisibility(View.VISIBLE);
        } else {
            mBaseDialogDescription.setVisibility(View.GONE);
        }

        if (mMainContent != null) {
            baseDialogContent.addView(mMainContent, 1);
        }

        if (mSingleBtn) {
            mBaseDialogCancel.setVisibility(View.GONE);
            mBaseDialogOk.setVisibility(View.GONE);
            mBaseDialogSingle.setText(mSingleBtnDescription);
            mBaseDialogSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callSingleBtnClickListener(v);
                }
            });
            mBaseDialogSingle.setVisibility(View.VISIBLE);
        } else {
            mBaseDialogCancel.setText(mLeftBtnDescription);
            mBaseDialogOk.setText(mRightBtnDescription);
            mBaseDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callLeftBtnClickListener(v);
                }
            });
            mBaseDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callRigthBtnClickListener(v);
                }
            });
            mBaseDialogCancel.setVisibility(View.VISIBLE);
            mBaseDialogOk.setVisibility(View.VISIBLE);
            mBaseDialogSingle.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDismissListener != null) {
            mDismissListener.onDismiss(null);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDismissListener = listener;
    }

    public void setOnLeftBtnClickListener(View.OnClickListener listener) {
        mLeftBtnClickListener = listener;
    }

    public void setOnRightBtnClickListener(View.OnClickListener listener) {
        mRightBtnClickListener = listener;
    }

    public void setOnSingleBtnClickListener(View.OnClickListener listener) {
        mSingleBtnClickListener = listener;
    }

    /**
     * 该处获得的view显示在顶部描述和底部按钮之间
     * @return
     */
    protected void setMainContent(View mainContent) {
        mMainContent = mainContent;
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

    /**
     * 底部是否仅显示一个按钮，即"取消"和"确定"按钮合并成一个大按钮
     * @return
     */
    protected void setSingleBtn(boolean singleBtn) {
        mSingleBtn = singleBtn;
    }

    protected void setSingleBtnDescription(String singleBtnDescription) {
        mSingleBtnDescription = singleBtnDescription;
    }

    protected void setLeftBtnDescription(String leftBtnDescription) {
        mLeftBtnDescription = leftBtnDescription;
    }

    protected void setRightBtnDescription(String rightBtnDescription) {
        mRightBtnDescription = rightBtnDescription;
    }

    private void callLeftBtnClickListener(View v) {
        if (mLeftBtnClickListener != null) {
            mLeftBtnClickListener.onClick(v);
        }
    }

    private void callRigthBtnClickListener(View v) {
        if (mRightBtnClickListener != null) {
            mRightBtnClickListener.onClick(v);
        }
    }

    private void callSingleBtnClickListener(View v) {
        if (mSingleBtnClickListener != null) {
            mSingleBtnClickListener.onClick(v);
        }
    }
}
