package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiAddrResult;
import com.zjhj.commom.result.MapiImageResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.sharedpreferences.UserSP;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/2/24.
 */
public class CommonApi extends BasicApi{

    /**
     * 上传图片
     * @param activity
     * @param file
     * @param callback
     * @param exceptionCallback
     */
    public static void
    uploadImage(Activity activity, File file, final RequestCallback callback, final RequestExceptionCallback exceptionCallback) {
        MapiUtil.getInstance().uploadFile(activity, uploadimg, file, new MapiUtil.MapiSuccessResponse() {
            @Override
            public void success(JSONObject json) {
                DebugLog.i(json.toString());
                MapiImageResult imageResult = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiImageResult.class);
                callback.success(imageResult);
            }
        }, new MapiUtil.MapiFailResponse() {
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code, failMessage);
            }
        });
    }

    /**
     * 地址列表
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void useraddress(Activity activity, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,useraddress,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiAddrResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiAddrResult.class);
                if(null!=result)
                    callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 编辑地址（或新增）
     * @param activity
     * @param consignee
     * @param mobile
     * @param province_id
     * @param city_id
     * @param area_id
     * @param address
     * @param callback
     * @param exceptionCallback
     */
    public static void saveAddress(Activity activity,String consignee,String mobile,String province_id,String city_id,String area_id,
                                  String address,String is_default,String id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("consignee",consignee);
        params.put("mobile",mobile);
        params.put("province_id",province_id);
        params.put("city_id",city_id);
        params.put("area_id",area_id);
        params.put("address",address);
        params.put("is_default",is_default);
        if(!TextUtils.isEmpty(id))
            params.put("add_id",id);
        MapiUtil.getInstance().call(activity,saveAddress,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 省份列表
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void defaultregion(Activity activity, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,defaultregion,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 删除地址
     * @param activity
     * @param address_id
     * @param callback
     * @param exceptionCallback
     */
    public static void deladdress(Activity activity,String address_id, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("add_id",address_id);
        MapiUtil.getInstance().call(activity,deladdress,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 设置默认地址
     * @param activity
     * @param add_id
     * @param callback
     * @param exceptionCallback
     */
    public static void setdefault(Activity activity,String add_id, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("add_id",add_id);
        MapiUtil.getInstance().call(activity,setdefault,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 材质
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void listtexture(Activity activity, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,listtexture,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiResourceResult> result = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),MapiResourceResult.class);
                if(null!=result)
                    callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

}
