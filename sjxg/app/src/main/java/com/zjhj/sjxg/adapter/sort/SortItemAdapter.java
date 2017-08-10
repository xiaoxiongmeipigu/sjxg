package com.zjhj.sjxg.adapter.sort;

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
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/21.
 */
public class SortItemAdapter extends RecyclerView.Adapter<SortItemAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<MapiResourceResult> mList;

    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SortItemAdapter(Context context, List<MapiResourceResult> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_sort, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MapiResourceResult itemResult = mList.get(position);
        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=onItemClickListener)
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        });

        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(itemResult.getCover_pic())?"":itemResult.getCover_pic());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(85), DPUtil.dip2px(80)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.image.setController(controller);

        holder.nameTv.setText(TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName());


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        SimpleDraweeView image;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.root_view)
        LinearLayout rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
