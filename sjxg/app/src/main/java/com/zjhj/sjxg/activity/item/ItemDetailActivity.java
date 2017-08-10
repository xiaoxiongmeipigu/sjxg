package com.zjhj.sjxg.activity.item;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lzy.widget.VerticalSlide;
import com.zjhj.commom.api.BasicApi;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.fragment.item.ItemDetailFragment;
import com.zjhj.sjxg.fragment.item.ItemInfoFragment;
import com.zjhj.sjxg.util.AnimationUtil;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.view.SizeColorLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.first)
    FrameLayout first;
    @Bind(R.id.second)
    FrameLayout second;
    @Bind(R.id.verticalSlide)
    VerticalSlide verticalSlide;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.bg_view)
    View bgView;
    @Bind(R.id.sizeColorLayout)
    SizeColorLayout sizeColorLayout;

    private ItemInfoFragment topFragment;
    private ItemDetailFragment bottomFragment;

    String id = "";
    String cover_pic = "";
    String goods_id = "";
    String trade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        if(null!=getIntent()){
            id = getIntent().getStringExtra("id");
        }
        if(!TextUtils.isEmpty(id)){
            initView();
            initListener();
            load();
        }
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("商品详情");
        ivRight.setImageResource(R.mipmap.purcase);

        sizeColorLayout.setActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        topFragment = new ItemInfoFragment();
        bottomFragment = new ItemDetailFragment();

        topFragment.setSizeOpenListener(new ItemInfoFragment.SizeOpenListener() {
            @Override
            public void open() {
                album();
            }
        });

        transaction.replace(R.id.first, topFragment);

        transaction.replace(R.id.second, bottomFragment);

        verticalSlide.setOnShowNextPageListener(new VerticalSlide.OnShowNextPageListener() {
            @Override
            public void onShowNextPage() {
//                ((PromptDetailFragment) bottomFragment).load();
            }
        });

        transaction.commit();

    }

    private void load() {

        showLoading() ;
        ItemApi.listdetails(this, id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                topFragment.load(success);
                goods_id = success.getJSONObject("data").getString("id");
                bottomFragment.setWebUrl(BasicApi.BASIC_URL+BasicApi.listinfo+goods_id);
                bottomFragment.load();
                sizeColorLayout.load(success);

                cover_pic = success.getJSONObject("data").getString("cover_pic");
                trade = success.getJSONObject("data").getString("trade");

            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void initListener() {
        sizeColorLayout.setOnHideListener(new SizeColorLayout.OnHideListener() {
            @Override
            public void hide() {
                hideAlbum();
                fab.setVisibility(View.VISIBLE);
            }
        });

        sizeColorLayout.setAttrListener(new SizeColorLayout.AttrInterface() {
            @Override
            public void getAttrs(String attrs) {
                if(null!=topFragment)
                    topFragment.setSelSize(attrs);
            }
        });

    }

    @OnClick({R.id.back, R.id.iv_right, R.id.fab, R.id.purcase, R.id.order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_right:
                ControllerUtil.go2Purcase();
                break;
            case R.id.fab:
                /**
                 * 返回顶部分三步
                 * 1.第二页滚动到第二页的顶部
                 * 2.VerticalSlide从第二页返回第一页
                 * 3.第一页滚动到第一页的顶部
                 * OnGoTopListener 表示第一页滚动到顶部 的方法,这个由于采用什么布局,库内部并不知道,所以一般是自己实现
                 * 也可以不实现,直接传null
                 */
                bottomFragment.goTop();
                verticalSlide.goTop(new VerticalSlide.OnGoTopListener() {
                    @Override
                    public void goTop() {
                        topFragment.goTop();
                    }
                });
                break;
            case R.id.purcase:
                String attrIds = sizeColorLayout.getSpec();
                int counts = sizeColorLayout.getCounts();
                if(TextUtils.isEmpty(attrIds)){
                    MainToast.showShortToast("请选择商品规格");
                    return;
                }
                if(counts<=0){
                    MainToast.showShortToast("请输入商品购买数量");
                    return;
                }
                joincar(attrIds,counts);
                break;
            case R.id.order:
                String attrIds2 = sizeColorLayout.getSpec();
                int counts2 = sizeColorLayout.getCounts();
                String color_name = sizeColorLayout.getColorName();
                String size_name = sizeColorLayout.getSizeName();
                Double price = sizeColorLayout.getPrice();
                if(TextUtils.isEmpty(attrIds2)){
                    MainToast.showShortToast("请选择商品规格");
                    return;
                }
                if(counts2<=0){
                    MainToast.showShortToast("请输入商品购买数量");
                    return;
                }

                if(TextUtils.isEmpty(goods_id)){
                    MainToast.showShortToast("商品信息不完善,无法操作");
                    return;
                }

                ArrayList<MapiCartItemResult> cartItemResults = new ArrayList<>();
                ArrayList<MapiCarResult> carResults = new ArrayList<>();

                MapiCarResult carResult = new MapiCarResult();
                carResult.setS_id(goods_id);
                carResult.setCover_pic(cover_pic);
                carResult.setTrade(trade);
                carResult.setGoods_type("1");

                carResults.add(carResult);

                MapiCartItemResult mapiCityItemResult = new MapiCartItemResult();
                mapiCityItemResult.setNum(counts2+"");
                mapiCityItemResult.setColor_name(color_name);
                mapiCityItemResult.setSize_name(size_name);
                mapiCityItemResult.setFare_price(price+"");
                mapiCityItemResult.setS_id(goods_id);
                mapiCityItemResult.setId(attrIds2);

                cartItemResults.add(mapiCityItemResult);

                carResults.get(0).setSpec(cartItemResults);

                ControllerUtil.go2Balance(cartItemResults,carResults,"DETAIL");


                break;
        }
    }

    private void album() {
        if (sizeColorLayout.getVisibility() == View.GONE) {
            popAlbum();
            fab.setVisibility(View.GONE);
        } else {
            hideAlbum();
            fab.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 弹出
     */
    private void popAlbum() {
        sizeColorLayout.setVisibility(View.VISIBLE);
        new AnimationUtil(this, R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(sizeColorLayout);
        bgView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    private void hideAlbum() {
        new AnimationUtil(this, R.anim.translate_down)
                .setLinearInterpolator().startAnimation(sizeColorLayout);
        sizeColorLayout.setVisibility(View.GONE);
        bgView.setVisibility(View.GONE);
    }

    /**
     * 加入购物车
     * */
    private void joincar(String specIds,int counts){
        showLoading();
        ItemApi.joincar(this,id,specIds, counts + "","","", new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                MainToast.showShortToast("成功加入购物车");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
               hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void buy(){

    }

}
