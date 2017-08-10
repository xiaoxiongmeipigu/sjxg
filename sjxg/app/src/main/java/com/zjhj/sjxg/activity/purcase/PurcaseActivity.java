package com.zjhj.sjxg.activity.purcase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.purcase.PurcaseAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.AdapterSelListener;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.BestSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurcaseActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.all)
    ImageView all;
    @Bind(R.id.allPriceLL)
    LinearLayout allPriceLL;
    @Bind(R.id.order)
    TextView order;

    List<IndexData> mList = new ArrayList<>();
    ArrayList<MapiCarResult> list = new ArrayList<>();
    int count;
    PurcaseAdapter mAdapter;
    //    private ArrayList<MapiItemResult> delList;
    private ArrayList<MapiCartItemResult> selList;
    private ArrayList<MapiCartItemResult> selCartList;
    boolean isAll;

    double allPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purcase);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        //        delList = new ArrayList<>();
        selList = new ArrayList<>();
        selCartList = new ArrayList<>();

        back.setImageResource(R.mipmap.back);
        center.setText("购物车");
        tvRight.setText("编辑");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
//        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(1), Color.parseColor("#eeeeee")));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new PurcaseAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(userSP.checkLogin()){
            refreshData();
        }

    }

    private void initListener() {
        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        mAdapter.setOnAdapterSelListener(new AdapterSelListener() {
            @Override
            public void isAll() {
                notifyAllStatus();
            }

            @Override
            public void notifyParentStatus(int position) {
                MapiCarResult ware = (MapiCarResult) mList.get(position).getData();
                ware.setSel(!ware.isSel());
                for (MapiCartItemResult item : ware.getSpec()) {
                    item.setSel(ware.isSel());
                }
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
            }

            @Override
            public void notifyChildStatus(int position) {
                MapiCartItemResult item = (MapiCartItemResult) mList.get(position).getData();
                item.setSel(!item.isSel());
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
            }

            @Override
            public void notifyChildNum(final int position,final int num) {
                DebugLog.i("notifyChildNum=>"+num);
                showLoading();
                MapiCartItemResult item = (MapiCartItemResult) mList.get(position).getData();
                ItemApi.editcart(PurcaseActivity.this, item.getId(), num + "", new RequestCallback<JSONObject>() {
                    @Override
                    public void success(JSONObject success) {
                        hideLoading();
                        String numStr = "";
                        try{
                            numStr = success.getJSONObject("data").getString("num");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(TextUtils.isEmpty(numStr))
                            numStr = "0";
                        MapiCartItemResult item = (MapiCartItemResult) mList.get(position).getData();
                        item.setNum(numStr);
                        mAdapter.notifyDataSetChanged();
                        notifyAllStatus();
                    }
                }, new RequestExceptionCallback() {
                    @Override
                    public void error(Integer code, String message) {
                        hideLoading();
                        MainToast.showShortToast(message);
                    }
                });
            }
        });

    }

    public void load() {
        showLoading();
        ItemApi.carlist(this, new RequestCallback<List<MapiCarResult>>() {
            @Override
            public void success(List<MapiCarResult> success) {
                hideLoading();
                if(null!=swipeRefreshLayout)
                    swipeRefreshLayout.setRefreshing(false);
                if(success.isEmpty())
                    return;
                list.clear();
                list.addAll(success);

                for(MapiCarResult carResult : list){
                    carResult.setGoods_type("1");
                }
                refreshList(list);
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

    public void refreshData(){
        list.clear();
        mList.clear();
        isAll = false;
        selList.clear();
        selCartList.clear();
        mAdapter.notifyDataSetChanged();
        notifyAllStatus();
        load();
    }

    private void notifyAllStatus() {
        DebugLog.i("notifyAllStatus");
        selList.clear();
        isAll = true;
        allPrice = 0 ;
        if(!mList.isEmpty()){
            for (IndexData indexData : mList) {
                if (indexData.getType().equals("head")) {
                    MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                    if (!mapiCartResult.isSel()) {
                        DebugLog.i("notifyAllStatus==>head");
                        isAll = false;
                    }
                } else if (indexData.getType().equals("item")||indexData.getType().equals("item_three")) {
                    MapiCartItemResult mapiItemResult = (MapiCartItemResult) indexData.getData();

                    if (!mapiItemResult.isSel()) {
                        DebugLog.i("notifyAllStatus==>item");
                        isAll = false;

                    } else {
                        String price = TextUtils.isEmpty(mapiItemResult.getFare_price())?"0":mapiItemResult.getFare_price();
                        String num = TextUtils.isEmpty(mapiItemResult.getNum())?"1":mapiItemResult.getNum();
                        int numInt = Integer.parseInt(num);
                        double priceDouble = Double.parseDouble(price) * numInt;
                        allPrice += priceDouble;
                        selList.add(mapiItemResult);
                    }
                }
            }
        }else
            isAll = false;

        price.setText(StringUtil.numberFormat(allPrice));
        if (isAll)
            all.setImageResource(R.mipmap.sel_right);
        else
            all.setImageResource(R.mipmap.sel);

    }

    @OnClick({R.id.all, R.id.order,R.id.tv_right,R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.all:
                for (IndexData indexData : mList) {
                    if(indexData.getType().equals("head")){
                        MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                        mapiCartResult.setSel(!isAll);

                    }else if(indexData.getType().equals("item")||indexData.getType().equals("item_three")){
                        MapiCartItemResult mapiItemResult = (MapiCartItemResult) indexData.getData();
                        mapiItemResult.setSel(!isAll);

                    }
                }
                isAll = !isAll;
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
                break;
            case R.id.order:
                if(null==selList||selList.isEmpty())
                    MainToast.showShortToast("请选择商品");
                else{
                    if("编辑".equals(tvRight.getText().toString())){
                        ControllerUtil.go2Balance(selList,list,"CAR");
                    } else if("完成".equals(tvRight.getText().toString())){
                        del();
                    }
                }
                break;
            case R.id.tv_right:
                if("编辑".equals(tvRight.getText().toString())){
                    tvRight.setText("完成");
                    order.setText("删除");
                    allPriceLL.setVisibility(View.INVISIBLE);
                    mAdapter.setDel(true);
                }else if("完成".equals(tvRight.getText().toString())){
                    tvRight.setText("编辑");
                    order.setText("结算");
                    allPriceLL.setVisibility(View.VISIBLE);
                    mAdapter.setDel(false);
                }
                selList.clear();
                for (IndexData indexData : mList) {
                    if(indexData.getType().equals("head")){
                        MapiCarResult mapiCartResult = (MapiCarResult) indexData.getData();
                        mapiCartResult.setSel(false);

                    }else if(indexData.getType().equals("item")||indexData.getType().equals("item_three")){
                        MapiCartItemResult mapiItemResult = (MapiCartItemResult) indexData.getData();
                        mapiItemResult.setSel(false);

                    }
                }
                isAll = false;
                mAdapter.notifyDataSetChanged();
                notifyAllStatus();
                break;
        }
    }

    private void del(){
        String rec_ids = "";
        for(MapiCartItemResult itemResult:selList){
            if(TextUtils.isEmpty(rec_ids))
                rec_ids += itemResult.getId();
            else
                rec_ids +=","+ itemResult.getId();
        }
        DebugLog.i("rec_ids==>"+rec_ids);
        showLoading();
        ItemApi.cartdel(this, rec_ids, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                MainToast.showShortToast("成功删除");
                for(MapiCarResult mapiCartResult : list){
                    Iterator<MapiCartItemResult> it =mapiCartResult.getSpec().iterator();
                    while(it.hasNext()){
                        MapiCartItemResult x = it.next();
                        if(selList.contains(x)){
                            DebugLog.i("delList.contains(x)=>"+x.getId());
                            it.remove();
                        }
                    }
                }

                Iterator<MapiCarResult> it =list.iterator();
                while(it.hasNext()){
                    MapiCarResult mapiCartResult = it.next();
                    if(null==mapiCartResult.getSpec()||mapiCartResult.getSpec().size()==0){
                        it.remove();
                    }
                }
                selList.clear();
                refreshList(list);

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void refreshList( List<MapiCarResult> data){
        mList.clear();
        int heatCount=0;
        count = 0;
        if (data == null || data.size() == 0) {
            count = 0;
        } else {
            for (MapiCarResult ware : data) {
                heatCount++;
                mList.add(new IndexData(count++, "head", ware));
                for (int i = 0; i < ware.getSpec().size(); i++) {
                    if("1".equals(ware.getGoods_type())||"2".equals(ware.getGoods_type()))
                        mList.add(new IndexData(count++, "item", ware.getSpec().get(i)));
                    else if("3".equals(ware.getGoods_type()))
                        mList.add(new IndexData(count++, "item_three", ware.getSpec().get(i)));
                }
                if (heatCount <= data.size() - 1)
                    mList.add(new IndexData(count++, "divider", new Object()));
            }
        }
        mAdapter.notifyDataSetChanged();
        notifyAllStatus();
    }
}
