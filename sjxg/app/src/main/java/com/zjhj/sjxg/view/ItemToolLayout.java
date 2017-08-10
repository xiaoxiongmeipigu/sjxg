package com.zjhj.sjxg.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hitomi.cslibrary.CrazyShadow;
import com.hitomi.cslibrary.base.CrazyShadowDirection;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/12/14.
 */
public class ItemToolLayout extends RelativeLayout {

    @Bind(R.id.old_price)
    TextView oldPrice;
    @Bind(R.id.selSizeLL)
    LinearLayout selSizeLL;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.sell_count)
    TextView sellCount;
    @Bind(R.id.size_tv)
    TextView sizeTv;
    @Bind(R.id.collect_iv)
    ImageView collectIv;

    private Context mContext;
    private View view;
    MapiItemResult itemResult;

    public ItemToolLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ItemToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public ItemToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_tool, this);
        ButterKnife.bind(this, view);
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        new CrazyShadow.Builder()
                .setContext(mContext)
                .setDirection(CrazyShadowDirection.ALL)
                .setShadowRadius(DPUtil.dip2px(5))
                .setBaseShadowColor(Color.parseColor("#dddddd"))
                .setImpl(CrazyShadow.IMPL_WRAP)
                .action(selSizeLL);
        initListener();
    }

    public void load(MapiItemResult mapiItemResult) {
        itemResult= mapiItemResult;
        if (null != mapiItemResult) {
            title.setText(TextUtils.isEmpty(mapiItemResult.getTrade()) ? "" : mapiItemResult.getTrade());
            price.setText(TextUtils.isEmpty(mapiItemResult.getPrice()) ? "0" : mapiItemResult.getPrice());
            sellCount.setText("已成交" + (TextUtils.isEmpty(mapiItemResult.getSales()) ? "0" : mapiItemResult.getSales()) + "笔");
            sizeTv.setText(TextUtils.isEmpty(mapiItemResult.getArrtStr())?"请选择规格":mapiItemResult.getArrtStr());

            if(mapiItemResult.getIs_collection().equals("1"))
                collectIv.setImageResource(R.mipmap.collect_yellow);
            else if(mapiItemResult.getIs_collection().equals("2"))
                collectIv.setImageResource(R.mipmap.uncollect_gray);
        }
    }

    private void initListener() {

    }


    public SizeOpenListener sizeOpenListener;

    @OnClick({R.id.collect_iv, R.id.selSizeLL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collect_iv:
                if(null!=itemResult){

                    if(itemResult.getIs_collection().equals("1"))
                        uncollect();
                    else if(itemResult.getIs_collection().equals("2"))
                        collect();

                }
                break;
            case R.id.selSizeLL:
                if (null != sizeOpenListener)
                    sizeOpenListener.open();
                break;
        }
    }

    public interface SizeOpenListener {
        void open();
    }

    public void setSizeOpenListener(SizeOpenListener sizeOpenListener) {
        this.sizeOpenListener = sizeOpenListener;
    }

    private void collect(){
        ((BaseActivity)mContext).showLoading();
        ItemApi.collectionadd(((BaseActivity) mContext), itemResult.getId(), new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast("您已成功收藏");
                collectIv.setImageResource(R.mipmap.collect_yellow);
                itemResult.setIs_collection("1");

                String c_id = TextUtils.isEmpty(success.getJSONObject("data").getString("id"))?"":success.getJSONObject("data").getString("id");
                itemResult.setC_id(c_id);

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void uncollect(){
        ((BaseActivity)mContext).showLoading();
        ItemApi.collectiondel(((BaseActivity) mContext), itemResult.getC_id(), new RequestCallback() {
            @Override
            public void success(Object success) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast("您已取消收藏");
                collectIv.setImageResource(R.mipmap.uncollect_gray);
                itemResult.setIs_collection("2");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

}
