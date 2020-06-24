package com.vgaw.scaffold.view;

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.vgaw.scaffold.R

class LoadingView : AppCompatImageView {
    private var mAnimator: ValueAnimator? = null
    private var mAttached = false
    private var mAnimEnabled = true
    private var mStartRotation = 0.0F

    private val sInterpolator: TimeInterpolator = LinearInterpolator()

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun setAnimEnabled(animEnabled: Boolean) {
        if (this.mAnimEnabled != animEnabled) {
            this.mAnimEnabled = animEnabled
            if (animEnabled && mAttached && getVisibility() == View.VISIBLE) {
                startProgress()
            } else {
                stopProgress()
            }
        }
    }

    fun isAnimEnabled() = mAnimEnabled

    fun setStartRotation(rotation: Float) {
        this.mStartRotation = rotation
        updateRotation(0F)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startProgress()
        } else {
            stopProgress()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttached = true
        startProgress()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAttached = false
        stopProgress()
    }

    private fun init() {
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.loading))
    }

    private fun startProgress() {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            mAnimator = null
        }

        if (!mAnimEnabled) {
            return
        }

        mAnimator = ValueAnimator.ofFloat(0F, 1F)
        mAnimator!!.run {
            repeatCount = ValueAnimator.INFINITE
            duration = 1000
            interpolator = sInterpolator
            addUpdateListener { animation ->
                val percent = animation.animatedValue as Float
                updateRotation(percent)
            }
            start()
        }
    }

    private fun stopProgress() {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            mAnimator = null
        }

        updateRotation(0F)
    }

    private fun updateRotation(percent: Float) {
        pivotX = (measuredWidth shr  1).toFloat()
        pivotY = (measuredHeight shr 1).toFloat()
        rotation = 360 * percent + mStartRotation
    }
}
