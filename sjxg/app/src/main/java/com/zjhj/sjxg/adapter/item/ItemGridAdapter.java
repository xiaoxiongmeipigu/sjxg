package com.zjhj.sjxg.adapter.item;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.hitomi.cslibrary.CrazyShadow;
import com.hitomi.cslibrary.base.CrazyShadowDirection;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/5.
 */
public class ItemGridAdapter extends RecyclerView.Adapter<ItemGridAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<MapiItemResult> mList;
    private RecyOnItemClickListener onItemClickListener;
    private Context mContext;
    CrazyShadow.Builder crazyShadowRight;
    CrazyShadow.Builder crazyShadowLeft;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ItemGridAdapter(Context context, List<MapiItemResult> list) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = list;
        crazyShadowRight = new CrazyShadow.Builder()
                .setContext(mContext)
                .setDirection(CrazyShadowDirection.RIGHT_BOTTOM)
                .setShadowRadius(DPUtil.dip2px(5))
                .setBaseShadowColor(Color.parseColor("#dddddd"))
                .setImpl(CrazyShadow.IMPL_WRAP);
        crazyShadowLeft = new CrazyShadow.Builder()
                .setContext(mContext)
                .setDirection(CrazyShadowDirection.BOTTOM_LEFT)
                .setShadowRadius(DPUtil.dip2px(5))
                .setBaseShadowColor(Color.parseColor("#dddddd"))
                .setImpl(CrazyShadow.IMPL_WRAP);
    }

    @Override
    public int getItemCount() {
       return null == mList ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_home_item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DebugLog.i("position:" + position);
        holder.root_view.setTag(position);
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        MapiItemResult itemResult = mList.get(position);

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(itemResult.getCover_pic())?"":itemResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(150), DPUtil.dip2px(150)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.title.setText(TextUtils.isEmpty(itemResult.getTrade())?"":itemResult.getTrade());
        holder.price.setText(TextUtils.isEmpty(itemResult.getPrice())?"0":itemResult.getPrice());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shadow_right_item);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shadow_left_item);
        }

        if(itemResult.isSel()){
            holder.bgView.setVisibility(View.VISIBLE);
            holder.selIv.setVisibility(View.VISIBLE);
        }else{
            holder.bgView.setVisibility(View.GONE);
            holder.selIv.setVisibility(View.GONE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        LinearLayout root_view;
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.item_view)
        LinearLayout itemView;
        @Bind(R.id.bg_view)
        View bgView;
        @Bind(R.id.sel_iv)
        ImageView selIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
