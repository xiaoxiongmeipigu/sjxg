package com.zjhj.sjxg.activity.addr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.widget.CityDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddrActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.mobile)
    EditText mobile;
    @Bind(R.id.area)
    TextView area;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.defaultIv)
    ImageView defaultIv;

    private int isDefault = 0;

    private CityDialog cityDiolog = null;
    private String province = "";//省
    private String city = "";//市
    private String county = "";//区

    private String provinceID = "";//省
    private String cityID = "";//市
    private String countyID = "";//区

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_addr);
        ButterKnife.bind(this);
        initView();
        if (TextUtils.isEmpty(userSP.getAddr()))
            load();
    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("添加收货地址");
        tvRight.setText("确认");
    }

    public void load(){
        showLoading();
        CommonApi.defaultregion(this, new RequestCallback<JSONObject>() {
            @Override
            public void success(JSONObject success) {
                hideLoading();
                userSP.setAddr(success.toJSONString());
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(Integer code, String message) {
                hideLoading();
                MainToast.showShortToast(message);
            }
        });
    }

    private void getAddress(String json) {
        setWindowBackgroundBlure(true);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (null == cityDiolog) {
            cityDiolog = new CityDialog(this, json, new CityDialog.PriorityListener() {

                @Override
                public void refreshPriorityUI(String proviceName, String cityName,
                                              String districtName) {

                }

                @Override
                public void refreshPriorityUI(String proviceName, String cityName, String districtName, String proviceId, String cityId, String districtId) {
                    province = proviceName;
                    city = cityName;
                    county = districtName;
                    area.setText(province + "-" + city + "-" + county);
                    provinceID = proviceId;
                    cityID = cityId;
                    countyID = districtId;
                }
            }, width, height, "请选择省市区", province, city, county);//选择日期
        }

        Window window = cityDiolog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        //		window.setWindowAnimations(R.style.AnimationPreview3); // 添加动画
        cityDiolog.setCancelable(true);
        cityDiolog.setCanceledOnTouchOutside(true);
        cityDiolog.show();
        cityDiolog.setOnDismissListener(new Dialog.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                setWindowBackgroundBlure(false);
            }
        });
    }

    // 设置弹出popupwindow后屏幕的背景透明度
    private void setWindowBackgroundBlure(boolean blure) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = blure ? 0.8f : 1;
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.back, R.id.tv_right,R.id.defaultIv,R.id.area_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                addAddr();
                break;
            case R.id.defaultIv:
                if(isDefault==0){
                    isDefault = 1;
                    defaultIv.setImageResource(R.mipmap.sel_right);
                }else{
                    isDefault = 0;
                    defaultIv.setImageResource(R.mipmap.sel);
                }
                break;
            case R.id.area_ll:
                if (!TextUtils.isEmpty(userSP.getAddr()))
                    getAddress(userSP.getAddr());
                else
                    MainToast.showShortToast("暂无地区信息");
                break;
        }
    }

    private void addAddr(){
        String consignee = name.getText().toString();
        String tel = mobile.getText().toString();
        String addressStr = address.getText().toString();
        if(TextUtils.isEmpty(consignee)) {
            MainToast.showShortToast("请输入收货人名称");
            return;
        }

        if(TextUtils.isEmpty(tel)){
            MainToast.showShortToast("请输入联系电话");
            return;
        }

        if(TextUtils.isEmpty(provinceID)||TextUtils.isEmpty(cityID)||TextUtils.isEmpty(countyID)){
            MainToast.showShortToast("请选择所在地区");
            return;
        }

        if(TextUtils.isEmpty(addressStr)){
            MainToast.showShortToast("请输入详细地址");
            return;
        }
        showLoading();
        CommonApi.saveAddress(this,consignee, tel, provinceID, cityID, countyID, addressStr,isDefault+"","", new RequestCallback() {
            @Override
            public void success(Object success) {
                hideLoading();
                MainToast.showShortToast("添加成功");
                setResult(RESULT_OK);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=cityDiolog) {
            cityDiolog.dismiss();
            cityDiolog = null;
        }
    }
}
