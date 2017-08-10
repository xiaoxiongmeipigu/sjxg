package com.zjhj.sjxg.adapter.purcase;

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
import com.zjhj.sjxg.interfaces.AdapterSelListener;
import com.zjhj.sjxg.view.PurcaseSheetLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/29.
 */
public class PurcaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    List<IndexData> mList = new ArrayList<>();

    AdapterSelListener listener;

    public void setOnAdapterSelListener(AdapterSelListener listener) {
        this.listener = listener;
    }

    private boolean isDel = false;

    public void setDel(boolean del) {
        isDel = del;
    }

    public PurcaseAdapter(Context context, List<IndexData> list) {
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
                return new HeadViewHolder(inflater.inflate(R.layout.item_purcase_head, parent, false));
            case 2:
                return new ItemViewHolder(inflater.inflate(R.layout.item_purcase_item, parent, false));
            case 3:
                return new DividerViewHolder(inflater.inflate(R.layout.item_purcase_divider, parent, false));
            case 4:
                return new ItemThreeViewHolder(inflater.inflate(R.layout.item_purcase_item_three, parent, false));
            default:
                return new HeadViewHolder(inflater.inflate(R.layout.item_purcase_head, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            setHead((HeadViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }else if(holder instanceof ItemThreeViewHolder){
            setItemThree((ItemThreeViewHolder) holder, position);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_sel)
        ImageView rootSel;
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
        @Bind(R.id.item_sel)
        ImageView itemSel;
        @Bind(R.id.purcaseSheetLayout)
        PurcaseSheetLayout purcaseSheetLayout;
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
        @Bind(R.id.item_sel)
        ImageView itemSel;
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

        boolean isAll = true;
        for (MapiCartItemResult item : ware.getSpec()) {
            if (!item.isSel()) {
                isAll = false;
                break;
            }
        }
        ware.setSel(isAll);
        if (null != listener) {
            listener.isAll();
        }
        if (ware.isSel()) {
            holder.rootSel.setImageResource(R.mipmap.sel_right);
        } else {
            holder.rootSel.setImageResource(R.mipmap.sel);
        }
        holder.rootSel.setTag(position);
        holder.rootSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                if (null != listener) {
                    listener.notifyParentStatus(position);
                }

            }
        });

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
        if (result.isSel())
            holder.itemSel.setImageResource(R.mipmap.sel_right);
        else
            holder.itemSel.setImageResource(R.mipmap.sel);

        holder.itemSel.setTag(position);
        holder.itemSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (int) view.getTag();
                if (null != listener) {
                    listener.notifyChildStatus(position);
                }

            }
        });
        String count = TextUtils.isEmpty(result.getNum())?"1":result.getNum();
        holder.purcaseSheetLayout.setNum(Integer.parseInt(count));
        holder.purcaseSheetLayout.setTag(position);
        holder.purcaseSheetLayout.setNumListener(new PurcaseSheetLayout.NumInterface() {
            @Override
            public void modify(View view,int num,String price) {
                int position = (int) view.getTag();
                if(null!=listener){
                    listener.notifyChildNum(position,num);
                }

            }
        });

        holder.purcaseSheetLayout.setCountEdit(false);
        if (isDel) {
            holder.purcaseSheetLayout.setCanDo(false);
        } else {
            holder.purcaseSheetLayout.setCanDo(true);
        }

        String color = TextUtils.isEmpty(result.getColor_name())?"":result.getColor_name();
        String size = TextUtils.isEmpty(result.getSize_name())?"":result.getSize_name();

        holder.attr.setText("颜色："+color+";  尺码："+size);

        holder.price.setText(TextUtils.isEmpty(result.getFare_price())?"0":result.getFare_price());


    }

    private void setItemThree(ItemThreeViewHolder holder, int position) {

        MapiCartItemResult result = (MapiCartItemResult) mList.get(position).getData();
        if (result.isSel())
            holder.itemSel.setImageResource(R.mipmap.sel_right);
        else
            holder.itemSel.setImageResource(R.mipmap.sel);

        holder.itemSel.setTag(position);
        holder.itemSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (int) view.getTag();
                if (null != listener) {
                    listener.notifyChildStatus(position);
                }

            }
        });
        String count = TextUtils.isEmpty(result.getCount())?"1":result.getCount();
       holder.num.setText("X "+count);

        holder.price.setText(TextUtils.isEmpty(result.getPrice())?"0":result.getPrice());


    }


}
