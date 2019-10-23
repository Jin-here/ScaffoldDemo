package com.vgaw.scaffold.databinding;

import android.widget.AbsSpinner;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

/**
 * Created by caojin on 2017/3/21.
 */

@BindingMethods({
        @BindingMethod(type = AbsSpinner.class,
                attribute = "android:selection",
                method = "setSelection"),
})
public class AbsSpinnerBindingAdapter extends androidx.databinding.adapters.AbsSpinnerBindingAdapter {
}
