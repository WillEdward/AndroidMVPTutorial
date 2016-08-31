package com.upward.lab.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.upward.lab.R;
import com.upward.lab.util.UPUtils;

/**
 * Func: 自动加载更多列表.
 * Date: 2015-08-21 14:58
 * Author: Will Tang (djtang@iflytek.com)
 * Version: 5.0
 */
public class UPAutoLoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    // 是否正在加载更多
    private boolean mIsLoadingMore = false;
    // 数据是否全部加载完毕
    private boolean mIsLoadAll = false;
    // 加载更多ProgressBar
    private ProgressBar mLoadingMoreProgressBar;
    // 没有更多数据TextView
    private TextView mLoadingMoreNoMoreDateTextView;
    // 加载更多回调接口
    private OnLoadingMoreListener mOnLoadingMoreListener;
    // 列表数据为空
    private ImageView mEmptyImageView;
    private LinearLayout mLoadingMoreFooter;

    public UPAutoLoadMoreListView(Context context) {
        this(context, null);
    }

    public UPAutoLoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UPAutoLoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
        initLoadingMoreFooterView();
        initEmptyView();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        ViewGroup parent = (ViewGroup) getParent();
        super.setAdapter(adapter);
        parent.removeView(mEmptyImageView);
        parent.addView(mEmptyImageView);
        setEmptyView(mEmptyImageView);
        parent.invalidate();
    }

    // 初始化加载更多Footer View
    private void initLoadingMoreFooterView() {
        mLoadingMoreFooter = new LinearLayout(getContext());
        mLoadingMoreFooter.setGravity(Gravity.CENTER);
        mLoadingMoreFooter.setOrientation(LinearLayout.VERTICAL);
        int padding = UPUtils.dp2px(10);
        mLoadingMoreFooter.setPadding(padding, padding, padding, padding);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, UPUtils.dp2px(48));
        mLoadingMoreFooter.setLayoutParams(params);

        mLoadingMoreProgressBar = new ProgressBar(getContext());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(UPUtils.dp2px(36), UPUtils.dp2px(36));
        mLoadingMoreProgressBar.setLayoutParams(params2);
        mLoadingMoreProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.up_progress_indeterminate_drawable));
        mLoadingMoreFooter.addView(mLoadingMoreProgressBar);

        mLoadingMoreNoMoreDateTextView = new TextView(getContext());
        mLoadingMoreNoMoreDateTextView.setBackgroundColor(Color.TRANSPARENT);
        mLoadingMoreNoMoreDateTextView.setGravity(Gravity.CENTER);
        mLoadingMoreNoMoreDateTextView.setText(R.string.no_more_data);
        mLoadingMoreNoMoreDateTextView.setTextSize(16);
        mLoadingMoreNoMoreDateTextView.setTextColor(getResources().getColor(R.color.theme_normal));
        mLoadingMoreNoMoreDateTextView.setVisibility(View.GONE);
        mLoadingMoreFooter.addView(mLoadingMoreNoMoreDateTextView);

        addFooterView(mLoadingMoreFooter);
    }

    // 列表为空视图
    private void initEmptyView() {
        mEmptyImageView = new ImageView(getContext());
        // mEmptyImageView.setImageResource(R.drawable.image_empty);
        mEmptyImageView.setVisibility(View.GONE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean isNeedLoadingMore = (firstVisibleItem + visibleItemCount == totalItemCount
                && totalItemCount > 0 && visibleItemCount < totalItemCount) && !mIsLoadingMore & !mIsLoadAll;
        if (isNeedLoadingMore) setLoadingMore(isNeedLoadingMore);
    }

    // 设置加载是否是加载更多
    public void setLoadingMore(boolean isLoadingMore) {
        this.mIsLoadingMore = isLoadingMore;
        if (isLoadingMore) {
            if (mOnLoadingMoreListener != null) mOnLoadingMoreListener.onLoading();
            mLoadingMoreProgressBar.setVisibility(View.VISIBLE);
        } else {
            mLoadingMoreProgressBar.setVisibility(View.GONE);
        }
    }

    public void setOnLoadingMoreListener(OnLoadingMoreListener listener) {
        this.mOnLoadingMoreListener = listener;
    }

    public void setIsLoadAll(boolean isLoadAll) {
        this.mIsLoadAll = isLoadAll;
        setLoadingMore(false);
        mLoadingMoreNoMoreDateTextView.setVisibility(View.VISIBLE);
        removeFooterView(mLoadingMoreFooter);
    }

    public interface OnLoadingMoreListener {

        void onLoading();

    }

}
