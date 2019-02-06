package com.alperez.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by stanislav.perchenko on 2/6/2019
 */
public class AnimatedCheckableImageView extends ImageView implements AnimatedCheckable {

    private int argCheckedPadding = 24;

    @AnimatedCheckableState
    private int mCheckedState = CHECKABLE_STATE_UNCHECKED;

    public AnimatedCheckableImageView(Context context) {
        super(context);
        extractArgs(context, null, 0, 0);
    }

    public AnimatedCheckableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        extractArgs(context, attrs, 0, 0);
    }

    public AnimatedCheckableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractArgs(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedCheckableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        extractArgs(context, attrs, defStyleAttr, defStyleRes);
    }

    private void extractArgs(Context c, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = c.getResources().obtainAttributes(attrs, R.styleable.AnimatedCheckableFrameLayout);
        argCheckedPadding = a.getDimensionPixelSize(R.styleable.AnimatedCheckableFrameLayout_checkedStatePadding, argCheckedPadding);
        a.recycle();
    }

    public void setCheckedPadding(int argCheckedPadding) {
        assertUiThread();
        if (this.argCheckedPadding != argCheckedPadding) {
            this.argCheckedPadding = argCheckedPadding;
            if (mCheckedState == CHECKABLE_STATE_CHECKED) {
                invalidate();
            }
        }
    }

    public int getCheckedPadding() {
        return argCheckedPadding;
    }

    @Override
    public void setChecked(boolean checked) {
        //TODO Implement this !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    @Override
    public boolean isChecked() {
        //TODO Implement this !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return false;
    }

    @Override
    public void toggle() {
        //TODO Implement this !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    private void assertUiThread() {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            throw new IllegalThreadStateException("This must be the UI thread");
        }
    }

    @Override
    @AnimatedCheckableState
    public int getCheckedState() {
        return mCheckedState;
    }
}
