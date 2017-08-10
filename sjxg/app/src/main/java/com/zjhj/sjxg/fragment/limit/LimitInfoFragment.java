package com.zjhj.sjxg.fragment.limit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzy.widget.vertical.VerticalRecyclerView;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.ItemInfoAdapter;
import com.zjhj.sjxg.base.BaseFrag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LimitInfoFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    VerticalRecyclerView recyclerView;

    private final static String SCROLL = "SCROLL";
    private final static String TOOL = "LIMIT_TOOL";
    private final static String ITEM = "ITEM";
    List<IndexData> mList = new ArrayList<>();

    ItemInfoAdapter mAdapter;

    MapiItemResult mapiItemResult;
    List<MapiResourceResult> result;

    int count = 1;

    public LimitInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_limit_info, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemInfoAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        mapiItemResult = new MapiItemResult();

    }

    private void initListener(){
        mAdapter.setNumListener(new ItemInfoAdapter.NumListener() {
            @Override
            public void num(int num) {
                count = num;
            }
        });
    }

    public void load(JSONObject jsonObject){

        if(null!=jsonObject){

            result = JSONArray.parseArray(jsonObject.getJSONObject("data").getJSONArray("picList").toJSONString(),MapiResourceResult.class);
            mapiItemResult = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(),MapiItemResult.class);

            if(null!=result){
                mList.add(new IndexData(0, SCROLL,result));
            }else{
                mList.add(new IndexData(0, SCROLL, new ArrayList<MapiResourceResult>()));
            }

            mList.add(new IndexData(1, TOOL,mapiItemResult));

            Collections.sort(mList);
            mAdapter.notifyDataSetChanged();
        }

    }

    public int getCounts(){
        return count;
    }

    @Override
    public void goTop() {
        DebugLog.i("info==>goTop");
        recyclerView.goTop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
