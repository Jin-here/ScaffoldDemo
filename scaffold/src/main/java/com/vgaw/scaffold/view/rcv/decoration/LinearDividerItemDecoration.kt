package com.vgaw.scaffold.view.rcv.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vgaw.scaffold.R
import com.vgaw.scaffold.util.phone.DensityUtil

class LinearDividerItemDecoration(context: Context, size: Float = 0.5F, orientation: Int = LinearLayoutManager.VERTICAL, color: Int = R.color.divider_bg, marginLeft: Int = 0, marginRight: Int = 0): BaseItemDecoration() {
    private val mDivider: Drawable
    private val mSize: Int
    private val mOrientation: Int
    private val mMarginLeft: Int
    private val mMarginRight: Int
    
    init {
        mDivider = ColorDrawable(context.resources.getColor(color))
        mSize = DensityUtil.dp2px(context, size)
        mOrientation = orientation
        mMarginLeft = marginLeft
        mMarginRight = marginRight
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter!!.itemCount
        // 过滤掉header和footer
        if (headerOrFooter(itemPosition, itemCount)) {
            outRect.set(0, 0, 0, 0)
            return
        }

        itemPosition = getActualItmePosition(itemPosition)
        if (lastItem(itemPosition, itemCount)) {
            outRect.set(0, 0, 0, 0)
            return
        }

        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, mSize, 0)
        } else {
            outRect.set(0, 0, 0, mSize)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int

        val itemCount = parent.adapter!!.itemCount
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                // 过滤header和footer
                val child = parent.getChildAt(i)
                var itemPosition = parent.getChildAdapterPosition(child)
                if (headerOrFooter(itemPosition, itemCount)) {
                    continue
                }

                itemPosition = getActualItmePosition(itemPosition)
                if (lastItem(itemPosition, itemCount)) {
                    continue
                }

                val params = child.layoutParams as RecyclerView.LayoutParams
                left = child.getRight() + params.rightMargin
                right = left + mSize
                mDivider.setBounds(left, top + mMarginLeft, right, bottom - mMarginRight)
                mDivider.draw(c)
            }
        } else {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)

                // 过滤header和footer
                var itemPosition = parent.getChildAdapterPosition(child)
                if (headerOrFooter(itemPosition, itemCount)) {
                    continue
                }

                itemPosition = getActualItmePosition(itemPosition)
                if (lastItem(itemPosition, itemCount)) {
                    continue
                }

                val params =
                        child.layoutParams as RecyclerView.LayoutParams
                top = child.bottom + params.bottomMargin
                bottom = top + mSize
                mDivider.setBounds(left + mMarginLeft, top, right - mMarginRight, bottom)
                mDivider.draw(c)
            }
        }
    }

    private fun lastItem(pos: Int, itemCount: Int) = (pos == itemCount - 1)
}