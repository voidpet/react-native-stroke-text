package com.stroketext;

import androidx.annotation.Nullable;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.viewmanagers.StrokeTextViewManagerInterface;
import com.facebook.react.viewmanagers.StrokeTextViewManagerDelegate;

@ReactModule(name = StrokeTextViewManager.REACT_CLASS)
public class StrokeTextViewManager extends SimpleViewManager<StrokedTextView> implements StrokeTextViewManagerInterface<StrokedTextView> {
    public static final String REACT_CLASS = "StrokeTextView";

    private final ViewManagerDelegate<StrokedTextView> mDelegate;

    public StrokeTextViewManager() {
        mDelegate = new StrokeTextViewManagerDelegate<>(this);
    }

    @Nullable
    @Override
    protected ViewManagerDelegate<StrokedTextView> getDelegate() {
        return mDelegate;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public StrokedTextView createViewInstance(ThemedReactContext reactContext) {
        return new StrokedTextView(reactContext);
    }

    @Override
    @ReactProp(name = "text")
    public void setText(StrokedTextView view, @Nullable String text) {
        view.setText(text);
    }

    @Override
    @ReactProp(name = "fontSize")
    public void setFontSize(StrokedTextView view, float fontSize) {
        view.setTextSize(fontSize);
    }

    @Override
    @ReactProp(name = "color")
    public void setColor(StrokedTextView view, @Nullable String color) {
        view.setTextColor(parseColor(color));
    }

    @Override
    @ReactProp(name = "strokeColor")
    public void setStrokeColor(StrokedTextView view, @Nullable String strokeColor) {
        view.setStrokeColor(strokeColor);
    }

    @Override
    @ReactProp(name = "strokeWidth")
    public void setStrokeWidth(StrokedTextView view, float strokeWidth) {
        view.setStrokeWidth(strokeWidth);
    }

    @Override
    @ReactProp(name = "fontFamily")
    public void setFontFamily(StrokedTextView view, @Nullable String fontFamily) {
        view.setTypeface(FontUtil.getFont(view.getContext(), fontFamily));
    }

    @Override
    @ReactProp(name = "align")
    public void setAlign(StrokedTextView view, @Nullable String align) {
        view.setAlign(align);
    }

    @Override
    @ReactProp(name = "numberOfLines")
    public void setNumberOfLines(StrokedTextView view, int numberOfLines) {
        view.setNumberOfLines(numberOfLines);
    }

    @Override
    @ReactProp(name = "ellipsis")
    public void setEllipsis(StrokedTextView view, boolean ellipsis) {
        view.setEllipsis(ellipsis);
    }

    @Override
    @ReactProp(name = "width")
    public void setWidth(StrokedTextView view, float width) {
        // Width is handled by React Native's layout system
    }

    private int parseColor(String color) {
        if (color == null) return 0xFF000000;
        try {
            return android.graphics.Color.parseColor(color);
        } catch (Exception e) {
            return 0xFF000000;
        }
    }
}
