package com.vgaw.scaffolddemo.page.demo.compoment.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.vgaw.scaffold.img.ImgLoader;
import com.vgaw.scaffold.util.Util;
import com.vgaw.scaffold.view.rcv.BaseNormalRcv;
import com.vgaw.scaffold.view.rcv.EasyRcvAdapter;
import com.vgaw.scaffold.view.rcv.EasyRcvHolder;
import com.vgaw.scaffold.view.rcv.decoration.BaseItemDecoration;
import com.vgaw.scaffold.view.rcv.decoration.GridDividerItemDecoration;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.data.MockUtil;
import com.vgaw.scaffolddemo.data.user.UserInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class NormalUserList extends BaseNormalRcv<UserInfo> {
    public NormalUserList(Context context) {
        super(context);
    }

    public NormalUserList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalUserList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(layoutManager);

        BaseItemDecoration gridDividerItemDecoration = new GridDividerItemDecoration(getContext(), 8);
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
        MockUtil.buildUserList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfos -> addData(userInfos));
    }
}
