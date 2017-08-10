package com.zjhj.sjxg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.item.ItemListActivity;
import com.zjhj.sjxg.adapter.item.ItemGridAdapter;
import com.zjhj.sjxg.adapter.sort.SortItemAdapter;
import com.zjhj.sjxg.adapter.sort.SortItemMenuAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortFragment extends BaseFrag {

    @Bind(R.id.menu_recycler)
    RecyclerView menuRecycler;
    @Bind(R.id.item_recycler)
    RecyclerView itemRecycler;
    @Bind(R.id.center)
    TextView center;

    SortItemMenuAdapter menuAdapter;
    List<MapiResourceResult> menuList;

    SortItemAdapter sortItemAdapter;
    List<MapiResourceResult> itemList;

    public SortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    private void initView() {

        menuList = new ArrayList<>();
        itemList = new ArrayList<>();
        center.setText("分类列表");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        menuRecycler.setLayoutManager(linearLayoutManager);
        menuAdapter = new SortItemMenuAdapter(getActivity(), menuList);
        menuRecycler.setAdapter(menuAdapter);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        itemRecycler.setLayoutManager(manager);
        itemRecycler.addItemDecoration(new DividerListItemDecoration(getActivity(), OrientationHelper.VERTICAL, DPUtil.dip2px(4), getResources().getColor(R.color.background_gray)));
        sortItemAdapter = new SortItemAdapter(getActivity(), itemList);
        itemRecycler.setAdapter(sortItemAdapter);

    }

    private void initListener() {
        menuAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadsecondary(menuList.get(position).getId());
            }
        });
        sortItemAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AppContext.getInstance(), ItemListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fromType","");
                intent.putExtra("sortId",itemList.get(position).getId());
                AppContext.getInstance().startActivity(intent);
            }
        });
    }

    public void load() {
        showLoading();
        ItemApi.topcategory(getActivity(), new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null!=success&&!success.isEmpty()){
                    menuList.clear();
                    menuList.addAll(success);
                    menuList.get(0).setSel(true);
                    menuAdapter.setSelPosition(0);
                    menuAdapter.notifyDataSetChanged();
                    loadsecondary(menuList.get(0).getId());
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void loadsecondary(String pid){
        ItemApi.secondary(getActivity(),pid, new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                itemList.clear();
                sortItemAdapter.notifyDataSetChanged();
                if(null!=success&&!success.isEmpty()){
                    itemList.addAll(success);
                    sortItemAdapter.notifyDataSetChanged();
                }
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                MainToast.showShortToast(message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
