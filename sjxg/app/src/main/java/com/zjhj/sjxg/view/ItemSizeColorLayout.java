package com.zjhj.sjxg.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/7/24.
 */
public class ItemSizeColorLayout extends LinearLayout {

    @Bind(R.id.recentflow)
    FlowLayout recentflow;
    private Context mContext;
    private View view;
    private int oldPos = -1;
    private RecyOnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ItemSizeColorLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ItemSizeColorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public ItemSizeColorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_size_color, this);
        ButterKnife.bind(this, view);
    }

    public void load(List<MapiResourceResult> list) {
        if(null!=list&&list.size()>0){
            recentflow.removeAllViews();
            for (int i=0;i<list.size();i++) {
                final MapiResourceResult resourceResult = list.get(i);
                final TextView textView = new TextView(mContext);
                textView.setTag(i);
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 8, 15, 8);
                textView.setLayoutParams(params);
                textView.setPadding(DPUtil.dip2px(15), DPUtil.dip2px(8), DPUtil.dip2px(15), DPUtil.dip2px(8));
                String color_name = TextUtils.isEmpty(resourceResult.getColor_name())?"":resourceResult.getColor_name();
                String size_name = TextUtils.isEmpty(resourceResult.getSize_name())?"":resourceResult.getSize_name();
                textView.setText(color_name+"  "+size_name);
                textView.setTextSize(15);
                textView.setGravity(Gravity.CENTER);
                if(oldPos==i) {
                    textView.setTextColor(mContext.getResources().getColor(R.color.shop_white));
                    textView.setBackgroundResource(R.drawable.rect_solid_color_red_round_5);
                }else{
                    textView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                    textView.setBackgroundResource(R.drawable.rect_solid_color_background_round_5);
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int) view.getTag();
                        if(pos==oldPos) {
                            oldPos = -1;
                            textView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                            textView.setBackgroundResource(R.drawable.rect_solid_color_background_round_5);
                        }else{

                            if(oldPos>=0){
                                TextView oldTextView = (TextView) recentflow.getChildAt(oldPos);
                                oldTextView.setTextColor(mContext.getResources().getColor(R.color.shop_black));
                                oldTextView.setBackgroundResource(R.drawable.rect_solid_color_background_round_5);
                            }
                            oldPos = pos;
                            textView.setTextColor(mContext.getResources().getColor(R.color.shop_white));
                            textView.setBackgroundResource(R.drawable.rect_solid_color_red_round_5);

                        }

                        if(null!=onItemClickListener)
                            onItemClickListener.onItemClick(view,pos);

                    }
                });
                recentflow.addView(textView);
            }
        }


    }

}
