package com.upward.lab;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Func: Fragment基类.
 * Date: 2016-03-15 13:27
 * Author: Will Tang (djtang@iflytek.com)
 * Version: 1.11.6
 */
public class UPBaseFragment extends Fragment {

    private static final boolean DEBUG = false;
    private static final String TAG = "BaseFragment";

    protected Activity activity;
    private ViewGroup loadingView;
    private View loadingErrorView;
    private UPOnReloadListener onReloadListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = (ViewGroup) view.findViewById(getResources().getIdentifier("loading_layout", "id", activity.getPackageName()));
        loadingErrorView = view.findViewById(getResources().getIdentifier("error_loading", "id", activity.getPackageName()));
    }

    protected void showLoadingView() {
        showLoadingView(getString(getResources().getIdentifier("loading_common_text", "string", activity.getPackageName())));
    }

    /**
     * 显示Loading视图
     */
    protected void showLoadingView(String text) {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            TextView loadingTextView = (TextView) loadingView.findViewById(getResources().getIdentifier("loading_text", "id", activity.getPackageName()));
            loadingTextView.setText(text);
        }
        hideErrorView();
    }

    /**
     * 隐藏Loading视图
     */
    protected void hideLoadingView() {
        if (loadingView != null) loadingView.setVisibility(View.GONE);
    }

    /**
     * 显示加载异常视图
     */
    protected void showErrorView() {
        hideLoadingView();
        if (loadingErrorView != null) {
            loadingErrorView.setVisibility(View.VISIBLE);
            loadingErrorView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onReloadListener != null) onReloadListener.onClickReload();
                }

            });
        }
    }

    /**
     * 隐藏加载异常视图
     */
    protected void hideErrorView() {
        if (loadingErrorView != null) loadingErrorView.setVisibility(View.GONE);
    }

    /**
     * 加载异常点击重新加载回调
     */
    public void setOnReloadListener(UPOnReloadListener l) {
        this.onReloadListener = l;
    }

}