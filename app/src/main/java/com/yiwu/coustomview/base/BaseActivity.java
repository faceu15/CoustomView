package com.yiwu.coustomview.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @Author:yeev
 * @Date: Created in 19:54 2019/9/23
 * @Description:
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected Context mContext;
    protected T mViewDataBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mViewDataBinding = DataBindingUtil.setContentView(this, getContentViewId());
        mContext = this;
        init();
    }

    @LayoutRes
    protected abstract int getContentViewId();

    /**
     * init data and view
     */
    protected abstract void init();

}
