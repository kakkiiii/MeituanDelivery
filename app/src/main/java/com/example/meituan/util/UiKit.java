package com.example.meituan.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * 简易 UI 工具：动态创建圆角/圆形色块（用颜色占位代替真实图片）
 */
public class UiKit {

    public static Drawable roundRect(Context c, int color, float radiusDp) {
        float r = radiusDp * c.getResources().getDisplayMetrics().density;
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(r);
        return g;
    }

    public static Drawable circle(Context c, int color) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setShape(GradientDrawable.OVAL);
        return g;
    }
}
