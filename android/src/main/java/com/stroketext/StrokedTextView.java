package com.stroketext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatTextView;

public class StrokedTextView extends AppCompatTextView {

    private float strokeWidthPx = 0f;
    private int strokeColor = 0xFF000000;
    private int fillColor = 0xFF000000;

    public StrokedTextView(Context context) {
        super(context);
        setFontSize(14f);
    }

    public void setStrokeColor(int color) {
        if (this.strokeColor != color) {
            this.strokeColor = color;
            invalidate();
        }
    }

    public void setStrokeColor(String color) {
        setStrokeColor(parseColor(color));
    }

    public void setFontSize(float fontSize) {
        float fontSizePx = (float) Math.ceil(getScaledSize(fontSize));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizePx);
    }

    public void setStrokeWidth(float width) {
        float newWidth = getScaledSize(Math.max(0f, width));
        if (this.strokeWidthPx != newWidth) {
            this.strokeWidthPx = newWidth;
            int strokeInsetPx = (int) Math.ceil(newWidth);
            setPadding(strokeInsetPx, strokeInsetPx, strokeInsetPx, strokeInsetPx);
            invalidate();
            requestLayout();
        }
    }

    @Override
    public void setTextColor(int color) {
        this.fillColor = color;
        super.setTextColor(color);
    }

    public void setAlign(String align) {
        if ("center".equals(align)) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        } else if ("right".equals(align)) {
            setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
        } else {
            setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
        }
    }

    public void setNumberOfLines(int numberOfLines) {
        setMaxLines(numberOfLines <= 0 ? Integer.MAX_VALUE : numberOfLines);
    }

    public void setEllipsis(boolean ellipsis) {
        setEllipsize(ellipsis ? android.text.TextUtils.TruncateAt.END : null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (strokeWidthPx > 0) {
            // Save original color
            int originalColor = getCurrentTextColor();

            // Draw stroke
            TextPaint paint = getPaint();
            Paint.Style originalStyle = paint.getStyle();
            float originalStrokeWidth = paint.getStrokeWidth();

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidthPx);
            paint.setStrokeJoin(Paint.Join.ROUND);
            super.setTextColor(strokeColor);
            super.onDraw(canvas);

            // Draw fill
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(originalStrokeWidth);
            super.setTextColor(fillColor);
            super.onDraw(canvas);

            // Restore
            paint.setStyle(originalStyle);
        } else {
            super.onDraw(canvas);
        }
    }

    private float getScaledSize(float size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    private int parseColor(String color) {
        if (color == null) return 0xFF000000;
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            if (color.startsWith("rgb")) return parseRgbColor(color);
            return 0xFF000000;
        }
    }

    private int parseRgbColor(String color) {
        try {
            String[] parts = color.replaceAll("[rgba()\\s]", "").split(",");
            int r = Integer.parseInt(parts[0]);
            int g = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);
            int a = parts.length > 3 ? (int) (Float.parseFloat(parts[3]) * 255) : 255;
            return Color.argb(a, r, g, b);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }
}
