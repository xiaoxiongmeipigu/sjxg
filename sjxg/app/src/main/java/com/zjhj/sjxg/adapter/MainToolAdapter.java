package com.zjhj.sjxg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.JGJDataSource;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/5.
 */
public class MainToolAdapter extends RecyclerView.Adapter<MainToolAdapter.ViewHolder> {
    LayoutInflater inflater;

    private List<MapiResourceResult> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public MainToolAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_home_tool, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int resId = R.mipmap.main_one;
        switch (mList.get(position).getId()){
            case JGJDataSource.TYPE_ONE:
                resId = R.mipmap.main_one;
                break;
            case JGJDataSource.TYPE_TWO:
                resId = R.mipmap.main_two;
                break;
            case JGJDataSource.TYPE_THREE:
                resId = R.mipmap.main_three;
                break;
            case JGJDataSource.TYPE_FOUR:
                resId = R.mipmap.main_four;
                break;
            case JGJDataSource.TYPE_FIVE:
                resId = R.mipmap.main_five;
                break;
            case JGJDataSource.TYPE_SIX:
                resId = R.mipmap.main_six;
                break;
            case JGJDataSource.TYPE_SEVEN:
                resId = R.mipmap.main_seven;
                break;
            case JGJDataSource.TYPE_EIGHT:
                resId = R.mipmap.main_eight;
                break;

        }
        holder.image.setImageResource(resId);
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view,(Integer)view.getTag());
            }
        });

        holder.title.setText(mList.get(position).getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout root_view;
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
