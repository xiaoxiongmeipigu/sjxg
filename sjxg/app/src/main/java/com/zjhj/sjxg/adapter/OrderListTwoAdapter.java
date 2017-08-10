package com.zjhj.sjxg.adapter;

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
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.OrderDeelListener;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/8/9.
 */
public class OrderListTwoAdapter extends RecyclerView.Adapter<OrderListTwoAdapter.ViewHolder> {

    LayoutInflater inflater;

    private List<MapiOrderResult> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OrderDeelListener orderDeelListener;

    public void setOrderDeelListener(OrderDeelListener orderDeelListener) {
        this.orderDeelListener = orderDeelListener;
    }

    public OrderListTwoAdapter(Context context, List<MapiOrderResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MapiOrderResult orderResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, pos);
            }
        });

        holder.dateTv.setText(TextUtils.isEmpty(orderResult.getAddtime()) ? "" : orderResult.getAddtime());

        if ("0".equals(orderResult.getOrder_type())) {
            holder.bottomLine.setVisibility(View.VISIBLE);
            holder.bottomLayout.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.comfirm.setVisibility(View.GONE);
            holder.statusTv.setText("待发货");
        } else if ("1".equals(orderResult.getOrder_type())) {
            holder.bottomLine.setVisibility(View.VISIBLE);
            holder.bottomLayout.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.GONE);
            holder.comfirm.setVisibility(View.VISIBLE);
            holder.statusTv.setText("已发货");
        } else if ("2".equals(orderResult.getOrder_type())) {
            holder.bottomLine.setVisibility(View.GONE);
            holder.bottomLayout.setVisibility(View.GONE);
            holder.statusTv.setText("已完成");
        } else if ("3".equals(orderResult.getOrder_type())) {
            holder.bottomLine.setVisibility(View.GONE);
            holder.bottomLayout.setVisibility(View.GONE);
            holder.statusTv.setText("商家备货中");
        }


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

        holder.price.setText(TextUtils.isEmpty(orderResult.getPrice()) ? "0" : orderResult.getPrice());

        holder.goodsTotal.setText(String.format("共%s件商品", TextUtils.isEmpty(count) ? "0" : count));

        String s_discount = TextUtils.isEmpty(orderResult.getS_discount()) ? "10" : orderResult.getS_discount();
        double discountdouble = Double.parseDouble(s_discount);
        if (TextUtils.isEmpty(s_discount) || discountdouble>=10) {
            holder.discountTv.setVisibility(View.GONE);
        } else {
            holder.discountTv.setVisibility(View.VISIBLE);
            holder.discountTv.setText("折扣：" + s_discount);
        }

        holder.allPriceTv.setText("合计：¥ " + orderResult.getS_price());

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

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.date_tv)
        TextView dateTv;
        @Bind(R.id.status_tv)
        TextView statusTv;
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
        @Bind(R.id.goods_total)
        TextView goodsTotal;
        @Bind(R.id.discount_tv)
        TextView discountTv;
        @Bind(R.id.all_price_tv)
        TextView allPriceTv;
        @Bind(R.id.cancel)
        TextView cancel;
        @Bind(R.id.comfirm)
        TextView comfirm;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        @Bind(R.id.bottom_line)
        View bottomLine;
        @Bind(R.id.bottom_layout)
        LinearLayout bottomLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
