package com.stroketext;

import android.content.Context;
import android.graphics.Typeface;

import com.facebook.react.common.assets.ReactFontManager;

public class FontUtil {

    public static Typeface getFont(Context context, String fontFamily) {
        String resolvedFontFamily = fontFamily == null ? "sans-serif" : fontFamily;
        return ReactFontManager.getInstance()
                .getTypeface(resolvedFontFamily, Typeface.NORMAL, context.getAssets());
    }
}
