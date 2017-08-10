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
import com.zjhj.commom.result.MapiAddrResult;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/28.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater inflater;
    private Context mContext;
    List<IndexData> mList = new ArrayList<>();

    private RecyOnItemClickListener itemClickListener;

    public void setRecyOnItemClickListener(RecyOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderDetailAdapter(Context context, List<IndexData> list) {
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
        } else if (type.equals("detail_head")) {
            return 2;
        } else if (type.equals("banlance_head")) {
            return 3;
        } else if (type.equals("item")) {
            return 4;
        }else if (type.equals("item_detail")) {
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
                return new DetailHeadHolder(inflater.inflate(R.layout.item_order_detail_head, parent, false));
            case 3:
                return new BanlanceHeadViewHolder(inflater.inflate(R.layout.item_balance_head, parent, false));
            case 4:
                return new BanlanceItemViewHolder(inflater.inflate(R.layout.item_balance_item, parent, false));
            case 5:
                return new ItemViewHolder(inflater.inflate(R.layout.item_order_detail, parent, false));
            default:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_ten, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BanlanceItemViewHolder) {
            setBanlanceItem((BanlanceItemViewHolder) holder, position);
        } else if (holder instanceof DetailHeadHolder) {
            setHead((DetailHeadHolder) holder, position);
        } else if (holder instanceof BanlanceHeadViewHolder) {
            setBanlanceHead((BanlanceHeadViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            setItem((ItemViewHolder) holder, position);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.attr)
        TextView attr;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.root_view)
        LinearLayout rootView;

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

    class DetailHeadHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.express)
        TextView express;
        @Bind(R.id.shop_name_tv)
        TextView shopNameTv;
        @Bind(R.id.shop_mobile)
        TextView shopMobile;
        @Bind(R.id.shop_address)
        TextView shopAddress;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.mobile)
        TextView mobile;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.addtime)
        TextView addtime;
        @Bind(R.id.goods_price)
        TextView goodsPrice;
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.total_price)
        TextView totalPrice;
        @Bind(R.id.address_view)
        View addressView;
        @Bind(R.id.address_ll)
        LinearLayout addressLl;

        public DetailHeadHolder(View itemView) {
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

    private void setItem(ItemViewHolder holder, int position) {
        MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(orderResult.getCover_pic()) ? "" : orderResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(78), DPUtil.dip2px(78)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.title.setText(TextUtils.isEmpty(orderResult.getTrade()) ? "" : orderResult.getTrade());

        String count = TextUtils.isEmpty(orderResult.getCount()) ? "1" : orderResult.getCount();
        holder.num.setText("X " + count);

        String color = TextUtils.isEmpty(orderResult.getColor_name()) ? "" : orderResult.getColor_name();
        String size = TextUtils.isEmpty(orderResult.getSize_name()) ? "" : orderResult.getSize_name();

        holder.attr.setText("颜色：" + color + ";  尺码：" + size);
    }


    private void setBanlanceItem(BanlanceItemViewHolder holder, int position) {
        MapiCartItemResult result = (MapiCartItemResult) mList.get(position).getData();

        String count = TextUtils.isEmpty(result.getCount()) ? "1" : result.getCount();
        holder.num.setText("X " + count);

        String color = TextUtils.isEmpty(result.getColor()) ? "" : result.getColor();
        String size = TextUtils.isEmpty(result.getSize()) ? "" : result.getSize();

        holder.attr.setText("颜色：" + color + ";  尺码：" + size);

        holder.price.setText(TextUtils.isEmpty(result.getPrice()) ? "0" : result.getPrice());
    }

    private void setHead(DetailHeadHolder holder, int position) {
        MapiOrderResult orderResult = (MapiOrderResult) mList.get(position).getData();
        holder.shopMobile.setTag(position);
        holder.shopMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != itemClickListener)
                    itemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });
        String status = orderResult.getStatus();
        if ("0".equals(status)) {
            holder.express.setText("配送方式：自提");
            holder.addressView.setVisibility(View.GONE);
            holder.addressLl.setVisibility(View.GONE);
        } else if ("1".equals(status)) {
            holder.express.setText("配送方式：送货上门");
            holder.addressView.setVisibility(View.VISIBLE);
            holder.addressLl.setVisibility(View.VISIBLE);
        }

        String order_type = orderResult.getOrder_type();
        if ("0".equals(order_type)) {
            holder.status.setText("待发货");
        } else if ("1".equals(order_type)) {
            holder.status.setText("已发货");
        } else if ("2".equals(order_type)) {
            holder.status.setText("已完成");
        } else if ("3".equals(order_type)) {
            holder.status.setText("商家备货中");
        }

        holder.shopNameTv.setText("店铺：" + (TextUtils.isEmpty(orderResult.getShop_name()) ? "" : orderResult.getShop_name()));
        holder.shopAddress.setText(TextUtils.isEmpty(orderResult.getShop_address()) ? "" : orderResult.getShop_address());
        MapiAddrResult addrResult = orderResult.getAddress();
        if (null != addrResult) {
            holder.nameTv.setText("收货人：" + (TextUtils.isEmpty(addrResult.getName()) ? "" : addrResult.getName()));
            holder.mobile.setText(TextUtils.isEmpty(addrResult.getMobile()) ? "" : addrResult.getMobile());
            holder.address.setText(TextUtils.isEmpty(addrResult.getAddress()) ? "" : addrResult.getAddress());
        }

        holder.orderId.setText("订单编号：" + (TextUtils.isEmpty(orderResult.getNumber()) ? "" : orderResult.getNumber()));
        holder.addtime.setText("下单时间：" + (TextUtils.isEmpty(orderResult.getAddtime()) ? "" : orderResult.getAddtime()));

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

}
