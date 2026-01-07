package com.stroketext;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;

import androidx.annotation.Nullable;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;

class StrokeTextShadowNode extends LayoutShadowNode implements YogaMeasureFunction {
    private String text = "";
    private float fontSize = 14;
    private float strokeWidth = 0;
    private String fontFamily = "sans-serif";
    private int numberOfLines = 0;
    private boolean ellipsis = false;
    private Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;

    private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public StrokeTextShadowNode() {
        setMeasureFunction(this);
    }

    @ReactProp(name = "text")
    public void setText(@Nullable String text) {
        this.text = text == null ? "" : text;
        dirty();
    }

    @ReactProp(name = "fontSize")
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        dirty();
    }

    @ReactProp(name = "strokeWidth")
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        dirty();
    }

    @ReactProp(name = "fontFamily")
    public void setFontFamily(@Nullable String fontFamily) {
        this.fontFamily = fontFamily == null ? "sans-serif" : fontFamily;
        dirty();
    }

    @ReactProp(name = "align")
    public void setAlign(@Nullable String align) {
        if ("center".equals(align)) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if ("right".equals(align)) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        } else {
            alignment = Layout.Alignment.ALIGN_NORMAL;
        }
        dirty();
    }

    @ReactProp(name = "numberOfLines")
    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
        dirty();
    }

    @ReactProp(name = "ellipsis")
    public void setEllipsis(boolean ellipsis) {
        this.ellipsis = ellipsis;
        dirty();
    }

    @Override
    public long measure(YogaNode node, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode) {
        ThemedReactContext context = getThemedContext();
        if (context == null) {
            return YogaMeasureOutput.make(0, 0);
        }

        Typeface typeface = FontUtil.getFont(context, fontFamily);
        float scaledFontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fontSize, context.getResources().getDisplayMetrics());
        float strokeWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, strokeWidth, context.getResources().getDisplayMetrics());

        textPaint.setTypeface(typeface);
        textPaint.setTextSize(scaledFontSize);

        float widthPx;
        if (widthMode == YogaMeasureMode.EXACTLY) {
            widthPx = width;
        } else {
            float maxLineWidth = 0;
            String[] lines = text.split("\n");
            for (String line : lines) {
                maxLineWidth = Math.max(maxLineWidth, textPaint.measureText(line));
            }
            widthPx = maxLineWidth + strokeWidthPx;
            if (widthMode == YogaMeasureMode.AT_MOST) {
                widthPx = Math.min(widthPx, width);
            }
        }

        if (widthPx <= 0) {
            return YogaMeasureOutput.make(0, 0);
        }

        int layoutWidth = (int) Math.ceil(widthPx);
        CharSequence ellipsizedText = ellipsis
                ? TextUtils.ellipsize(text, textPaint, layoutWidth, TextUtils.TruncateAt.END)
                : text;

        StaticLayout textLayout = new StaticLayout(ellipsizedText, textPaint, layoutWidth, alignment, 1.0f, 0.0f, false);
        if (numberOfLines > 0 && numberOfLines < textLayout.getLineCount()) {
            int lineEnd = textLayout.getLineEnd(numberOfLines - 1);
            CharSequence trimmed = ellipsizedText.subSequence(0, lineEnd);
            textLayout = new StaticLayout(trimmed, textPaint, layoutWidth, alignment, 1.0f, 0.0f, false);
        }

        float heightPx;
        if (heightMode == YogaMeasureMode.EXACTLY) {
            heightPx = height;
        } else {
            heightPx = textLayout.getHeight();
            if (heightMode == YogaMeasureMode.AT_MOST) {
                heightPx = Math.min(heightPx, height);
            }
        }

        return YogaMeasureOutput.make(widthPx, heightPx);
    }
}
