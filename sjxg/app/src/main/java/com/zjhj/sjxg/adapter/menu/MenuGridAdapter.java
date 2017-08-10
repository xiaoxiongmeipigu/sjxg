package com.zjhj.sjxg.adapter.menu;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hitomi.cslibrary.CrazyShadow;
import com.hitomi.cslibrary.base.CrazyShadowDirection;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/23.
 */
public class MenuGridAdapter extends RecyclerView.Adapter<MenuGridAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<MapiResourceResult> mList;
    private RecyOnItemClickListener onItemClickListener;
    private Context mContext;
    private int oldPos = -1;

    public List<MapiResourceResult> getmList() {
        return mList;
    }

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MenuGridAdapter(Context context, List<MapiResourceResult> list) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_menu_child,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiResourceResult resourceResult = mList.get(position);
        holder.root_view.setText(TextUtils.isEmpty(resourceResult.getName())?"":resourceResult.getName());
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();

                if(oldPos!=pos){
                    if(oldPos>=0)
                        mList.get(oldPos).setSel(false);
                    mList.get(pos).setSel(true);
                }else{
                    mList.get(pos).setSel(!mList.get(pos).isSel());
                }
                oldPos = pos;
                notifyDataSetChanged();
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        if(mList.get(position).isSel()){
            holder.root_view.setChecked(true);
        }else{
            holder.root_view.setChecked(false);
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.root_view)
        CheckBox root_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
