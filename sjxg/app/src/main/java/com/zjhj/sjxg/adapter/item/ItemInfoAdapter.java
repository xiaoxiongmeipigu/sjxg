package com.zjhj.sjxg.adapter.item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.view.ItemSliderLayout;
import com.zjhj.sjxg.view.ItemToolLayout;
import com.zjhj.sjxg.view.ItemItemLayout;
import com.zjhj.sjxg.view.LimitToolLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/24.
 */
public class ItemInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater inflater;
    private List<IndexData> mList;
    private RecyOnItemClickListener onItemClickListener;

    private final static int SLIDER_IMAGE = 0;
    private final static int TOOL = 1;
    private final static int ITEM = 2;
    private final static int LIMIT_TOOL = 3;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ItemInfoAdapter(Context context, List<IndexData> list) {
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
                return new SliderViewHolder(inflater.inflate(R.layout.lay_item_slider, parent, false));
            case TOOL:
                return new ToolViewHolder(inflater.inflate(R.layout.lay_item_tool, parent, false));
            case ITEM:
                return new ItemViewHolder(inflater.inflate(R.layout.lay_item_item, parent, false));
            case LIMIT_TOOL:
                return new LimitToolViewHolder(inflater.inflate(R.layout.lay_limit_tool, parent, false));
            default:
                return new SliderViewHolder(inflater.inflate(R.layout.lay_item_slider, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SliderViewHolder) {
            ((SliderViewHolder)holder).itemSliderLayout.load((List<MapiResourceResult>) mList.get(position).getData());
        }else if(holder instanceof ItemViewHolder){
            ((ItemViewHolder)holder).itemItemLayout.load((List<MapiItemResult>) mList.get(position).getData());
        }else if(holder instanceof ToolViewHolder){
            ((ToolViewHolder)holder).itemToolLayout.load((MapiItemResult) mList.get(position).getData());
            ((ToolViewHolder)holder).itemToolLayout.setSizeOpenListener(new ItemToolLayout.SizeOpenListener() {
                @Override
                public void open() {
                    DebugLog.i("PromptInfoAdapter=>selSizeLL");
                    if(null!=sizeOpenListener)
                        sizeOpenListener.open();
                }
            });
        }else if(holder instanceof LimitToolViewHolder){
            ((LimitToolViewHolder)holder).limitToolLayout.load((MapiItemResult) mList.get(position).getData());
            ((LimitToolViewHolder)holder).limitToolLayout.setNumListener(new LimitToolLayout.NumListener() {
                @Override
                public void num(int num) {
                    if(null!=numListener)
                        numListener.num(num);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "SCROLL":
                return SLIDER_IMAGE;
            case "TOOL":
                return TOOL;
            case "LIMIT_TOOL":
                return LIMIT_TOOL;
            case "ITEM":
                return ITEM;
            default:
                return SLIDER_IMAGE;
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemSliderLayout)
        ItemSliderLayout itemSliderLayout;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ToolViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemToolLayout)
        ItemToolLayout itemToolLayout;
        public ToolViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemItemLayout)
        ItemItemLayout itemItemLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LimitToolViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.limitToolLayout)
        LimitToolLayout limitToolLayout;
        public LimitToolViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public SizeOpenListener sizeOpenListener;

    public interface SizeOpenListener{
        void open();
    }

    public void setSizeOpenListener(SizeOpenListener sizeOpenListener){
        this.sizeOpenListener = sizeOpenListener;
    }

    public NumListener numListener;

    public interface NumListener{
        void num(int num);
    }

    public void setNumListener(NumListener numListener){
        this.numListener = numListener;
    }

}
