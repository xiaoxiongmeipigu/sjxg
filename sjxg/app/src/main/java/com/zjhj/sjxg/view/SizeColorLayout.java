package com.zjhj.sjxg.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zjhj.commom.api.ItemApi;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.StringUtil;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.adapter.item.SizeColorAdapter;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.util.ControllerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2016/12/17.
 */
public class SizeColorLayout extends RelativeLayout {


    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.purcaseSheetLayout)
    PurcaseSheetLayout purcaseSheetLayout;
    @Bind(R.id.price)
    TextView priceTv;

    private Context mContext;
    private View view;
    BaseActivity activity;

    List<MapiResourceResult> mList;
    SizeColorAdapter mAdapter;
    private int oldPos = -1;
    double priceDouble = 0;
    double newPrice = 0;
    List<MapiResourceResult> sizeResults;

    private String s_id = "";
    String cover_pic = "";
    String trade = "";

    public SizeColorLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SizeColorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public SizeColorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        if (isInEditMode())
            return;
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_size_color, this);
        ButterKnife.bind(this, view);
        init();
        initListener();
    }

    private void init() {

        mList = new ArrayList<>();
        sizeResults = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SizeColorAdapter(mContext, mList);
        recyclerView.setAdapter(mAdapter);

    }

    private void initListener(){
        mAdapter.setOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MapiResourceResult resourceResult = sizeResults.get(position);
                if(position==oldPos) {
                    resourceResult.setSel(false);
                    oldPos = -1;
                    priceTv.setText("¥"+(StringUtil.numberFormat(priceDouble)));
                }else{
                    if(oldPos>=0){
                        sizeResults.get(oldPos).setSel(false);
                    }
                    oldPos = position;
                    sizeResults.get(oldPos).setSel(true);

                    String fareStr = TextUtils.isEmpty(resourceResult.getFare())?"0":resourceResult.getFare();
                    Double fareDouble = Double.parseDouble(fareStr);

                    newPrice = priceDouble + fareDouble;

                    priceTv.setText("¥"+(StringUtil.numberFormat(newPrice)));

                }

                if(null!=attrInterface){
                    attrInterface.getAttrs(getSpecStr());
                }

            }
        });
    }

    public Double getPrice(){
        return newPrice;
    }

    public void load(JSONObject jsonObject) {

        cover_pic = jsonObject.getJSONObject("data").getString("cover_pic");
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(TextUtils.isEmpty(cover_pic)?"":cover_pic);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(DPUtil.dip2px(100), DPUtil.dip2px(100)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(image.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        image.setController(controller);

        String price = jsonObject.getJSONObject("data").getString("price");
        priceTv.setText("¥"+(TextUtils.isEmpty(price)?"0":price));

        priceDouble = Double.parseDouble(TextUtils.isEmpty(price)?"0":price);

        sizeResults = JSONArray.parseArray(jsonObject.getJSONObject("data").getJSONObject("package").getJSONArray("spec").toJSONString(), MapiResourceResult.class);

        MapiResourceResult parentResult1 = new MapiResourceResult();
        parentResult1.setName("规格");
        if (null != sizeResults) {
            parentResult1.setList(sizeResults);
        }else{
            parentResult1.setList(new ArrayList<MapiResourceResult>());
        }
        mList.add(parentResult1);
        mAdapter.notifyDataSetChanged();

        s_id = jsonObject.getJSONObject("data").getString("id");
        trade = jsonObject.getJSONObject("data").getString("trade");

    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    private OnHideListener onHideListener;

    @OnClick({R.id.close_iv, R.id.purcase, R.id.order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                if (null != onHideListener)
                    onHideListener.hide();
                break;
            case R.id.purcase:
                if(TextUtils.isEmpty(getSpec())){
                    MainToast.showShortToast("请选择商品规格");
                    return;
                }
                if(!TextUtils.isEmpty(s_id))
                    joincar();
                break;
            case R.id.order:

                String attrIds2 = getSpec();
                int counts2 = getCounts();
                String color_name = getColorName();
                String size_name = getSizeName();
                Double price = getPrice();
                if(TextUtils.isEmpty(attrIds2)){
                    MainToast.showShortToast("请选择商品规格");
                    return;
                }
                if(counts2<=0){
                    MainToast.showShortToast("请输入商品购买数量");
                    return;
                }

                if(TextUtils.isEmpty(s_id)){
                    MainToast.showShortToast("商品信息不完善,无法操作");
                    return;
                }

                ArrayList<MapiCartItemResult> cartItemResults = new ArrayList<>();
                ArrayList<MapiCarResult> carResults = new ArrayList<>();

                MapiCarResult carResult = new MapiCarResult();
                carResult.setS_id(s_id);
                carResult.setCover_pic(cover_pic);
                carResult.setTrade(trade);
                carResult.setGoods_type("1");

                carResults.add(carResult);

                MapiCartItemResult mapiCityItemResult = new MapiCartItemResult();
                mapiCityItemResult.setNum(counts2+"");
                mapiCityItemResult.setColor_name(color_name);
                mapiCityItemResult.setSize_name(size_name);
                mapiCityItemResult.setFare_price(price+"");
                mapiCityItemResult.setS_id(s_id);
                mapiCityItemResult.setId(attrIds2);

                cartItemResults.add(mapiCityItemResult);

                carResults.get(0).setSpec(cartItemResults);

                ControllerUtil.go2Balance(cartItemResults,carResults,"DETAIL");

                break;
        }
    }

    public Integer getCounts(){
        return purcaseSheetLayout.getNum();
    }

    public String getSpec(){
        String specIds = "";
        for(MapiResourceResult resourceResult : sizeResults){
            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(specIds))
                    specIds = resourceResult.getId();
                else
                    specIds += "," + resourceResult.getId();
            }
        }
        return specIds;
    }

    public String getColorName(){
        String specStr = "";
        for(MapiResourceResult resourceResult : sizeResults){
            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(specStr))
                    specStr = resourceResult.getColor_name();
                else
                    specStr += "," + resourceResult.getColor_name();
            }
        }
        return specStr;
    }

    public String getSizeName(){
        String sizeStr = "";
        for(MapiResourceResult resourceResult : sizeResults){
            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(sizeStr))
                    sizeStr = resourceResult.getSize_name();
                else
                    sizeStr += "," + resourceResult.getSize_name();
            }
        }
        return sizeStr;
    }

    public String getSpecStr(){
        String attrStr = "";
        for(MapiResourceResult resourceResult : sizeResults){
            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(attrStr))
                    attrStr = "颜色："+resourceResult.getColor_name()+"，尺寸："+resourceResult.getSize_name();
                else
                    attrStr += "；颜色：" + resourceResult.getColor_name()+"，尺寸："+ resourceResult.getSize_name();
            }
        }
        return attrStr;
    }

    public interface OnHideListener {
        void hide();
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        this.onHideListener = onHideListener;
    }

    AttrInterface attrInterface;

    public interface AttrInterface {
        void getAttrs(String attrs);
    }

    public void setAttrListener(AttrInterface attrInterface) {
        this.attrInterface = attrInterface;
    }

    private void joincar(){
        ((BaseActivity)mContext).showLoading();
        ItemApi.joincar(((BaseActivity) mContext), s_id, getSpec(), purcaseSheetLayout.getNum() + "","","", new RequestCallback() {
            @Override
            public void success(Object success) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast("成功加入购物车");
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                ((BaseActivity)mContext).hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

}
