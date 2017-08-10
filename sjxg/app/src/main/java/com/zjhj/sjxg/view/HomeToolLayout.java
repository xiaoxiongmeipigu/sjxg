package com.zjhj.sjxg.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.MainToolAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.util.JGJDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/8/31.
 */
public class HomeToolLayout extends RelativeLayout {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private View view;
    BaseActivity activity;

    List<MapiResourceResult> mList;
    MainToolAdapter mAdapter;

    public HomeToolLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeToolLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeToolLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_tool, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        initData();
        initListener();

    }

    public void initData() {
        mList.clear();
        mList.addAll(JGJDataSource.getMainResource());
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        mAdapter = new MainToolAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener(){
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0://新品上市
                        ControllerUtil.go2ItemList("new");
                        break;
                    case 1://当季热品
                        ControllerUtil.go2ItemList("hot");
                        break;
                    case 2://促销折扣
                        ControllerUtil.go2ItemList("discount");
                        break;
                    case 3://限时抢购
                        ControllerUtil.go2ItemLimit();
                        break;
                    case 4://vip
                        break;
                    case 5://来样定制
                        break;

                    case 6://售后

                        break;
                    case 7:
                        MainToast.showShortToast("敬请期待");
                        break;
                }
            }
        });
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }
}
