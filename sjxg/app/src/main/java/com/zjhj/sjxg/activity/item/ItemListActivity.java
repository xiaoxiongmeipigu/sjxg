package com.zjhj.sjxg.activity.item;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.SearchActivity;
import com.zjhj.sjxg.adapter.item.ItemGridAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.RequestCode;
import com.zjhj.sjxg.fragment.ItemMenuFrag;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.BestSwipeRefreshLayout;
import com.zjhj.sjxg.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    BestSwipeRefreshLayout refreshLayout;
    // 实例化抽屉菜单
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.root_view)
    LinearLayout rootView;
    @Bind(R.id.ll_drawerlayout)
    LinearLayout ll_drawerlayout;
    @Bind(R.id.search_tv)
    TextView searchTv;
    @Bind(R.id.clear_iv)
    ImageView clearIv;

    private ItemMenuFrag fragment = null;
    private FragmentManager fragmentManager;
    ItemGridAdapter mAdapter;
    List<MapiItemResult> mList;
    private boolean isCanAccessFocus = true;

    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer counts;

    private String fromType = "";
    private String search = "";
    private String sort = "1";
    private String seasonId = "";
    private String textureId = "";
    private String sortId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
        if(null!=getIntent()) {
            fromType = getIntent().getStringExtra("fromType");
            sortId = getIntent().getStringExtra("sortId");
        }
        if(TextUtils.isEmpty(fromType))
            fromType = "";
        initView();
        initListener();
        load();
    }

    private void initView() {

        mList = new ArrayList<>();
        back.setImageResource(R.mipmap.back);

        fragmentManager = getSupportFragmentManager();

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.VERTICAL, DPUtil.dip2px(5), getResources().getColor(R.color.shop_white)));
        mAdapter = new ItemGridAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        try {
            fragment = new ItemMenuFrag();
            replaceFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawerLayout.setFocusableInTouchMode(true);
        // 设置drawlayout的状态监听事件
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            @Override
            public void onDrawerOpened(View arg0) {
                System.out.println("-------------此时打开");
                isCanAccessFocus = false;
            }

            @Override
            public void onDrawerClosed(View arg0) {
                System.out.println("-------------此时关闭");
                isCanAccessFocus = true;
            }
        });
        solveConflict();

    }

    private void replaceFragment(ItemMenuFrag newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 创建事物操作对象
        if (!newFragment.isAdded()) {// 如果没有将fragment添加到
            transaction
                    .add(R.id.fragment_content, newFragment)
                    .commitAllowingStateLoss();

        } else {
            transaction.show(newFragment)
                    .commitAllowingStateLoss();
        }

    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    /**
     * 解决冲突处理方法
     */
    public void solveConflict() {
        rootView.setOnTouchListener(new CustomTouchListener());
    }

    // 判断drawerLayout后的点击事件是否可以获取焦点
    public boolean canScroll() {
        return isCanAccessFocus;

    }

    class CustomTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean canScroll = canScroll();
            // System.out.println("------canScroll---------" + canScroll);
            if (!canScroll) {
                event.setAction(MotionEvent.ACTION_CANCEL);
                return false;
            } else {
                return v.onTouchEvent(event);
            }
        }

    }

    private void initListener() {
        refreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
               refreshData();
            }
        });
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ControllerUtil.go2ItemDetail(mList.get(position).getId());
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

        if(null!=fragment){
            fragment.setOnCompleteClickListener(new ItemMenuFrag.OnCompleteClickListener() {
                @Override
                public void onCompleteClick(String season, String texture) {
                    seasonId = season;
                    textureId = texture;
                    refreshData();
                }
            });
        }

    }


    public void load() {

        showLoading();
        ItemApi.listindex(this, pageIndex + "", pageSize + "", fromType, search, sort, seasonId, textureId, sortId,new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {
                hideLoading();
                if(null!=refreshLayout)
                    refreshLayout.setRefreshing(false);
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
                if(null!=refreshLayout)
                    refreshLayout.setRefreshing(false);
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

    @OnClick({R.id.back,R.id.search_tv, R.id.radio_one, R.id.radio_two, R.id.radio_three, R.id.radio_four, R.id.radio_five,R.id.clear_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_tv:
                Intent intent = new Intent(ItemListActivity.this, SearchActivity.class);
                startActivityForResult(intent, RequestCode.SEARCH_HIS);
                break;
            case R.id.radio_one:
                sort = "1";
                refreshData();
                break;
            case R.id.radio_two:
                sort = "2";
                refreshData();
                break;
            case R.id.radio_three:
                sort = "3";
                refreshData();
                break;
            case R.id.radio_four:
                sort = "4";
                refreshData();
                break;
            case R.id.radio_five:
                if(null!=drawerLayout&&drawerLayout.isDrawerOpen(ll_drawerlayout))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                else
                    drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.clear_iv:
                search = "";
                searchTv.setText("");
                refreshData();
                clearIv.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.SEARCH_HIS) {
                if (null != data) {
                    String key = data.getStringExtra("keyword");
                    if (TextUtils.isEmpty(key))
                        search = "";
                    else
                        search = key;
                    searchTv.setText(search);
                    clearIv.setVisibility(View.VISIBLE);
                    refreshData();
                }
            }
        }
    }

}
