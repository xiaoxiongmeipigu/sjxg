package com.zjhj.sjxg.activity.limit;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.fragment.item.ItemDetailFragment;
import com.zjhj.sjxg.fragment.item.ItemInfoFragment;
import com.zjhj.sjxg.fragment.limit.LimitInfoFragment;
import com.zjhj.sjxg.util.ControllerUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LimitDetailActivity extends BaseActivity {


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

    private LimitInfoFragment topFragment;
    private ItemDetailFragment bottomFragment;
    String id = "";
    String g_id = "";
    String s_id = "";
    String color_name = "";
    String size_name = "";
    String price = "";
    String cover_pic = "";
    String trade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_detail);
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

    private void initView(){

        back.setImageResource(R.mipmap.back);
        center.setText("商品详情");
        ivRight.setImageResource(R.mipmap.purcase);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        topFragment = new LimitInfoFragment();
        bottomFragment = new ItemDetailFragment();

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

    private void initListener() {

    }

    private void load() {

        showLoading() ;
        ItemApi.flashdetails(this, id, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                s_id = success.getJSONObject("data").getString("s_id");
                topFragment.load(success);
                g_id = success.getJSONObject("data").getString("g_id");
                bottomFragment.setWebUrl(BasicApi.BASIC_URL+BasicApi.listinfo+g_id);
                bottomFragment.load();
                color_name = success.getJSONObject("data").getString("color_name");
                size_name = success.getJSONObject("data").getString("size_name");
                price = success.getJSONObject("data").getString("price");
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

    @OnClick({R.id.back, R.id.iv_right,R.id.purcase, R.id.order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_right:
                ControllerUtil.go2Purcase();
                break;
            case R.id.purcase:
                if(null!=topFragment){
                    int counts = topFragment.getCounts();
                    if(counts<=0){
                        MainToast.showShortToast("请输入商品购买数量");
                        return;
                    }
                    if(TextUtils.isEmpty(g_id)||TextUtils.isEmpty(s_id)){
                        MainToast.showShortToast("商品信息不完善,无法操作");
                        return;
                    }
                    joincar(counts);
                }

                break;
            case R.id.order:
                int counts = topFragment.getCounts();
                if(counts<=0){
                    MainToast.showShortToast("请输入商品购买数量");
                    return;
                }

                if(TextUtils.isEmpty(g_id)){
                    MainToast.showShortToast("商品信息不完善,无法操作");
                    return;
                }

                ArrayList<MapiCartItemResult> cartItemResults = new ArrayList<>();
                ArrayList<MapiCarResult> carResults = new ArrayList<>();

                MapiCarResult carResult = new MapiCarResult();
                carResult.setS_id(g_id);
                carResult.setCover_pic(cover_pic);
                carResult.setTrade(trade);
                carResult.setGoods_type("1");

                carResults.add(carResult);

                MapiCartItemResult mapiCityItemResult = new MapiCartItemResult();
                mapiCityItemResult.setNum(counts+"");
                mapiCityItemResult.setColor_name(color_name);
                mapiCityItemResult.setSize_name(size_name);
                mapiCityItemResult.setFare_price(price+"");
                mapiCityItemResult.setS_id(g_id);
                mapiCityItemResult.setId(s_id);
                mapiCityItemResult.setSpecial_id(id);

                cartItemResults.add(mapiCityItemResult);

                carResults.get(0).setSpec(cartItemResults);

                ControllerUtil.go2Balance(cartItemResults,carResults,"PANIC");
                break;
        }
    }

    /**
     * 加入购物车
     * */
    private void joincar(int counts){
        showLoading();
        ItemApi.joincar(this,g_id,s_id, counts + "",id,"1", new RequestCallback() {
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

}
