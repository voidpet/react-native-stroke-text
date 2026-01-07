package com.stroketext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View.MeasureSpec;
import androidx.appcompat.widget.AppCompatTextView;

public class StrokedTextView extends AppCompatTextView {

    private float strokeWidthPx = 0f;
    private int strokeColor = 0xFF000000;
    private int fillColor = 0xFF000000;
    private boolean needsRemeasure = false;

    public StrokedTextView(Context context) {
        super(context);
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

    public void setStrokeWidth(float width) {
        float newWidth = getScaledSize(width);
        if (this.strokeWidthPx != newWidth) {
            this.strokeWidthPx = newWidth;
            needsRemeasure = true;
            invalidate();
            requestLayout();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        needsRemeasure = true;
        requestLayout();
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        needsRemeasure = true;
        requestLayout();
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Fabric passes EXACTLY 0 - we need to change to UNSPECIFIED to let TextView measure itself
        if (widthMode == MeasureSpec.EXACTLY && widthSize == 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        if (heightMode == MeasureSpec.EXACTLY && heightSize == 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Add padding for stroke
        int extraWidth = (int) Math.ceil(strokeWidthPx * 2);
        int extraHeight = (int) Math.ceil(strokeWidthPx * 2);

        int measuredWidth = getMeasuredWidth() + extraWidth;
        int measuredHeight = getMeasuredHeight() + extraHeight;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // If Fabric gave us 0x0, force proper size while preserving position
        if ((right - left == 0 || bottom - top == 0) && needsRemeasure) {
            needsRemeasure = false;
            post(new Runnable() {
                @Override
                public void run() {
                    // Measure ourselves
                    measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                           MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                    int w = getMeasuredWidth();
                    int h = getMeasuredHeight();

                    // Use current center position to recalculate bounds
                    int centerX = (getLeft() + getRight()) / 2;
                    int centerY = (getTop() + getBottom()) / 2;

                    int newLeft = centerX - w / 2;
                    int newTop = centerY - h / 2;
                    int newRight = newLeft + w;
                    int newBottom = newTop + h;

                    layout(newLeft, newTop, newRight, newBottom);
                }
            });
        }
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
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_SP,
            size,
            getResources().getDisplayMetrics()
        );
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
