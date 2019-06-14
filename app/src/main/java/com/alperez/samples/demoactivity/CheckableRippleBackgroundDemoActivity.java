package com.alperez.samples.demoactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Checkable;

import com.alperez.samples.R;

public class CheckableRippleBackgroundDemoActivity extends BaseDemoActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_demo_checkable_ripple_background;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.txt_switch).setOnClickListener(v -> {
            Checkable chk = (Checkable) v;
            chk.setChecked(!chk.isChecked());
        });
    }
}
