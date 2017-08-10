package com.zjhj.sjxg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.view.HomeHotLayout;
import com.zjhj.sjxg.view.HomeSliderLayout;
import com.zjhj.sjxg.view.HomeToolLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/20.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final static int SLIDER_IMAGE = 0;
    private final static int TOOL = 1;
    private final static int ITEM = 2;
    private final static int DIVIDER = 3;

    LayoutInflater inflater;

    private List<IndexData> mList;

    public MainAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SLIDER_IMAGE:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_home_slider, parent, false));
            case TOOL:
                return new ToolViewHolder(inflater.inflate(R.layout.lay_home_tool, parent, false));
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.lay_home_item, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_ten, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_ten, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderViewHolder) {
            ((SliderViewHolder)holder).homeSliderLayout.setSlider(true);
            ((SliderViewHolder)holder).homeSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }else if(holder instanceof ItemViewHolder){
            ((ItemViewHolder)holder).homeHotLayout.load((List<MapiItemResult>) mList.get(position).getData());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "SCROLL":
                return SLIDER_IMAGE;
            case "TOOL":
                return TOOL;
            case "ITEM":
                return ITEM;
            case "DIVIDER":
                return DIVIDER;
            default:
                return DIVIDER;
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeSliderLayout)
        HomeSliderLayout homeSliderLayout;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ToolViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeToolLayout)
        HomeToolLayout homeToolLayout;
        public ToolViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.homeHotLayout)
        HomeHotLayout homeHotLayout;
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



}
