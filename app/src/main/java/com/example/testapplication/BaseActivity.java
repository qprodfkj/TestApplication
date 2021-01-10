package com.example.testapplication;

import android.app.Activity;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.util.L;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Activity mActivity;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        L.d(TAG, "setContentView()   and ButterKnife.bind()");
    }
}
