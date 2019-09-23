package com.yiwu.coustomview;

import android.os.Bundle;

import com.yiwu.coustomview.base.BaseActivity;
import com.yiwu.coustomview.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }
}
