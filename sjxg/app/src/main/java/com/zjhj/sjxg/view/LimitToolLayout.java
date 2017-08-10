package com.zjhj.sjxg.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by brain on 2017/8/8.
 */
public class LimitToolLayout extends LinearLayout {

    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.stock_tv)
    TextView stockTv;
    @Bind(R.id.countdownView)
    CountdownView countdownView;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.size_tv)
    TextView sizeTv;
    @Bind(R.id.color_tv)
    TextView colorTv;
    @Bind(R.id.purcaseSheetLayout)
    PurcaseSheetLayout purcaseSheetLayout;
    private Context mContext;
    private View view;
    MapiItemResult itemResult;

    public LimitToolLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public LimitToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public LimitToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_limit_tool, this);
        ButterKnife.bind(this, view);
        initListener();
    }

    public void load(MapiItemResult mapiItemResult) {
        itemResult = mapiItemResult;
        if (null != mapiItemResult) {

            long endTime = Long.parseLong(TextUtils.isEmpty(mapiItemResult.getEndtime()) ? "0" : mapiItemResult.getEndtime());
            countdownView.start(endTime);
            countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    MainToast.showShortToast("结束了");
                }
            });

            title.setText(TextUtils.isEmpty(mapiItemResult.getTrade()) ? "" : mapiItemResult.getTrade());
            price.setText(TextUtils.isEmpty(mapiItemResult.getPrice()) ? "0" : mapiItemResult.getPrice());
            stockTv.setText("库存" + (TextUtils.isEmpty(mapiItemResult.getStock()) ? "0" : mapiItemResult.getStock()));
            sizeTv.setText("尺码:"+(TextUtils.isEmpty(mapiItemResult.getSize_name()) ? "" : mapiItemResult.getSize_name()));
            colorTv.setText("颜色:"+(TextUtils.isEmpty(mapiItemResult.getColor_name()) ? "" : mapiItemResult.getColor_name()));

        }
    }

    private void initListener() {
        purcaseSheetLayout.setNumListener(new PurcaseSheetLayout.NumInterface() {
            @Override
            public void modify(View view, int num, String price) {
                if(null!=numListener)
                    numListener.num(num);
            }
        });
    }

    public NumListener numListener;

    public interface NumListener{
        void num(int num);
    }

    public void setNumListener(NumListener numListener){
        this.numListener = numListener;
    }

}
