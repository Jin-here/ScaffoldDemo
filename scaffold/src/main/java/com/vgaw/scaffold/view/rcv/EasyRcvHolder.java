package com.vgaw.scaffold.view.rcv;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class EasyRcvHolder<T> extends RecyclerView.ViewHolder {
    protected Context mContext;
    protected View mView;

    public EasyRcvHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mView = itemView;
    }

    protected abstract View onCreateView();

    protected abstract void  refreshView(int position, T item);

    protected void onGetExtraData(List<Object> extraDataList) {}
}
