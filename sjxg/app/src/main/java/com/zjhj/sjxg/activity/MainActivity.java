package com.zjhj.sjxg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.base.RequestCode;
import com.zjhj.sjxg.fragment.HomeFragment;
import com.zjhj.sjxg.fragment.PersonFragment;
import com.zjhj.sjxg.fragment.SortFragment;
import com.zjhj.sjxg.fragment.purcase.PurcaseFragment;
import com.zjhj.sjxg.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.radio_home)
    RadioButton radioHome;
    @Bind(R.id.radio_sort)
    RadioButton radioSort;
    @Bind(R.id.radio_scan)
    RadioButton radioScan;
    @Bind(R.id.radio_person)
    RadioButton radioPerson;
    @Bind(R.id.radio_purcase)
    RadioButton radioPurcase;

    private BaseFrag[] fragments;
    private int index = 0;
    private long exitTime = 0;

    private RadioButton[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!userSP.checkLogin()){
            ControllerUtil.go2Login();
            finish();
        }else{
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            initView();
            initListener();
            load();
        }

    }

    private void initView() {

        fragments = new BaseFrag[4];
        fragments[0] = new HomeFragment();
        fragments[1] = new SortFragment();
        fragments[2] = new PersonFragment();
        fragments[3] = new PurcaseFragment();

        buttons = new RadioButton[4];
        buttons[0] = radioHome;
        buttons[1] = radioSort;
        buttons[2] = radioPerson;
        buttons[3] = radioPurcase;

        selectTab();

    }

    private void initListener() {

    }

    public void load() {

    }

    private void selectTab() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFrag frag : fragments) {
            if (!frag.isAdded())
                transaction.add(R.id.content, frag);
            transaction.hide(frag);
        }
        transaction.show(fragments[index]);
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.radio_home, R.id.radio_sort, R.id.radio_scan, R.id.radio_person, R.id.radio_purcase})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_home:
                index = 0;
                selectTab();
                break;
            case R.id.radio_sort:
                index = 1;
                selectTab();
                break;
            case R.id.radio_scan:
                Intent intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(RequestCode.REQUEST_SCAN_MODE,RequestCode.REQUEST_SCAN_MODE_ALL_MODE);
                startActivity(intent);

               /* intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
                startActivity(intent);
                break;
            case  R.id.scan_bar_code://扫描条形码
                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
                startActivity(intent);
                break;
            case  R.id.scan_code://扫描条形码或者二维码
                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivity(intent);*/

                break;
            case R.id.radio_person:
                index = 2;
                selectTab();
                break;
            case R.id.radio_purcase:
                index = 3;
                selectTab();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null!=intent){
            int type = intent.getIntExtra("type",0);
            if(3==type){
                ControllerUtil.go2Login();
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
