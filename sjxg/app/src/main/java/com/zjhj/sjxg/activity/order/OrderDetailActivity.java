package com.zjhj.sjxg.activity.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiOrderDetailResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.order.OrderDetailAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IndexData> mList;
    int count;

    OrderDetailAdapter mAdapter;

    MainAlertDialog callDialog;

    String service_tel = "";

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        if(null!=getIntent())
            id = getIntent().getStringExtra("id");
        if(!TextUtils.isEmpty(id)){
            initView();
            initListener();
            load();
        }
    }

    private void initView() {

        mList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("订单详情");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
//        recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(), OrientationHelper.HORIZONTAL, DPUtil.dip2px(8), Color.parseColor("#f7f7f7")));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderDetailAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        callDialog = new MainAlertDialog(this);

    }

    private void initListener(){
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!TextUtils.isEmpty(service_tel)) {
                    if(null!=callDialog)
                        callDialog.show();
                }else{
                    MainToast.showShortToast("暂无商家联系方式");
                }
            }
        });

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

        service_tel = "";

        showLoading();
        ItemApi.orderdetails(this, id, new RequestCallback<MapiOrderResult>() {
            @Override
            public void success(MapiOrderResult success) {

                hideLoading();
                service_tel = TextUtils.isEmpty(success.getShop_mobile())?"":success.getShop_mobile();
                callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mList.add(new IndexData(count++, "detail_head",success));
                mList.add(new IndexData(count++, "item_detail",success));
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


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != callDialog && callDialog.isShowing()) {
            callDialog.dismiss();
            callDialog = null;
        }
    }
}
