package com.vgaw.scaffolddemo.page.demo.compoment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.vgaw.scaffold.page.BaseFrag;
import com.vgaw.scaffold.page.MockFrag;
import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.phone.DensityUtil;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.bottomnav.BottomNavFrag;
import com.vgaw.scaffolddemo.page.demo.compoment.other.OtherFrag;
import com.vgaw.scaffolddemo.page.demo.compoment.recyclerview.NormalRecyclerViewFrag;
import com.vgaw.scaffolddemo.page.demo.compoment.recyclerview.PullLoadRecyclerViewFrag;
import com.vgaw.scaffolddemo.page.demo.compoment.tab.TabFragment;
import com.vgaw.scaffolddemo.page.demo.compoment.viewpager.ViewPagerFrag;

import java.util.ArrayList;
import java.util.List;

public class CompomentFrag extends MockFrag {
    private LinearLayout mCompomentContainer;

    private List<ItemBean> mItemList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavFrag bottomNavFragment = BottomNavFrag.newInstance();
        mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "bottom nav"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用", bottomNavFragment));

        /*mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "listview"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "上拉下拉"));*/

        NormalRecyclerViewFrag normalRecyclerViewFrag = new NormalRecyclerViewFrag();
        PullLoadRecyclerViewFrag pullLoadRecyclerViewFrag = new PullLoadRecyclerViewFrag();
        mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "recyclerview"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用", normalRecyclerViewFrag));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "上拉下拉", pullLoadRecyclerViewFrag));

        TabFragment tabFragment = new TabFragment();
        mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "tab"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用", tabFragment));

        ViewPagerFrag viewPagerFrag = new ViewPagerFrag();
        mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "viewpager"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用", viewPagerFrag));

        OtherFrag otherFrag = new OtherFrag();
        mItemList.add(new ItemBean(ItemBean.TYPE_LABEL, "other"));
        mItemList.add(new ItemBean(ItemBean.TYPE_CONTENT, "基本使用", otherFrag));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compoment_frag, container, false);
        mCompomentContainer = view.findViewById(R.id.compoment_container);
        initView();
        return view;
    }

    private void initView() {
        for (ItemBean item : mItemList) {
            if (item.type == ItemBean.TYPE_CONTENT) {
                addContentView(item.name, item.frag);
            } else {
                addLabelView(item.name);
            }
        }
    }

    private void addLabelView(String name) {
        TextView tv = new TextView(getContext());
        tv.setPadding(DensityUtil.dp2px(getContext(), 10), 0, 0, 0);
        tv.setBackgroundResource(android.R.color.darker_gray);
        tv.setText(name);
        tv.setTextSize(16);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        mCompomentContainer.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dp2px(getContext(), 36)));
    }

    private void addContentView(String name, final BaseFrag frag) {
        TextView tv = new TextView(getContext());
        tv.setPadding(DensityUtil.dp2px(getContext(), 10), 0, 0, 0);
        tv.setText(name);
        tv.setTextSize(16);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag != null) {
                    FragmentManager fragmentManager = ContextManager.getInstance().getFragmentManager();
                    if (fragmentManager != null) {
                        DialogUtil.showDialog(frag, fragmentManager);
                    }
                }
            }
        });
        mCompomentContainer.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dp2px(getContext(), 64)));
    }

    private static class ItemBean {
        private static final int TYPE_LABEL = 0;
        private static final int TYPE_CONTENT = 1;

        private int type = -1;
        private String name;
        private BaseFrag frag;

        public ItemBean(int type, String name) {
            this(type, name, null);
        }

        public ItemBean(int type, String name, BaseFrag frag) {
            this.type = type;
            this.name = name;
            this.frag = frag;
        }
    }
}
