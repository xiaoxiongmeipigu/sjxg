package com.zjhj.sjxg.adapter.item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.view.ItemSizeColorLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/24.
 */
public class SizeColorAdapter extends RecyclerView.Adapter<SizeColorAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<MapiResourceResult> mList;
    private RecyOnItemClickListener onItemClickListener;
    private Context mContext;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SizeColorAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_size_color, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiResourceResult resourceResult = mList.get(position);
        holder.titleTv.setText(TextUtils.isEmpty(resourceResult.getName()) ? "" : resourceResult.getName());

        holder.itemSizeColorLayout.load(mList.get(position).getList());
        holder.itemSizeColorLayout.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view,position);
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.itemSizeColorLayout)
        ItemSizeColorLayout itemSizeColorLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
