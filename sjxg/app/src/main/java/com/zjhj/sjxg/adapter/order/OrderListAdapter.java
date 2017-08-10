package com.zjhj.sjxg.adapter.order;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.OrderDeelListener;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/27.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    List<IndexData> mList = new ArrayList<>();

    private OrderDeelListener orderDeelListener;

    public void setOrderDeelListener(OrderDeelListener orderDeelListener) {
        this.orderDeelListener = orderDeelListener;
    }

    private RecyOnItemClickListener itemClickListener;

    public void setRecyOnItemClickListener(RecyOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderListAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = mList.get(position).getType();
        if (type.equals("divider")) {
            return 1;
        } else if (type.equals("head")) {
            return 2;
        } else if (type.equals("banlance_head")) {
            return 3;
        } else if (type.equals("item")) {
            return 4;
        } else if (type.equals("bottom")) {
            return 5;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_ten, parent, false));
            case 2:
                return new ItemHeadHolder(inflater.inflate(R.layout.item_order_head, parent, false));
            case 3:
                return new BanlanceHeadViewHolder(inflater.inflate(R.layout.item_balance_head, parent, false));
            case 4:
                return new BanlanceItemViewHolder(inflater.inflate(R.layout.item_balance_item, parent, false));
            case 5:
                return new ItemBottomHolder(inflater.inflate(R.layout.item_order_bottom, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_ten, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BanlanceItemViewHolder) {
            setBanlanceItem((BanlanceItemViewHolder) holder, position);
        } else if (holder instanceof ItemHeadHolder) {
            setHead((ItemHeadHolder) holder, position);
        } else if (holder instanceof ItemBottomHolder) {
            setBottom((ItemBottomHolder) holder, position);
        } else if (holder instanceof BanlanceHeadViewHolder) {
            setBanlanceHead((BanlanceHeadViewHolder) holder, position);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemHeadHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.status_tv)
        TextView statusTv;

        public ItemHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BanlanceItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.attr)
        TextView attr;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public BanlanceItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemBottomHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.goods_total)
        TextView goodsTotal;
        @Bind(R.id.total_amount)
        TextView totalAmount;
        @Bind(R.id.express_fee)
        TextView expressFee;
        @Bind(R.id.cancel)
        TextView cancel;
        @Bind(R.id.comfirm)
        TextView comfirm;
        public ItemBottomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BanlanceHeadViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.root_view)
        LinearLayout rootView;

        public BanlanceHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void setBanlanceItem(BanlanceItemViewHolder holder, int position) {
        MapiCartItemResult result = (MapiCartItemResult) mList.get(position).getData();

        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (null != itemClickListener)
                    itemClickListener.onItemClick(view, pos);
            }
        });

        String count = TextUtils.isEmpty(result.getCount()) ? "1" : result.getCount();
        holder.num.setText("X " + count);

        String color = TextUtils.isEmpty(result.getColor()) ? "" : result.getColor();
        String size = TextUtils.isEmpty(result.getSize()) ? "" : result.getSize();

        holder.attr.setText("颜色：" + color + ";  尺码：" + size);

        holder.price.setText(TextUtils.isEmpty(result.getPrice()) ? "0" : result.getPrice());
    }

    private void setHead(ItemHeadHolder holder, int position) {
        MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
        holder.statusTv.setText(TextUtils.isEmpty(orderResult.getStatus())?"":orderResult.getStatus());
    }

    private void setBanlanceHead(final BanlanceHeadViewHolder holder, int position) {
        MapiCarResult ware = (MapiCarResult) mList.get(position).getData();

        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (null != itemClickListener)
                    itemClickListener.onItemClick(view, pos);
            }
        });

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(ware.getImg()) ? "" : ware.getImg());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(78), DPUtil.dip2px(78)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.title.setText(TextUtils.isEmpty(ware.getTitle()) ? "" : ware.getTitle());
    }

    private void setBottom(ItemBottomHolder holder, int position) {

        MapiOrderResult mapiOrderResult = (MapiOrderResult) mList.get(position).getData();
        if (null != mapiOrderResult) {
            holder.totalAmount.setText(mapiOrderResult.getTotal_amount());
            holder.goodsTotal.setText(String.format("共%s件商品", TextUtils.isEmpty(mapiOrderResult.getGoods_total())?"0":mapiOrderResult.getGoods_total()));
            holder.expressFee.setText(String.format("(包含运费 ¥ %s)", TextUtils.isEmpty(mapiOrderResult.getExpress_fee())?"0":mapiOrderResult.getExpress_fee()));
            holder.cancel.setTag(position);
            holder.comfirm.setTag(position);
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    if (null != orderDeelListener)
                        orderDeelListener.del(view, pos);
                }
            });
            holder.comfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    if (null != orderDeelListener)
                        orderDeelListener.pay(view, pos);
                }
            });
        }


    }


}
