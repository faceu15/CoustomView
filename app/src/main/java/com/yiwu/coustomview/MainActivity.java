package com.yiwu.coustomview;

import android.view.View;

import com.yiwu.coustomview.base.BaseActivity;
import com.yiwu.coustomview.databinding.ActivityMainBinding;
import com.yiwu.coustomview.view.LevelView;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener {

    private LevelView mLevelView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mViewDataBinding.btnAdd.setOnClickListener(this);
        mLevelView = mViewDataBinding.level;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
//                mLevelView.setLevel(mLevelView.getLevel() + 1);
                mViewDataBinding.ringView.setValue(mViewDataBinding.ringView.getValue() + 20);
                break;
        }
    }
}
