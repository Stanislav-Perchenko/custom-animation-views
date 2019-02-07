package com.alperez.widget;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stanislav.perchenko on 2/6/2019
 */
public class AnimatedCheckableImageView extends ImageView implements AnimatedCheckable {

    private int argCheckedPadding = 24;
    private float argDuration = 600;
    private int argFPS = 35;


    @AnimatedCheckableState
    private int mCheckedState = CHECKABLE_STATE_UNCHECKED;

    /**
     * This is the animation status value in the range of [0, 1]
     * value 0 - the View is fully unselected. This is the start point for the forward animation and END point for the backward animation
     * value 1 - the View is fully selected. This is the START point for backward animation and the END point for the forward animation
     */
    private float mCurrAnimValue = 0;

    private ValueAnimator animEngine;

    private List<OnCheckedChangeListener> mCheckedChangeListeners;

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
        argDuration = a.getInt(R.styleable.AnimatedCheckableFrameLayout_anim_duration, (int) argDuration);
        argFPS = a.getInt(R.styleable.AnimatedCheckableFrameLayout_anim_fps, argFPS);
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

    public int getCheckedPadding() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        return argCheckedPadding;
    }

    @Override
    public void setChecked(boolean checked) {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        setCheckedInternal(checked, false);
    }

    @Override
    public boolean isChecked() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        return (mCheckedState == CHECKABLE_STATE_CHECKED) || (mCheckedState == CHECKABLE_STATE_UNCHECKING);
    }

    @Override
    public void toggle() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        setCheckedInternal(!isChecked(), false);
    }

    @Override
    @AnimatedCheckableState
    public int getCheckedState() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        return mCheckedState;
    }

    @Override
    public void animatedToggle() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        boolean targetChecked = ((mCheckedState == CHECKABLE_STATE_UNCHECKED) || (mCheckedState == CHECKABLE_STATE_UNCHECKING));
        setCheckedAnimated(targetChecked);
    }

    @Override
    public void setCheckedAnimated(boolean checked) {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        float v1, v2;
        long animLen;
        if (checked && ((mCheckedState == CHECKABLE_STATE_CHECKED) || (mCheckedState == CHECKABLE_STATE_CHECKING))) {
            return;
        } else if (!checked && ((mCheckedState == CHECKABLE_STATE_UNCHECKED) || (mCheckedState == CHECKABLE_STATE_UNCHECKING))) {
            return;
        } else if (checked) {
            //---  Animate forward  ---
            v1 = (mCurrAnimValue < 1f) ? mCurrAnimValue : 0;
            v2 = 1;
            animLen = Math.round(argDuration * (1 - v1));
            mCheckedState = CHECKABLE_STATE_CHECKING;
        } else {
            //---  Animate backward  ---
            v1 = (mCurrAnimValue > 0) ? mCurrAnimValue : 1f;
            v2 = 0;
            animLen = Math.round(argDuration * v1);
            mCheckedState = CHECKABLE_STATE_UNCHECKING;
        }

        if (animEngine == null) {
            animEngine = createAnimatorEngine();
        } else {
            cancelCurrentAnimation();
        }
        animEngine.setFloatValues(v1, v2);
        animEngine.setDuration(animLen);
        animEngine.setCurrentPlayTime(0);
        animEngine.start();
    }

    @Override
    public void addOnCheckedChangeListener(OnCheckedChangeListener listener) {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        assert (listener != null);
        if (mCheckedChangeListeners == null) {
            mCheckedChangeListeners = new ArrayList<>();
        }
        mCheckedChangeListeners.add(listener);
    }

    @Override
    public void removeOnCheckedChangeListener(OnCheckedChangeListener listener) {//+++++++++++++++++++++++++++++++++
        assert (listener != null);
        if (mCheckedChangeListeners == null) {
            // This can happen if this method is called before the first call to addDrawerListener
            return;
        }
        mCheckedChangeListeners.remove(listener);
    }




    private void setCheckedInternal(boolean checked, boolean callback) {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        int targCheckState = checked ? CHECKABLE_STATE_CHECKED : CHECKABLE_STATE_UNCHECKED;
        if (mCheckedState != targCheckState) {
            cancelCurrentAnimation();
            mCheckedState = targCheckState;
            mCurrAnimValue = checked ? 1 : 0;
            invalidate();
            if (callback && (mCheckedChangeListeners != null)) {
                // Notify the listeners. Do that from the end of the list so that if a listener
                // removes itself as the result of being called, it won't mess up with our iteration
                int listenerCount = mCheckedChangeListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) mCheckedChangeListeners.get(i).onCheckedChanged(this, checked);
            }
        }
    }

    private void cancelCurrentAnimation() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        if ((mCheckedState == CHECKABLE_STATE_CHECKING) || (mCheckedState == CHECKABLE_STATE_UNCHECKING)) {
            animEngine.cancel();
        }
    }



    private ValueAnimator createAnimatorEngine() {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        ValueAnimator va = new ValueAnimator();
        va.setInterpolator(null); //This will setup default Linear interpolator.
        va.setEvaluator(new FloatEvaluator());
        va.setRepeatCount(1);
        va.setFrameDelay(Math.round(1000.0 / argFPS));
        va.setStartDelay(0);
        va.addUpdateListener(animation -> {
            float v = ((Float) animation.getAnimatedValue()).floatValue();
            if (mCheckedState == CHECKABLE_STATE_CHECKED) {
                mCurrAnimValue = 1f;
                invalidate();
            } else if (mCheckedState == CHECKABLE_STATE_UNCHECKED) {
                mCurrAnimValue = 0;
                invalidate();
            } else if (mCheckedState == CHECKABLE_STATE_CHECKING && isFloatEquals(v, 1f)) {
                setCheckedInternal(true, true);
            } else if (mCheckedState == CHECKABLE_STATE_UNCHECKING && isFloatEquals(v, 0)) {
                setCheckedInternal(false, true);
            } else {
                mCurrAnimValue = v;
                invalidate();
            }
        });
        return va;
    }

    private void assertUiThread() {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            throw new IllegalThreadStateException("This must be the UI thread");
        }
    }

    private boolean isFloatEquals(float f1, float f2) {//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
        float df = f1 - f2;
        if (df >= 0) {
            return (df < 0.000001f);
        } else {
            return (df > -0.000001f);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mCheckedState == CHECKABLE_STATE_UNCHECKED) {
            super.onDraw(canvas);
        } else {
            final float w = getMeasuredWidth();
            final float h = getMeasuredHeight();

            float sx = (w - 2*argCheckedPadding) / w*mCurrAnimValue;
            float sy = (h - 2*argCheckedPadding) / h*mCurrAnimValue;
            final int mark = canvas.save();
            canvas.scale(sx, sy, w/2f, h/2f);
            super.onDraw(canvas);
            canvas.restoreToCount(mark);
        }
    }
}
