package com.zjhj.sjxg.adapter.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiShopResult;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/27.
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiShopResult> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ShopListAdapter(Context context, List<MapiShopResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_shop_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiShopResult shopResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view,pos);
            }
        });

        holder.nameTv.setText(TextUtils.isEmpty(shopResult.getName())?"":shopResult.getName());
        holder.phoneTv.setText("联系电话："+(TextUtils.isEmpty(shopResult.getMobile())?"":shopResult.getMobile()));
        holder.addressTv.setText(TextUtils.isEmpty(shopResult.getAddress())?"":shopResult.getAddress());

        String distanceStr = TextUtils.isEmpty(shopResult.getDistance())?"":shopResult.getDistance();

        holder.distanceTv.setText(distanceStr+"km");

    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.phone_tv)
        TextView phoneTv;
        @Bind(R.id.address_tv)
        TextView addressTv;
        @Bind(R.id.distance_tv)
        TextView distanceTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
