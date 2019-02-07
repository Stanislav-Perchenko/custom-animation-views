package com.alperez.samples.demoactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alperez.samples.R;
import com.alperez.widget.AnimatedCheckableImageView;

/**
 * Created by stanislav.perchenko on 2/6/2019
 */
public class AnimatedCheckDemoActivity extends BaseDemoActivity {

    AnimatedCheckableImageView vImage;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_demo_animated_check;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        vImage = (AnimatedCheckableImageView) findViewById(R.id.animated_image);

        findViewById(R.id.action_toggle_iv_no_anim).setOnClickListener(v -> vImage.toggle());
        findViewById(R.id.action_toggle_iv_with_anim).setOnClickListener(v -> vImage.animatedToggle());
    }
}
