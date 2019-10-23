package com.vgaw.scaffold.view.rcv.decoration;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vgaw.scaffold.util.phone.DensityUtil;

public class GridDividerItemDecoration extends BaseItemDecoration {
    private Paint mPaint;
    private int mDividerWidth;

    private int mSpanCount;
    private int mOrientation;

    public GridDividerItemDecoration(Context context, int width) {
        this(context, width, android.R.color.transparent);
    }

    public GridDividerItemDecoration(Context context, int width, @ColorRes int color) {
        mDividerWidth = DensityUtil.dp2px(context, width);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(color));
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 纵向：
     * 列：如果数量等于2，则各切一般；如果数量大于2
     * 行：切各自下方；
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof GridLayoutManager)) {
            return;
        } else {
            mSpanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            mOrientation = ((GridLayoutManager) layoutManager).getOrientation();
        }

        if (mOrientation == RecyclerView.VERTICAL) {
            //int itemPosition = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            int itemPosition = parent.getChildAdapterPosition(view);
            int itemCount = parent.getAdapter().getItemCount();
            // 过滤掉header和footer
            if (headerOrFooter(itemPosition, itemCount)) {
                outRect.set(0, 0, 0, 0);
                return;
            }

            itemPosition = getActualItmePosition(itemPosition);

            int left = getLeft(itemPosition);
            int right = getRight(itemPosition);
            int top = 0;
            int bottom = mDividerWidth;

            // 不区分最后一行
            /*if (lastRow(parent, itemPosition, itemCount)) {
                bottom = 0;
            }*/
            outRect.set(left, top, right, bottom);
        }
    }

    private int getLeft(int pos) {
        if (firstCol(pos)) {
            return 0;
        } else {
            if (mSpanCount == 2) {
                return mDividerWidth / 2;
            } else {
                int mod = (pos % mSpanCount);
                return (mDividerWidth * mod / mSpanCount);
            }
        }
    }

    private int getRight(int pos) {
        if (lastCol(pos)) {
            return 0;
        } else {
            if (mSpanCount == 2) {
                return mDividerWidth / 2;
            } else {
                int mod = (pos % mSpanCount);
                return (mDividerWidth * (mSpanCount - mod) / mSpanCount);
            }
        }
    }

    /*@Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childSize = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            int itemPosition = parent.getChildAdapterPosition(child);
            if (headerOrFooter(itemPosition, itemCount)) {
                continue;
            }

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left;
            int right;
            int top;
            int bottom;

            itemPosition = getActualItmePosition(itemPosition);

            //画水平分隔线
            boolean lstRow = lastRow(parent, itemPosition, itemCount);
            if (!lstRow) {
                left = child.getLeft();
                right = child.getRight();
                top = child.getBottom() + layoutParams.bottomMargin;
                bottom = top + mDividerWidth;

                c.drawRect(left, top, right, bottom, mPaint);
            }

            //画垂直分割线
            if (!lastCol(parent, itemPosition, itemCount)) {
                top = child.getTop();
                // 如果是最后一行，则竖线高度有所变化
                bottom = child.getBottom() + (lstRow ? 0 : mDividerWidth);
                left = child.getRight() + layoutParams.rightMargin;
                right = left + mDividerWidth;

                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }*/

    private boolean firstCol(int pos) {
        return (pos) % mSpanCount == 0;
    }

    private boolean lastCol(int pos) {
        return (pos + 1) % mSpanCount == 0;
    }

    private boolean firstRow(int pos) {
        return (pos < mSpanCount);
    }

    private boolean lastRow(int pos, int itemCount) {
        // childCount = childCount - childCount % mSpanCount;
        int lines = itemCount % mSpanCount == 0 ? itemCount / mSpanCount : itemCount / mSpanCount + 1;
        return lines == pos / mSpanCount + 1;
    }
}