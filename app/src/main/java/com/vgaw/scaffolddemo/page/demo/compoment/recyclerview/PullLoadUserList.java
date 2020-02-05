package com.vgaw.scaffolddemo.page.demo.compoment.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.vgaw.scaffold.img.ImgLoader;
import com.vgaw.scaffold.util.Util;
import com.vgaw.scaffold.view.rcv.BaseRcv;
import com.vgaw.scaffold.view.rcv.EasyRcvAdapter;
import com.vgaw.scaffold.view.rcv.EasyRcvHolder;
import com.vgaw.scaffold.view.rcv.decoration.BaseItemDecoration;
import com.vgaw.scaffold.view.rcv.decoration.GridDividerItemDecoration;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.data.MockUtil;
import com.vgaw.scaffolddemo.data.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PullLoadUserList extends BaseRcv<UserInfo> {
    public PullLoadUserList(Context context) {
        super(context);
    }

    public PullLoadUserList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullLoadUserList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(layoutManager);

        BaseItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(getContext(), 8);
        gridDividerItemDecoration.setHeaderCount(1);
        addItemDecoration(gridDividerItemDecoration);

        mAdapter = new EasyRcvAdapter<UserInfo>(getContext(), mDataList) {
            @Override
            protected EasyRcvHolder getHolder(int type) {
                return new EasyRcvHolder(LayoutInflater.from(getContext()).inflate(R.layout.normal_recyclerview_item, null)) {
                    private CircleImageView mNormalRecyclerviewItemAvatar;
                    private TextView mNormalRecyclerviewItemName;

                    @Override
                    protected View onCreateView() {
                        mNormalRecyclerviewItemAvatar = mView.findViewById(R.id.normal_recyclerview_item_avatar);
                        mNormalRecyclerviewItemName = mView.findViewById(R.id.normal_recyclerview_item_name);
                        return mView;
                    }

                    @Override
                    protected void refreshView(int position, Object item) {
                        UserInfo userInfo = (UserInfo) item;
                        ImgLoader.load(mContext, userInfo.getAvatar(), mNormalRecyclerviewItemAvatar);
                        mNormalRecyclerviewItemName.setText(Util.nullToEmpty(userInfo.getName()));
                    }
                };
            }
        };
        setAdapter(mAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLoadingListener(new LoadingListener() {
            @Override
            public void onRefresh() {
                resetPage();

                fetchData(true);
            }

            @Override
            public void onLoadMore() {
                fetchData(false);
            }
        });

        fetchData(true);
    }

    private void fetchData(final boolean refresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<UserInfo> mockList = new ArrayList<>();
                mockList.addAll(MockUtil.buildUserList());
                mockList.addAll(MockUtil.buildUserList());

                onOnePageAchieved();

                if (refresh) {
                    updateData(mockList);
                } else {
                    addData(mockList);
                }

                refreshComplete();
            }
        }, 500);
    }
}
