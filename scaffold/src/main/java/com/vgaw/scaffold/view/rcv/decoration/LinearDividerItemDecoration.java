package com.vgaw.scaffold.view.rcv.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vgaw.scaffold.util.phone.DensityUtil;

public class LinearDividerItemDecoration extends BaseItemDecoration {
    private final Drawable mDivider;
    private final int mSize;
    private final int mOrientation;

    public LinearDividerItemDecoration(Context context, int size, int orientation, @ColorRes int color) {
        mDivider = new ColorDrawable(context.getResources().getColor(color));
        mSize = DensityUtil.dp2px(context, size);
        mOrientation = orientation;
    }

    public LinearDividerItemDecoration(Context context, int size, int orientation) {
        this(context, size, orientation, android.R.color.transparent);
    }

    public LinearDividerItemDecoration(Context context, int size) {
        this(context, size, LinearLayoutManager.VERTICAL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        // 过滤掉header和footer
        if (headerOrFooter(itemPosition, itemCount)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        itemPosition = getActualItmePosition(itemPosition);
        if (lastItem(itemPosition, itemCount)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, mSize, 0);
        } else {
            outRect.set(0, 0, 0, mSize);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left;
        int right;
        int top;
        int bottom;

        int itemCount = parent.getAdapter().getItemCount();
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                // 过滤header和footer
                View child = parent.getChildAt(i);
                int itemPosition = parent.getChildAdapterPosition(child);
                if (headerOrFooter(itemPosition, itemCount)) {
                    continue;
                }

                itemPosition = getActualItmePosition(itemPosition);
                if (lastItem(itemPosition, itemCount)) {
                    continue;
                }

                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                left = child.getRight() + params.rightMargin;
                right = left + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                // 过滤header和footer
                int itemPosition = parent.getChildAdapterPosition(child);
                if (headerOrFooter(itemPosition, itemCount)) {
                    continue;
                }

                itemPosition = getActualItmePosition(itemPosition);
                if (lastItem(itemPosition, itemCount)) {
                    continue;
                }

                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private boolean lastItem(int pos, int itemCount) {
        return (pos == itemCount - 1);
    }
}