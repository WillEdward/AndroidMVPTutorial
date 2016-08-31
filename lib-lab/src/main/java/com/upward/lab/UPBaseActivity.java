package com.upward.lab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UPBaseActivity extends AppCompatActivity {

    private static final String TAG     = UPBaseActivity.class.getSimpleName();
    private static final boolean DEBUG  = true;

    protected Context context;
    private ViewGroup mLoadingView;
    private View mErrorView;
    private TextView mTitleView;
    private UPOnReloadListener mOnReloadListener;
    private boolean mDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mTitleView = (TextView) findViewById(getResources().getIdentifier("title", "id", getPackageName()));
        if (mTitleView != null) {
            mTitleView.setText(getTitle());
        }
        ImageView backImageView = (ImageView) findViewById(getResources().getIdentifier("back", "id", getPackageName()));
        if (backImageView != null) {
            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        mLoadingView = (ViewGroup) findViewById(getResources().getIdentifier("loading_layout", "id", getPackageName()));
        mErrorView = findViewById(getResources().getIdentifier("error_loading", "id", getPackageName()));
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mDestroyed = true;
        super.onDestroy();
    }

    protected void showLoadingView() {
        showLoadingView(getString(getResources().getIdentifier("loading_common_text", "string", getPackageName())));
    }

    /**
     * 显示Loading视图
     */
    protected void showLoadingView(String text) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            TextView loadingTextView = (TextView) mLoadingView.findViewById(getResources().getIdentifier("loading_text", "id", getPackageName()));
            loadingTextView.setText(text);
        }
        hideErrorView();
    }

    /**
     * 隐藏Loading视图
     */
    protected void hideLoadingView() {
        if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
    }

    /**
     * 显示加载异常视图
     */
    protected void showErrorView() {
        hideLoadingView();
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnReloadListener != null) mOnReloadListener.onClickReload();
                }

            });
        }
    }

    /**
     * 隐藏加载异常视图
     */
    protected void hideErrorView() {
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
    }

    /**
     * 加载异常点击重新加载回调
     */
    public void setOnReloadListener(UPOnReloadListener l) {
        this.mOnReloadListener = l;
    }

    public boolean isDestroyed() {
        return mDestroyed;
    }

    public void setTitle(String title) {
        if (mTitleView != null) mTitleView.setText(title);
    }

}