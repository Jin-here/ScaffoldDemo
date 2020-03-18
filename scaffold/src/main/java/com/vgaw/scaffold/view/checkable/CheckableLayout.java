package com.vgaw.scaffold.view.checkable;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vgaw.scaffold.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 特性：
 * 1. 支持单选/多选
 * 2. 支持checkableItem中间夹杂普通item
 *
 * 添加子view方法：
 * 1. xml方式: 创建任意实现{@link CheckableItemInterface}接口的View
 * 2. 代码方式: {@link #setAdapter(CheckableAdapter)}}添加子View
 */
public class CheckableLayout extends LinearLayout {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private SparseBooleanArray mCheckMap = new SparseBooleanArray();

    private int mOrientation = HORIZONTAL;
    private boolean mMultiChoice;

    private OnItemCheckedListener mListener;
    private CheckableAdapter mAdapter;

    private boolean mEqualization;
    private int mDividerSize;

    public CheckableLayout(Context context) {
        super(context);
        init(null);
    }

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CheckableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation == HORIZONTAL ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        mOrientation = orientation;
    }

    public boolean isEqualization() {
        return mEqualization;
    }

    public void setEqualization(boolean equalization) {
        this.mEqualization = equalization;
    }

    public int getDividerSize() {
        return mDividerSize;
    }

    public void setDividerSize(int dividerSize) {
        this.mDividerSize = dividerSize;
    }

    @Override
    public int getOrientation() {
        return mOrientation;
    }

    public void setMultiChoice(boolean multiChoice) {
        mMultiChoice = multiChoice;
    }

    public boolean getMultiChoice() {
        return mMultiChoice;
    }

    public void check(int index) {
        boolean checkedBefore = mCheckMap.get(index);
        if (!checkedBefore) {
            if (mMultiChoice) {
                checkItem(index, true);
            } else {
                checkSelfUncheckOther(index);
            }
        }
    }

    public List<Integer> getCheckedIndex() {
        List<Integer> resultList = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0;i < childCount;i++) {
            if (mCheckMap.get(i)) {
                resultList.add(i);
            }
        }
        return resultList;
    }

    /**
     * valid when {@link #mMultiChoice} is true
     * @param indexArray
     */
    public void check(int[] indexArray) {
        check(indexArray, true);
    }

    /**
     * valid when {@link #mMultiChoice} is true
     * @param index
     */
    public void uncheck(int index) {
        check(new int[]{index}, false);
    }

    /**
     * valid when {@link #mMultiChoice} is true
     * @param indexArray
     */
    public void uncheck(int[] indexArray) {
        check(indexArray, false);
    }

    /**
     * valid when {@link #mMultiChoice} is true
     * @param indexArray
     */
    private void check(int[] indexArray, boolean check) {
        if (mMultiChoice && indexArray != null && indexArray.length > 0) {
            for (int index : indexArray) {
                boolean mCheckedBefore = mCheckMap.get(index);
                if (mCheckedBefore^check) {
                    checkItem(index, check);
                }
            }
        }
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        mListener = listener;
    }

    public void setAdapter(CheckableAdapter adapter) {
        mAdapter = adapter;

        int childCount = mAdapter.getCount();
        for (int i = 0;i < childCount;i++) {
            View child = mAdapter.createView(i);

            LayoutParams params = null;
            if (mEqualization) {
                params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
            } else {
                params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            if (mDividerSize != 0) {
                if (i < childCount - 1) {
                    params.rightMargin = mDividerSize;
                }
            }
            addView(child, params);
        }

        initChildView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initChildView();
    }

    private void initChildView() {
        int childCount = getChildCount();
        for (int i = 0;i < childCount;i++) {
            View child = getChildAt(i);
            if (child instanceof CheckableItemInterface) {
                child.setOnClickListener(new ChildClickListener(i));
                checkItem(i, false);
            }
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CheckableLayout);
            mOrientation = array.getInteger(R.styleable.CheckableLayout_checkableOrientation, HORIZONTAL);
            mMultiChoice = array.getBoolean(R.styleable.CheckableLayout_checkableMultiChoice, false);
            mEqualization = array.getBoolean(R.styleable.CheckableLayout_checkableEqualization, false);
            mDividerSize = array.getDimensionPixelSize(R.styleable.CheckableLayout_checkableDividerSize, 0);

            array.recycle();
        }
        setOrientation(mOrientation == HORIZONTAL ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
    }

    private void checkItem(int index, boolean check) {
        View child = getChildAt(index);
        if (child instanceof CheckableItemInterface) {
            mCheckMap.put(index, check);
            ((CheckableItemInterface) child).onCheckChanged(check);
            if (check) {
                callItemChecked(index);
            }
        }
    }

    private void checkSelfUncheckOther(int index) {
        int childCount = getChildCount();
        for (int i = 0;i < childCount;i++) {
            boolean self = (i == index);
            checkItem(i, self);
        }
    }

    private void callItemChecked(int index) {
        if (mListener != null) {
            mListener.onItemChecked(index);
        }
    }

    private class ChildClickListener implements OnClickListener {
        private int mIndex;

        public ChildClickListener(int index) {
            mIndex = index;
        }

        @Override
        public void onClick(View v) {
            boolean checkedBefore = mCheckMap.get(mIndex);
            if (mMultiChoice && checkedBefore) {
                uncheck(mIndex);
            } else {
                check(mIndex);
            }
        }
    }
}
