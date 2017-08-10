package com.zjhj.sjxg.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.LoginActivity;
import com.zjhj.sjxg.activity.MainActivity;
import com.zjhj.sjxg.adapter.MainAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.util.ControllerUtil;
import com.zjhj.sjxg.widget.MainAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends BaseFrag {


    List<IndexData> mList = new ArrayList<>();
    MainAdapter mAdapter;
    @Bind(R.id.image)
    SimpleDraweeView image;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;

    MainAlertDialog callDialog;
    MainAlertDialog exitDialog;
    String service_tel = "";

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MapiUserResult userResult = userSP.getUserBean();
        if(null!=userResult){
            phoneTv.setText(TextUtils.isEmpty(userResult.getMobile())?"":userResult.getMobile());
            nameTv.setText(TextUtils.isEmpty(userResult.getNickname())?"":userResult.getNickname());
        }
    }

    private void initView() {

        callDialog = new MainAlertDialog(getActivity());
        exitDialog = new MainAlertDialog(getActivity());
        exitDialog.setLeftBtnText("退出").setRightBtnText("取消").setTitle("确认退出？");

    }

    private void initListener() {
        callDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
            }
        });

        callDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service_tel));
                startActivity(intent);
                callDialog.dismiss();
            }
        });

        exitDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSP.clearUserData();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("type", 3);
                startActivity(i);
                exitDialog.dismiss();
            }
        });

        exitDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
            }
        });
    }

    public void load() {
        service_tel = "111";
        callDialog.setLeftBtnText("取消").setRightBtnText("呼叫").setTitle(service_tel);
    }

    @OnClick({R.id.edit_iv, R.id.uncomplete_order, R.id.complete_order, R.id.collect_ll, R.id.modify_ll, R.id.tutorial_ll, R.id.service_ll, R.id.exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_iv:
                ControllerUtil.go2PersonEdit();
                break;
            case R.id.uncomplete_order:
                ControllerUtil.go2UnComOrder();
                break;
            case R.id.complete_order:
                ControllerUtil.go2ComOrder();
                break;
            case R.id.collect_ll:
                ControllerUtil.go2CollectList();
                break;
            case R.id.modify_ll:
                ControllerUtil.go2ModifyPsd();
                break;
            case R.id.tutorial_ll:
                break;
            case R.id.service_ll:
                if (!TextUtils.isEmpty(service_tel)) {
                    if(null!=callDialog)
                        callDialog.show();
                }
                break;
            case R.id.exit:
                if(null!=exitDialog)
                    exitDialog.show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        if (null != callDialog && callDialog.isShowing()) {
            callDialog.dismiss();
            callDialog = null;
        }

        if (null != exitDialog && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }

    }

}
