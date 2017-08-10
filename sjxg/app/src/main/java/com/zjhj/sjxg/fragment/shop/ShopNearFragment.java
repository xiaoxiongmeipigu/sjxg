package com.zjhj.sjxg.fragment.shop;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiShopResult;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.MainAdapter;
import com.zjhj.sjxg.adapter.shop.ShopListAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.widget.BestSwipeRefreshLayout;
import com.zjhj.sjxg.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link BaseFrag} subclass.
 */
public class ShopNearFragment extends BaseFrag {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    private List<MapiShopResult> mList;
    ShopListAdapter mAdapter;

    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer counts;
    private String longitude ="";
    private String latitude = "";

    public ShopNearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop_near, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();

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

        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition() >= 0 && (manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public void load() {
        MapiCityItemResult itemResult = userSP.getUserAddr();
        if(null!=itemResult){
            longitude = TextUtils.isEmpty(itemResult.getLongitude())?"":itemResult.getLongitude();
            latitude = TextUtils.isEmpty(itemResult.getLatitude())?"":itemResult.getLatitude();
        }
        showLoading();
        ItemApi.nearbyshop(getActivity(),longitude,latitude, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiShopResult>>() {
            @Override
            public void success(Integer isNext, List<MapiShopResult> success) {
                hideLoading();
                if(null!=swipeRefreshLayout)
                    swipeRefreshLayout.setRefreshing(false);
                counts = isNext;
                if (null==success)
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                if(null!=swipeRefreshLayout)
                    swipeRefreshLayout.setRefreshing(false);
                MainToast.showShortToast(message);
            }
        });

    }

    private void loadNext() {
        if (counts == null || counts <= pageIndex) {
            MainToast.showShortToast("没有更多数据了");
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 1;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
