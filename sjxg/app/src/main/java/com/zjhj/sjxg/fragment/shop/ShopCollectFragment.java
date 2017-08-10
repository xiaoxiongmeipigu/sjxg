package com.zjhj.sjxg.fragment.shop;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiShopResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.shop.ShopListAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class ShopCollectFragment extends BaseFrag {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<MapiShopResult> mList;
    ShopListAdapter mAdapter;

    public ShopCollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop_collect, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    private void initView() {

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ShopListAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);


    }

    private void initListener() {

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiShopResult shopResult = mList.get(position);
                if(null!=userSP.getUserAddr()){
                    MapiCityItemResult itemResult = userSP.getUserAddr();
                    itemResult.setLongitude(shopResult.getLongitude()+"");
                    itemResult.setLatitude(shopResult.getLatitude()+"");
                    itemResult.setId(shopResult.getId());
                    itemResult.setName(shopResult.getName());
                    userSP.saveUserAddr(itemResult);
                }else{
                    MapiCityItemResult cityItemResult = new MapiCityItemResult();
                    cityItemResult.setLongitude(shopResult.getLongitude()+"");
                    cityItemResult.setLatitude(shopResult.getLatitude()+"");
                    cityItemResult.setId(shopResult.getId());
                    cityItemResult.setName(shopResult.getName());
                    userSP.saveUserAddr(cityItemResult);
                }

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    public void load() {

        showLoading();
        ItemApi.commonshop(getActivity(), new RequestCallback<List<MapiShopResult>>() {
            @Override
            public void success(List<MapiShopResult> success) {
                hideLoading();
                if (null==success)
                    return;
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
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
