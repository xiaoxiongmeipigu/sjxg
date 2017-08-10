package com.zjhj.sjxg.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjhj.commom.api.CommonApi;
import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.widget.MainToast;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.activity.item.ItemListActivity;
import com.zjhj.sjxg.adapter.MainAdapter;
import com.zjhj.sjxg.adapter.menu.MenuParentAdapter;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.interfaces.RecyOnItemClickListener;
import com.zjhj.sjxg.interfaces.RecyOnMenuClickListener;
import com.zjhj.sjxg.util.JGJDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemMenuFrag extends BaseFrag {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IndexData> mList;
    MenuParentAdapter mAdapter;

    ItemListActivity activity;

    private List<MapiResourceResult> seasons;
    private List<MapiResourceResult> textures;

    private int oldSeasonPos = -1;
    private int oldtexturesPos = -1;

    public ItemMenuFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item_menu, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        load();
        activity = (ItemListActivity) getActivity();
        return view;
    }

    private void initView() {

        textures = new ArrayList<>();
        seasons = new ArrayList<>();
        mList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MenuParentAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnMenuClickListener(new RecyOnMenuClickListener() {
            @Override
            public void onMenuClick(View view,String type,int child) {

                if("0".equals(type)){
                    if(oldSeasonPos!=child){
                        if(oldSeasonPos>=0)
                            seasons.get(oldSeasonPos).setSel(false);
                        seasons.get(child).setSel(true);
                    }else{
                        seasons.get(child).setSel(!seasons.get(child).isSel());
                    }
                    oldSeasonPos = child;
                }else if("3".equals(type)){
                    if(oldtexturesPos!=child){
                        if(oldtexturesPos>=0)
                            seasons.get(oldtexturesPos).setSel(false);
                        seasons.get(child).setSel(true);
                    }else{
                        seasons.get(child).setSel(!seasons.get(child).isSel());
                    }
                    oldtexturesPos = child;
                }
            }
        });
    }

    public void load(){
        seasons.clear();
        seasons.addAll(JGJDataSource.getSeason());

        mList.add(new IndexData(0,"ITEM", seasons));
        mList.add(new IndexData(1,"DIVIDER",new Object()));
        mAdapter.notifyDataSetChanged();

        showLoading();
        CommonApi.listtexture(getActivity(), new RequestCallback<List<MapiResourceResult>>() {
            @Override
            public void success(List<MapiResourceResult> success) {
                hideLoading();
                if(null==success||success.isEmpty())
                    return;
                for(MapiResourceResult resourceResult : success){
                    resourceResult.setType("2");
                }

                textures.clear();
                textures.addAll(success);
                mList.add(new IndexData(2,"ITEM",textures));
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

    @OnClick({R.id.reset, R.id.complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                notifyData();
                break;
            case R.id.complete:

                String season = getSeason();
                String textures = getTextures();

                if(null!=onCompleteClickListener){
                    onCompleteClickListener.onCompleteClick(season,textures);
                }
                activity.closeDrawer();
                break;
        }
    }

    private String getSeason(){
        String str = "";
        for(MapiResourceResult resourceResult : seasons){
            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(str))
                    str += resourceResult.getId();
                else
                    str += ","+resourceResult.getId();
            }
        }
        return str;
    }

    private String getTextures(){
        String str = "";
        for(MapiResourceResult resourceResult : textures){

            if(resourceResult.isSel()){
                if(TextUtils.isEmpty(str))
                    str += resourceResult.getId();
                else
                    str += ","+resourceResult.getId();
            }

        }
        return str;
    }

    private void notifyData(){
        mList.clear();
        mAdapter.notifyDataSetChanged();
        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public OnCompleteClickListener onCompleteClickListener;

    public interface OnCompleteClickListener {
        void onCompleteClick(String season,String texture);
    }

    public void setOnCompleteClickListener(OnCompleteClickListener onCompleteClickListener){
        this.onCompleteClickListener = onCompleteClickListener;
    }

}
