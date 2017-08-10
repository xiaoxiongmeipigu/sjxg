package com.zjhj.sjxg.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiShopResult;
import com.zjhj.commom.util.LocationUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.LocationAddrActivity;
import com.zjhj.sjxg.adapter.TabFragmentAdapter;
import com.zjhj.sjxg.adapter.shop.ShopListAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.RequestCode;
import com.zjhj.sjxg.fragment.shop.ShopCollectFragment;
import com.zjhj.sjxg.fragment.shop.ShopNearFragment;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.addr_tv)
    TextView addrTv;

    private List<String> list_title;
    private List<Fragment> list;
    ShopNearFragment shopNearFragment;
    ShopCollectFragment shopCollectFragment;

    TabFragmentAdapter mAdapter;

    LocationUtil locationUtil;
    private boolean isLoc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);

        locationUtil = new LocationUtil(this);

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!locationUtil.isOPen(this)){
            MainToast.showShortToast("请打开GPS定位");
            isLoc = false;
            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,RequestCode.LOCATION_QUEST); //设置完成后返回到原来的界面
        }else{
            if(!isLoc){
                isLoc = true;
                showLoading();
                locationUtil.startLoc();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null!=locationUtil)
            locationUtil.stopLoc();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("选择门店");

        list_title = new ArrayList<>();
        list = new ArrayList<>();

        shopNearFragment = new ShopNearFragment();
        shopCollectFragment = new ShopCollectFragment();

        list.add(shopNearFragment);
        list.add(shopCollectFragment);

        list_title.add("附近门店");
        list_title.add("常用门店");
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));

        mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), list, list_title);
        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    private void initListener() {

        locationUtil.setOnReceivedMessageListener(new LocationUtil.OnLocationListener() {
            @Override
            public void localInfo(AMapLocation location) {

                if(null!=userSP.getUserAddr()){
                    MapiCityItemResult itemResult = userSP.getUserAddr();
                    itemResult.setLongitude(location.getLongitude()+"");
                    itemResult.setLatitude(location.getLatitude()+"");
                    itemResult.setCity_name(location.getCity());
                    userSP.saveUserAddr(itemResult);
                }else{
                    MapiCityItemResult cityItemResult = new MapiCityItemResult();
                    cityItemResult.setLongitude(location.getLongitude()+"");
                    cityItemResult.setLatitude(location.getLatitude()+"");
                    cityItemResult.setCity_name(location.getCity());
                    userSP.saveUserAddr(cityItemResult);
                }
                if(null!=shopNearFragment){
                    shopNearFragment.refreshData();
                }
                addrTv.setText(location.getAddress());
            }

            @Override
            public void localFail() {
                addrTv.setText("请在地图上选址");
            }
        });

    }

    @OnClick({R.id.back, R.id.addr_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.addr_ll:
                if (!locationUtil.isOPen(this)) {
                    MainToast.showShortToast("请打开GPS定位");
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, RequestCode.LOCATION_QUEST); //设置完成后返回到原来的界面
                } else {
                    Intent intent = new Intent(this, LocationAddrActivity.class);
                    startActivityForResult(intent, RequestCode.SEARCH_RESULT);
                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCode.SEARCH_RESULT:
                    if (null != data) {
                        PoiItem poiItem = data.getParcelableExtra("item");
                        if (null != poiItem) {
                            String provinceName = TextUtils.isEmpty(poiItem.getProvinceName()) ? "" : poiItem.getProvinceName();
                            String cityName = TextUtils.isEmpty(poiItem.getCityName()) ? "" : poiItem.getCityName();
                            String adName = TextUtils.isEmpty(poiItem.getAdName()) ? "" : poiItem.getAdName();
                            String snippetName = TextUtils.isEmpty(poiItem.getSnippet()) ? "" : poiItem.getSnippet();

                            String addr = provinceName + cityName + adName + snippetName;
                            addrTv.setText(addr);

                            LatLonPoint point = poiItem.getLatLonPoint();

                            MapiCityItemResult cityItemResult = new MapiCityItemResult();
                            cityItemResult.setLongitude(point.getLongitude()+"");
                            cityItemResult.setLatitude(point.getLatitude()+"");
                            cityItemResult.setCity_name(poiItem.getCityName());
                            userSP.saveUserAddr(cityItemResult);
                            if(null!=shopNearFragment){
                                shopNearFragment.refreshData();
                            }
                        }
                    }
                    break;
                case RequestCode.LOCATION_QUEST:
                    if (null != locationUtil) {
                        //打开GPS后操作

                    }
                    break;
            }
        }
    }

}
