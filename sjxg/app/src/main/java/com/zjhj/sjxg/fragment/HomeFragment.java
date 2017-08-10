package com.zjhj.sjxg.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.shop.ShopListActivity;
import com.zjhj.sjxg.adapter.MainAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.base.RequestCode;
import com.zjhj.sjxg.util.AmapUtil;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.MainAlertDialog;
import com.zjhj.sjxg.widget.MapsDialog;
import com.zjhj.sjxg.widget.PhotoDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFrag {

    @Bind(R.id.shop_tv)
    TextView shopTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IndexData> mList = new ArrayList<>();
    MainAdapter mAdapter;

    MainAlertDialog callDialog;
    String service_tel = "";

    MapsDialog mapsDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        mAdapter = new MainAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        callDialog = new MainAlertDialog(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        if(userSP.checkLogin()){
            if(null==userSP.getUserAddr()||TextUtils.isEmpty(userSP.getUserAddr().getId())){
                MainToast.showLongToast("请先选择门店");
                Intent intent = new Intent(getActivity(), ShopListActivity.class);
                startActivityForResult(intent, RequestCode.SHOP_LIST);
            }
        }

    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service_tel));
                startActivity(intent);
                callDialog.dismiss();
            }
        });
    }



    public void load(){

        String shopId = "";
        String nameStr = "";
        String mobileStr = "";
        MapiCityItemResult itemResult = userSP.getUserAddr();
        if(null!=itemResult){
            shopId = TextUtils.isEmpty(itemResult.getId())?"":itemResult.getId();
            nameStr = TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName();
            mobileStr = TextUtils.isEmpty(itemResult.getMobile())?"":itemResult.getMobile();
        }
        service_tel = mobileStr;
        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);
        shopTv.setText(nameStr);

        showLoading();
        ItemApi.main(getActivity(),shopId, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                if(null==success)
                    return;
                try {
                    List<MapiResourceResult> bannersList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("banner").toJSONString(),MapiResourceResult.class);
                    if(null!=bannersList&&bannersList.size()>0)
                        mList.add(new IndexData(0,"SCROLL",bannersList));

                    mList.add(new IndexData(1,"TOOL",new Object()));
                    mList.add(new IndexData(2,"DIVIDER",new Object()));

                    List<MapiItemResult> sellingList = JSONArray.parseArray(success.getJSONObject("data").getJSONArray("selling").toJSONString(),MapiItemResult.class);
                    if(null!=sellingList&&sellingList.size()>0)
                        mList.add(new IndexData(3,"ITEM",sellingList));

                    mAdapter.notifyDataSetChanged();

                    MapiCityItemResult cityItemResult = JSONObject.parseObject(success.getJSONObject("data").getJSONObject("shopInfo").toJSONString(),MapiCityItemResult.class);

                    if(null!=cityItemResult){
                        service_tel = TextUtils.isEmpty(cityItemResult.getMobile())?"":cityItemResult.getMobile();
                        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);
                        userSP.saveUserAddr(cityItemResult);
                    }else{
                        MainToast.showLongToast("请先选择门店");
                        Intent intent = new Intent(getActivity(), ShopListActivity.class);
                        startActivityForResult(intent, RequestCode.SHOP_LIST);
                    }

                }catch (Exception e){

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


    @OnClick({R.id.shop_ll, R.id.iv_right,R.id.phone_ll,R.id.navi_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_ll:
                Intent intent = new Intent(getActivity(), ShopListActivity.class);
                startActivityForResult(intent, RequestCode.SHOP_LIST);
                break;
            case R.id.iv_right:
                break;
            case R.id.phone_ll:
                if (!TextUtils.isEmpty(service_tel)) {
                    if(null!=callDialog)
                        callDialog.show();
                }
            break;
            case R.id.navi_ll:

                MapiCityItemResult itemResult = userSP.getUserAddr();
                if(null==itemResult||TextUtils.isEmpty(itemResult.getLatitude())||TextUtils.isEmpty(itemResult.getLongitude())){
                    MainToast.showShortToast("店铺地址信息不全，无法导航");
                    return;
                }

                String longitude = itemResult.getLongitude();
                String latitude = itemResult.getLatitude();

                if (mapsDialog == null)
                    mapsDialog = new MapsDialog(getActivity(), R.style.image_dialog_theme);
                mapsDialog.setLongitude(longitude);
                mapsDialog.setLatitude(latitude);

                mapsDialog.showDialog();

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (null != callDialog && callDialog.isShowing()) {
            callDialog.dismiss();
            callDialog = null;
        }

        if(null!=mapsDialog) {
            mapsDialog.dismiss();
            mapsDialog = null;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==RequestCode.SHOP_LIST){
                mList.clear();
                mAdapter.notifyDataSetChanged();
                load();
            }
        }

    }
}
