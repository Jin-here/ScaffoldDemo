package com.vgaw.scaffold.view.rcv;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class EasyRcvAdapter<T> extends RecyclerView.Adapter<EasyRcvHolder> {
    protected Context mContext;
    private List<T> mDataList;

    public EasyRcvAdapter(Context context, List<T> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @NonNull
    @Override
    public EasyRcvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EasyRcvHolder<T> holder = getHolder(parent, viewType);
        holder.onCreateView();
        holder.onGetExtraData(getExtraData());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EasyRcvHolder holder, int position) {
        holder.refreshView(position, getItem(position));
    }

    protected abstract EasyRcvHolder<T> getHolder(@NonNull ViewGroup parent, int type);

    protected T getItem(int position) {
        if (mDataList != null) {
            return mDataList.get(position);
        }
        return null;
    }

    /**
     * 设置额外数据，例如选中状态；
     * @return
     */
    protected List<Object> getExtraData() {return null;}
}