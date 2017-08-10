package com.zjhj.sjxg.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhj.commom.api.UserApi;
import com.zjhj.commom.result.MapiCityItemResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.sharedpreferences.UserSP;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.util.ControllerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.psd_et)
    EditText psdEt;
    @Bind(R.id.clear_psd)
    ImageView clearPsd;
    @Bind(R.id.show_psd)
    ImageView showPsd;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.clear_name)
    ImageView clearName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {

        back.setImageResource(R.mipmap.back_close);
        center.setText("登录");
        tvRight.setText("注册");

    }

    private void initListener() {
        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        psdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearPsd.setVisibility(View.VISIBLE);
                } else {
                    clearPsd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.back, R.id.tv_right, R.id.weixin_iv, R.id.protocol, R.id.login,R.id.clear_name,R.id.clear_psd,R.id.forget,R.id.show_psd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                ControllerUtil.go2Register();
                break;
            case R.id.weixin_iv:

                break;
            case R.id.protocol:

                break;
            case R.id.login:
                if(TextUtils.isEmpty(nameEt.getText())){
                    MainToast.showShortToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(psdEt.getText())){
                    MainToast.showShortToast("请输入密码");
                    return;
                }
                login();
                break;
            case R.id.clear_name:
                nameEt.setText("");
                break;
            case R.id.clear_psd:
                psdEt.setText("");
                break;
            case R.id.forget:
                ControllerUtil.go2Forget();
                break;
            case R.id.show_psd:
                if(psdEt.getTransformationMethod()== PasswordTransformationMethod.getInstance()){
                    //显示密码
                    psdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPsd.setImageResource(R.mipmap.show_psd);
                }else{
                    //否则隐藏密码
                    psdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPsd.setImageResource(R.mipmap.hid_psd);
                }
                break;
        }
    }

    private void login(){
        showLoading();
        UserApi.login(this, nameEt.getText().toString(), psdEt.getText().toString(), new RequestCallback<MapiUserResult>() {
            @Override
            public void success(MapiUserResult success) {
                hideLoading();
                MainToast.showShortToast("登录成功");
                userSP.saveUserBean(success);

                if(null!=success.getShopInfo()){
                    MapiCityItemResult cityItemResult = success.getShopInfo();
                    cityItemResult.setId(TextUtils.isEmpty(success.getLast_login_shop())?"":success.getLast_login_shop());
                    userSP.saveUserAddr(cityItemResult);
                }else{
                    MapiCityItemResult cityItemResult = new MapiCityItemResult();
                    cityItemResult.setId(TextUtils.isEmpty(success.getLast_login_shop())?"":success.getLast_login_shop());
                    userSP.saveUserAddr(cityItemResult);
                }

                ControllerUtil.go2Main();
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


}
