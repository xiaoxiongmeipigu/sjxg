package com.zjhj.sjxg.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.ItemItemAdapter;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2016/12/14.
 */
public class ItemItemLayout extends RelativeLayout {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context mContext;
    private View view;

    ItemItemAdapter mAdapter;
    List<MapiItemResult> mList;

    public ItemItemLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ItemItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public ItemItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_item, this);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ItemItemAdapter(mContext,mList);
        recyclerView.setAdapter(mAdapter);
        initListener();
    }

    public void load(List<MapiItemResult> list) {
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2ItemDetail(mList.get(position).getId());

            }
        });
    }

}
