package com.zjhj.sjxg.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.util.AmapUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brain on 2017/8/8.
 */
public class MapsDialog extends Dialog {

    @Bind(R.id.gaode_tv)
    TextView gaodeTv;
    @Bind(R.id.baidu_tv)
    TextView baiduTv;
    @Bind(R.id.cancel)
    TextView cancel;

    private BaseActivity mActivity;
    String longitude = "";
    String latitude = "";


    public MapsDialog(Context context, int theme) {
        super(context,theme);
        mActivity = (BaseActivity) context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_maps);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//默认黑色背景，设置背景为透明色，小米出现黑色背景
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void showDialog() {
        super.show();
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.gaode_tv, R.id.baidu_tv, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gaode_tv:
                try {
                    if(!TextUtils.isEmpty(latitude)&&!TextUtils.isEmpty(longitude)){
                        if (AmapUtil.isInstallByRead("com.autonavi.minimap")){
                            AmapUtil.goToGaodeNaviActivity(mActivity,"三季小购","",latitude,longitude,"","");
                        }else{
                            MainToast.showShortToast("没有安装高德地图客户端，请先下载该地图应用");
                        }
                    }
                }catch (Exception e){

                }


                break;
            case R.id.baidu_tv:
                try {
                    if(!TextUtils.isEmpty(latitude)&&!TextUtils.isEmpty(longitude)){
                        if (AmapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                            AmapUtil.goToBaiduNaviActivity(mActivity, 0, 0, Double.parseDouble(latitude), Double.parseDouble(longitude), "driving");
                        }else{
                            MainToast.showShortToast("没有安装百度地图客户端，请先下载该地图应用");
                        }
                    }
                }catch (Exception e){

                }

                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

}
