package com.vgaw.scaffold.view.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;

// TODO: 2019-05-31 加载出错后，点击加载更多
public class LoadMoreListView extends ListView {
    private static final int STATUS_FINISHED = 1;
    private static final int STATUS_LOADING = 2;
    private static final int STATUS_NO_MORE = 3;
    private static final int STATUS_ERROR = 4;

    private View mFooterView;
    private TextView mLoadmoreHint;

    private int mStatus;

    public OnLoadMoreListener mListener;

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // 前提：必有footer
            // #1 visibleItemCount == totalItemCount，此时不刷新
            // #2 否则如果firstVisibleItem + visibleItemCount > totalItemCount - 1，此时刷新
            if (visibleItemCount != totalItemCount && firstVisibleItem + visibleItemCount > totalItemCount - 1) {
                if (mStatus == STATUS_FINISHED){
                    changeFooterHint(STATUS_LOADING);

                    callLoadMore();
                }
            }
        }
    };

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void notifyFinished(){
        changeFooterHint(STATUS_FINISHED);
    }

    public void notifyNoMore(){
        changeFooterHint(STATUS_NO_MORE);
    }

    public void notifyError(){
        changeFooterHint(STATUS_ERROR);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    protected boolean enableLoadmore() {
        return true;
    }

    private void init() {
        if (enableLoadmore()) {
            initFooterView(getContext());
            setFooterDividersEnabled(false);
            addFooterView(mFooterView, null, false);
            setOnScrollListener(mScrollListener);
        }
    }

    private void initFooterView(Context context) {
        mFooterView = LayoutInflater.from(context).inflate(R.layout.layout_loadmore, null);
        mLoadmoreHint = mFooterView.findViewById(R.id.loadmore_hint);
        mFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus == STATUS_ERROR) {
                    // 请求失败后，点击重试
                    callLoadMore();
                }
            }
        });

        changeFooterHint(STATUS_FINISHED);
    }

    /**
     * 更改状态提示
     */
    private void changeFooterHint(int status) {
        this.mStatus = status;
        if (enableLoadmore()) {
            switch (status) {
                case STATUS_FINISHED:
                    mLoadmoreHint.setText("");
                    break;
                case STATUS_LOADING:
                    mLoadmoreHint.setText(R.string.base_loading);
                    break;
                case STATUS_NO_MORE:
                    mLoadmoreHint.setText(R.string.base_no_more);
                    break;
                case STATUS_ERROR:
                    mLoadmoreHint.setText(R.string.base_load_error);
                    break;
            }
        }
    }

    private void callLoadMore() {
        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
