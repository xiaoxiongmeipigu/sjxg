package com.zjhj.sjxg.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.ItemGridAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/7/20.
 */
public class HomeHotLayout extends LinearLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private View view;
    BaseActivity activity;

    List<MapiItemResult> mList;
    ItemGridAdapter mAdapter;

    public HomeHotLayout(Context context) {
        super(context);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeHotLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    public HomeHotLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        activity = (BaseActivity) mContext;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_hot, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        initData();
        initListener();

    }

    public void initData() {
        mList.clear();
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.VERTICAL, DPUtil.dip2px(5), getResources().getColor(R.color.shop_white)));
        mAdapter = new ItemGridAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2ItemDetail(mList.get(position).getId());
            }
        });
    }

    public void load(List<MapiItemResult> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.more_ll)
    public void onClick() {
        ControllerUtil.go2ItemList("");
    }
}
