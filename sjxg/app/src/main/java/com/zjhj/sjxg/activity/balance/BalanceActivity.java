package com.zjhj.sjxg.activity.balance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiAddrResult;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.addr.AddrListActivity;
import com.zjhj.sjxg.adapter.balance.BalanceItemAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.RequestCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BalanceActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.mobile)
    TextView mobile;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.default_tip)
    TextView defaultTip;
    @Bind(R.id.addr_info)
    LinearLayout addrInfo;
    @Bind(R.id.addr_ll)
    LinearLayout addrLl;
    @Bind(R.id.send_ll)
    LinearLayout sendLl;
    @Bind(R.id.shop_name_tv)
    TextView shopNameTv;
    @Bind(R.id.discount_tv)
    TextView discountTv;
    @Bind(R.id.shop_address)
    TextView shopAddress;
    @Bind(R.id.shop_ll)
    LinearLayout shopLl;
    @Bind(R.id.shop_mobile_tv)
    TextView shopMobileTv;

    ArrayList<MapiCartItemResult> itemList;
    ArrayList<MapiCarResult> carList;

    List<IndexData> mList;
    ArrayList<MapiCarResult> list;
    BalanceItemAdapter mAdapter;
    int count;
    double allPrice = 0;

    MapiAddrResult mapiAddrResult;

    String discountStr = "10";
    String order_type = "1";
    String scene = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);

        if (null != getIntent()) {
            itemList = (ArrayList<MapiCartItemResult>) getIntent().getSerializableExtra("itemList");
            carList = (ArrayList<MapiCarResult>) getIntent().getSerializableExtra("carList");
            scene = getIntent().getStringExtra("scene");
        }

        if (null != itemList && !itemList.isEmpty()) {
            initView();
            initListener();
            initItem();
            load();
        }

    }

    private void initView() {

        sendLl.requestFocus();
        sendLl.setFocusableInTouchMode(true);

        list = new ArrayList<>();
        mList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("预订");
        addrLl.setFocusable(true);
        addrLl.setFocusableInTouchMode(true);
        addrLl.requestFocus();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
//        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), Color.parseColor("#eeeeee")));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BalanceItemAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (null != userSP.getUserAddr()) {
            MapiCityItemResult itemResult = userSP.getUserAddr();
            shopNameTv.setText("店铺："+(TextUtils.isEmpty(itemResult.getName())?"":itemResult.getName()));
            shopMobileTv.setText("联系方式："+(TextUtils.isEmpty(itemResult.getMobile())?"":itemResult.getMobile()));
            shopAddress.setText(TextUtils.isEmpty(itemResult.getAddress())?"":itemResult.getAddress());
        }

    }

    private void initListener() {

    }

    private void initItem() {
        for (MapiCarResult ware : carList) {

            MapiCarResult carResult = new MapiCarResult();
            carResult.setS_id(ware.getS_id());
            carResult.setCover_pic(ware.getCover_pic());
            carResult.setTrade(ware.getTrade());
            carResult.setGoods_type(ware.getGoods_type());
            boolean isSel = false;
            for (MapiCartItemResult cartItemResult : ware.getSpec()) {

                if (itemList.contains(cartItemResult)) {
                    isSel = true;
                    carResult.getSpec().add(cartItemResult);
                    String price = TextUtils.isEmpty(cartItemResult.getFare_price()) ? "0" : cartItemResult.getFare_price();
                    String num = TextUtils.isEmpty(cartItemResult.getNum()) ? "1" : cartItemResult.getNum();
                    int numInt = Integer.parseInt(num);
                    double priceDouble = Double.parseDouble(price) * numInt;
                    allPrice += priceDouble;
                }

            }
            if (isSel)
                list.add(carResult);
        }

        price.setText(StringUtil.numberFormat(allPrice));

        refreshList();

    }

    private void load() {
        showLoading();
        ItemApi.orderaddr(this, "", userSP.getUserAddr().getId(), new RequestCallback<MapiAddrResult>() {
            @Override
            public void success(MapiAddrResult success) {
                hideLoading();
                if (null != success) {
                    mapiAddrResult = success;
                }
                if (null != mapiAddrResult&&!TextUtils.isEmpty(mapiAddrResult.getId())) {

                    defaultTip.setVisibility(View.GONE);
                    addrInfo.setVisibility(View.VISIBLE);
                    name.setText("收货人：" + (TextUtils.isEmpty(mapiAddrResult.getConsignee()) ? "" : mapiAddrResult.getConsignee()));
                    mobile.setText(TextUtils.isEmpty(mapiAddrResult.getMobile()) ? "" : mapiAddrResult.getMobile());
                    address.setText(mapiAddrResult.getProvince_name() + " " + mapiAddrResult.getCity_name() + " " + mapiAddrResult.getArea_name() + " " + mapiAddrResult.getAddress());

                    discountStr = TextUtils.isEmpty(mapiAddrResult.getShop_discount())?"10":mapiAddrResult.getShop_discount();
                    double discountdouble = Double.parseDouble(discountStr);
                    if(!TextUtils.isEmpty(discountStr)&&discountdouble<10){
                        discountTv.setVisibility(View.VISIBLE);
                        discountTv.setText("店铺折扣："+mapiAddrResult.getShop_discount());

                        double discountDouble = Double.parseDouble(discountStr);
                        double priceDouble = allPrice * discountDouble;
                        price.setText(StringUtil.numberFormat(priceDouble));

                    }else{
                        discountTv.setVisibility(View.GONE);
                    }


                } else {
                    defaultTip.setVisibility(View.VISIBLE);
                    addrInfo.setVisibility(View.GONE);
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

    private void refreshList() {
        if (null == list || list.isEmpty())
            return;
        int heatCount = 0;
        count = 0;
        if (list == null || list.size() == 0) {
            count = 0;
        } else {
            for (MapiCarResult ware : list) {
                heatCount++;
                mList.add(new IndexData(count++, "head", ware));
                for (int i = 0; i < ware.getSpec().size(); i++) {
                    if ("1".equals(ware.getGoods_type()) || "2".equals(ware.getGoods_type()))
                        mList.add(new IndexData(count++, "item", ware.getSpec().get(i)));
                    else if ("3".equals(ware.getGoods_type()))
                        mList.add(new IndexData(count++, "item_three", ware.getSpec().get(i)));
                }
                if (heatCount <= list.size() - 1)
                    mList.add(new IndexData(count++, "divider", new Object()));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private String getCart_ids(){
        String cart_ids = "";
        for(MapiCartItemResult mapiCartItemResult : itemList){
            if(TextUtils.isEmpty(cart_ids))
                cart_ids = mapiCartItemResult.getId();
            else
                cart_ids += ","+mapiCartItemResult.getId();
        }
        return cart_ids;
    }

    @OnClick({R.id.back, R.id.pay, R.id.addr_ll, R.id.radio_one, R.id.radio_two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.pay:
                String addr_id = "";
                if(order_type.equals("1")){
                    if(null==mapiAddrResult||TextUtils.isEmpty(mapiAddrResult.getId())){
                        MainToast.showShortToast("请选择地址");
                        return;
                    }else
                        addr_id = mapiAddrResult.getId();
                }

                if(scene.equals("CAR")){
                    String ids = getCart_ids();
                    showLoading();
                    ItemApi.placeorder(this, scene, userSP.getUserAddr().getId(), order_type, addr_id, ids, "", "", "", "", new RequestCallback() {
                        @Override
                        public void success(Object success) {
                            hideLoading();
                            MainToast.showShortToast("预订成功");
                            finish();
                        }
                    }, new RequestExceptionCallback() {
                        @Override
                        public void error(Integer code, String message) {
                            hideLoading();
                            MainToast.showShortToast(message);
                        }
                    });
                }else if(scene.equals("DETAIL")){
                    if (null != itemList && !itemList.isEmpty()) {
                        MapiCartItemResult cartItemResult = itemList.get(0);
                        showLoading();
                        ItemApi.placeorder(this, scene, userSP.getUserAddr().getId(), order_type, addr_id,"", cartItemResult.getS_id(), cartItemResult.getId(), "", cartItemResult.getNum(), new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("预订成功");
                                finish();
                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(Integer code, String message) {
                                hideLoading();
                                MainToast.showShortToast(message);
                            }
                        });
                    }

                }else if(scene.equals("PANIC")){
                    if (null != itemList && !itemList.isEmpty()) {
                        MapiCartItemResult cartItemResult = itemList.get(0);
                        showLoading();
                        ItemApi.placeorder(this, scene, userSP.getUserAddr().getId(), order_type, addr_id, "", "", "", cartItemResult.getSpecial_id(), cartItemResult.getNum(), new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("预订成功");
                                finish();
                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(Integer code, String message) {
                                hideLoading();
                                MainToast.showShortToast(message);
                            }
                        });
                    }

                }



                break;
            case R.id.addr_ll:
                Intent intent = new Intent(BalanceActivity.this, AddrListActivity.class);
                intent.putExtra("fromBalance", true);
                intent.putExtra("isShowDel", false);
                startActivityForResult(intent, RequestCode.sel_addr);
                break;
            case R.id.radio_one:
                order_type = "1";
                addrLl.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_two:
                order_type = "2";
                addrLl.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (requestCode == RequestCode.sel_addr) {
                if (null != data) {
                    mapiAddrResult = (MapiAddrResult) data.getSerializableExtra("item");
                    if (null != mapiAddrResult) {
                        defaultTip.setVisibility(View.GONE);
                        addrInfo.setVisibility(View.VISIBLE);
                        name.setText("收货人：" + (TextUtils.isEmpty(mapiAddrResult.getConsignee()) ? "" : mapiAddrResult.getConsignee()));
                        mobile.setText(TextUtils.isEmpty(mapiAddrResult.getMobile()) ? "" : mapiAddrResult.getMobile());
                        address.setText(mapiAddrResult.getProvince_name() + " " + mapiAddrResult.getCity_name() + " " + mapiAddrResult.getArea_name() + " " + mapiAddrResult.getAddress());
                    }else{
                        defaultTip.setVisibility(View.VISIBLE);
                        addrInfo.setVisibility(View.GONE);
                    }

                }

            }
        }
    }

}
