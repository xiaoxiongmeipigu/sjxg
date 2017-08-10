package com.zjhj.sjxg.adapter.menu;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.ItemGridAdapter;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.interfaces.RecyOnMenuClickListener;
import com.zjhj.sjxg.widget.DividerListItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/23.
 */
public class MenuParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM = 0;
    private final static int DIVIDER = 1;

    LayoutInflater inflater;

    private Context mContext;
    private List<IndexData> mList;

    private RecyOnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(RecyOnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public MenuParentAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.item_menu_parent, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_one, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_one, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setItem(ItemViewHolder holder,int position) {

        final List<MapiResourceResult> list = (List<MapiResourceResult>) mList.get(position).getData();

        if(list.get(0).getType().equals("1"))
            holder.titleTv.setText("季节");
        else
            holder.titleTv.setText("材质");

        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        holder.recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        holder.recyclerView.setHasFixedSize(true);
//        holder.recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.VERTICAL, DPUtil.dip2px(5), mContext.getResources().getColor(R.color.shop_white)));
        MenuGridAdapter mAdapter = new MenuGridAdapter(mContext, list);
        holder.recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String type = list.get(position).getType();
                if(null!=onMenuClickListener)
                    onMenuClickListener.onMenuClick(view,type,position);
            }
        });

    }


}
