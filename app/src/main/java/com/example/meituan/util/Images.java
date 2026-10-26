package com.example.meituan.util;

import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * 真实图片加载：从 assets 读取本地食物照片（离线可用），失败时回退为占位底色。
 */
public class Images {

    public static void load(ImageView view, String assetPath, int placeholderColor) {
        load(view, assetPath, placeholderColor, 10);
    }

    public static void load(ImageView view, String assetPath, int placeholderColor, int radiusDp) {
        ColorDrawable placeholder = new ColorDrawable(placeholderColor);
        if (assetPath == null || assetPath.isEmpty()) {
            view.setImageDrawable(placeholder);
            return;
        }
        float density = view.getContext().getResources().getDisplayMetrics().density;
        int radius = Math.round(radiusDp * density);
        RequestOptions options = new RequestOptions()
                .transform(new RoundedCorners(radius))
                .placeholder(placeholder)
                .error(placeholder);
        Glide.with(view.getContext())
                .load("file:///android_asset/" + assetPath)
                .apply(options)
                .centerCrop()
                .into(view);
    }
}
