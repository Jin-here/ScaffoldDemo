package com.vgaw.scaffold.databinding;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author caojin
 * @date 2018/6/5
 */
public abstract class DataBindingAdapter<T> extends BaseAdapter {
    protected Context context;
    private List<T> dataList;

    public DataBindingAdapter(Context context, List<T> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    public void updateDataList(List<T> dataList) {
        this.dataList = dataList;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
