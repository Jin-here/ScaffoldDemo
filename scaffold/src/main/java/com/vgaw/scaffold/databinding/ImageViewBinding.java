package com.vgaw.scaffold.databinding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.adapters.ImageViewBindingAdapter;

/**
 * @author caojin
 * @date 2018/6/8
 */
@BindingMethods({
        @BindingMethod(type = ImageView.class,
                attribute = "android:background",
                method = "setBackgroundResource"),
})
public class ImageViewBinding extends ImageViewBindingAdapter {
    @BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
    public static void setImageUrl(ImageView imageView, String url, Drawable placeHolder) {
        if (url == null) {
            imageView.setImageDrawable(placeHolder);
        } else {
            //ImageLoader.load(imageView.getContext(), url, imageView, placeHolder);
        }
    }
}
