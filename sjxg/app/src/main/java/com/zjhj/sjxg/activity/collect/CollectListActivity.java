package com.zjhj.sjxg.activity.collect;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.ItemGridAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.AnimationUtil;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.BestSwipeRefreshLayout;
import com.zjhj.sjxg.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectListActivity extends BaseActivity {

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
    @Bind(R.id.bottom_layout)
    TextView bottomLayout;

    private boolean isDel = false;
    ItemGridAdapter mAdapter;
    List<MapiItemResult> mList;

    private boolean isShow = false;

    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back);
        center.setText("我的收藏");
        tvRight.setText("编辑");

        mList = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerListItemDecoration(this, OrientationHelper.VERTICAL, DPUtil.dip2px(5), getResources().getColor(R.color.shop_white)));
        mAdapter = new ItemGridAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        swipeRefreshLayout.setBestRefreshListener(new BestSwipeRefreshLayout.BestRefreshListener() {
            @Override
            public void onBestRefresh() {
                refreshData();
            }
        });
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isDel) {
                    mList.get(position).setSel(!mList.get(position).isSel());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ControllerUtil.go2ItemDetail(mList.get(position).getS_id());
                }
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
        showLoading();
        ItemApi.collectionindex(this, pageIndex + "", pageSize + "", new RequestPageCallback<List<MapiItemResult>>() {
            @Override
            public void success(Integer isNext, List<MapiItemResult> success) {

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

    private void refreshData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        load();
    }

    private String getIds(){
        String ids = "";
        for(MapiItemResult itemResult : mList){
            if(itemResult.isSel()){
                if(TextUtils.isEmpty(ids))
                    ids += itemResult.getId();
                else
                    ids += ","+itemResult.getId();
            }

        }
        return ids;
    }

    @OnClick({R.id.back, R.id.tv_right,R.id.bottom_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:

                if (isDel) {
                    isDel = false;
                    tvRight.setText("编辑");
                    if(null!=mList){
                        for(MapiItemResult itemResult : mList){
                            itemResult.setSel(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                } else {
                    isDel = true;
                    tvRight.setText("完成");

                }
                album();
                break;
            case R.id.bottom_layout:
                if(TextUtils.isEmpty(getIds())){
                    MainToast.showShortToast("请选择删除的商品");
                    return;
                }
                uncollect();
                break;
        }
    }

    private void album() {
        if (bottomLayout.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /** 弹出 */
    private void popAlbum() {
        bottomLayout.setVisibility(View.VISIBLE);
        new AnimationUtil(this, R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(bottomLayout);
    }

    /** 隐藏 */
    private void hideAlbum() {
        new AnimationUtil(this, R.anim.translate_down)
                .setLinearInterpolator().startAnimation(bottomLayout);
        bottomLayout.setVisibility(View.GONE);
    }

    private void uncollect(){
        showLoading();
        ItemApi.collectiondel(this, getIds(), new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                MainToast.showShortToast("删除成功");
                Iterator<MapiItemResult> iterator = mList.iterator();
                while(iterator.hasNext()){
                    MapiItemResult itemResult = iterator.next();
                    if(itemResult.isSel())
                        iterator.remove();
                }
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

}
