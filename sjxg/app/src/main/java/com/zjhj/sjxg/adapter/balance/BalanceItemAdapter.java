package com.zjhj.sjxg.adapter.balance;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/4/13.
 */
public class BalanceItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    List<IndexData> mList = new ArrayList<>();

    public BalanceItemAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        String type = mList.get(position).getType();
        if (type.equals("item")) {
            return 2;
        } else if (type.equals("head")) {
            return 1;
        } else if (type.equals("divider")) {
            return 3;
        }else if(type.equals("item_three")){
            return 4;
        }
        return 3;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new HeadViewHolder(inflater.inflate(R.layout.item_balance_head, parent, false));
            case 2:
                return new ItemViewHolder(inflater.inflate(R.layout.item_balance_item, parent, false));
            case 3:
                return new DividerViewHolder(inflater.inflate(R.layout.item_purcase_divider, parent, false));
            case 4:
                return new ItemThreeViewHolder(inflater.inflate(R.layout.item_balance_item_three, parent, false));
            default:
                return new HeadViewHolder(inflater.inflate(R.layout.item_balance_head, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            setHead((HeadViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }else if (holder instanceof ItemThreeViewHolder) {
            setItemThree((ItemThreeViewHolder) holder, position);
        }

    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.attr)
        TextView attr;
        @Bind(R.id.price)
        TextView price;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemThreeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.price)
        TextView price;

        public ItemThreeViewHolder(View itemView) {
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

    private void setHead(HeadViewHolder holder, int position) {

        MapiCarResult ware = (MapiCarResult) mList.get(position).getData();

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(ware.getCover_pic())?"":ware.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(78), DPUtil.dip2px(78)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.title.setText(TextUtils.isEmpty(ware.getTrade()) ? "" : ware.getTrade());

    }

    private void setItem(ItemViewHolder holder, int position) {

        MapiCartItemResult result = (MapiCartItemResult) mList.get(position).getData();

        String count = TextUtils.isEmpty(result.getNum())?"1":result.getNum();
        holder.num.setText("X "+count);

        String color = TextUtils.isEmpty(result.getColor_name())?"":result.getColor_name();
        String size = TextUtils.isEmpty(result.getSize_name())?"":result.getSize_name();

        holder.attr.setText("颜色："+color+";  尺码："+size);

        holder.price.setText(TextUtils.isEmpty(result.getFare_price())?"0":result.getFare_price());


    }

    private void setItemThree(ItemThreeViewHolder holder, int position) {

        MapiCartItemResult result = (MapiCartItemResult) mList.get(position).getData();
        String count = TextUtils.isEmpty(result.getNum())?"1":result.getNum();
        holder.num.setText("X "+count);

        holder.price.setText(TextUtils.isEmpty(result.getFare_price())?"0":result.getFare_price());


    }

}
