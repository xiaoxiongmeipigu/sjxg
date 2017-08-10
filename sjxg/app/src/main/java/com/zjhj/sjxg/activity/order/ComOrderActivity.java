package com.zjhj.sjxg.activity.order;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.OrderListTwoAdapter;
import com.zjhj.sjxg.adapter.order.OrderListAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.OrderDeelListener;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.BestSwipeRefreshLayout;
import com.zjhj.sjxg.widget.DividerListItemDecoration;
import com.zjhj.sjxg.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComOrderActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    BestSwipeRefreshLayout swipeRefreshLayout;

    OrderListTwoAdapter mAdapter;
    List<MapiOrderResult> mList;
    int delPos = -1;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer counts;

    MainAlertDialog cancelDialog;
    MainAlertDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_com_order);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("已完成订单");

        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.HORIZONTAL, DPUtil.dip2px(10), Color.parseColor("#f7f7f7")));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderListTwoAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        cancelDialog = new MainAlertDialog(this);
        cancelDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认取消该订单？");

        confirmDialog = new MainAlertDialog(this);
        confirmDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认收货？");

    }

    private void initListener() {

        cancelDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog.dismiss();
            }
        });

        cancelDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog.dismiss();
                if(delPos>=0)
                    orderdel();
            }
        });

        confirmDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
                if(delPos>=0)
                    orderconfirm();
            }
        });

        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });

        mAdapter.setOrderDeelListener(new OrderDeelListener() {
            @Override
            public void del(View view, int position) {

                delPos = position;
                cancelDialog.show();

            }

            @Override
            public void pay(View view, int position) {
                delPos = position;
                confirmDialog.show();
            }
        });

        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2OrderDetail(mList.get(position).getId());
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

    public void load(){

        showLoading();
        ItemApi.userorder(this, pageIndex + "", pageSize + "", "1", new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
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

    private void orderdel(){
        showLoading();
        ItemApi.orderdel(this, mList.get(delPos).getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(delPos);
                mAdapter.notifyDataSetChanged();
                delPos = -1;
                MainToast.showShortToast("取消成功");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void orderconfirm(){
        showLoading();
        ItemApi.orderconfirm(this, mList.get(delPos).getId(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                mList.remove(delPos);
                mAdapter.notifyDataSetChanged();
                delPos = -1;
                MainToast.showShortToast("确认成功");
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
        if (null != cancelDialog && cancelDialog.isShowing()) {
            cancelDialog.dismiss();
            cancelDialog = null;
        }
        if (null != confirmDialog && confirmDialog.isShowing()) {
            confirmDialog.dismiss();
            confirmDialog = null;
        }
    }

}
