package com.alperez.widget;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.Checkable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * Created by stanislav.perchenko on 2/6/2019
 */
public interface AnimatedCheckable extends Checkable {

    int CHECKABLE_STATE_UNCHECKED   = 0;
    int CHECKABLE_STATE_CHECKING    = 1;
    int CHECKABLE_STATE_CHECKED     = 2;
    int CHECKABLE_STATE_UNCHECKING  = 3;

    @IntDef({CHECKABLE_STATE_UNCHECKED, CHECKABLE_STATE_CHECKING, CHECKABLE_STATE_CHECKED, CHECKABLE_STATE_UNCHECKING})
    @Retention(RetentionPolicy.SOURCE)
    @interface AnimatedCheckableState {}


    @AnimatedCheckableState
    int getCheckedState();

    /**
     * Change the checked state of the view to the inverse of its current state
     * with animation
     */
    void animatedToggle();

    /**
     * Change the checked state of the view with animation
     *
     * @param checked The new checked state
     */
    void setCheckedAnimated(boolean checked);

    void addOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener);

    void removeOnCheckedChangeListener(@NonNull OnCheckedChangeListener listener);
}
