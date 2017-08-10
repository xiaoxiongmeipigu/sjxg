package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiAddrResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2016/6/16.
 */
public class UserApi extends BasicApi{

    public static void login(Activity activity, String mobile, String password, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("password",password);
        MapiUtil.getInstance().call(activity,loginUrl,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiUserResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiUserResult.class);
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
     * 获取验证码
     * @param activity
     * @param phone
     * @param callback
     * @param exceptionCallback
     */
    public static void getverify(Activity activity,String scene,String phone,String img_code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        params.put("mobile",phone);
        if(!TextUtils.isEmpty(img_code))
            params.put("img_code",img_code);
        MapiUtil.getInstance().call(activity,getverify,params,new MapiUtil.MapiSuccessResponse(){
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
     * 注册
     * @param activity
     * @param mobile
     * @param sms_code
     * @param password
     * @param callback
     * @param exceptionCallback
     */
    public static void registerreg(Activity activity,String mobile,String sms_code,String password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("mobile",mobile);
        params.put("sms_code",sms_code);
        params.put("password",password);
        MapiUtil.getInstance().call(activity,registerreg,params,new MapiUtil.MapiSuccessResponse(){
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
     * 忘记密码
     * @param activity
     * @param scene
     * @param mobile
     * @param sms_code
     * @param code
     * @param new_password
     * @param callback
     * @param exceptionCallback
     */
    public static void findpassword(Activity activity,String scene,String mobile,String sms_code,String code,String new_password,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        if(!TextUtils.isEmpty(mobile))
            params.put("mobile",mobile);
        if(!TextUtils.isEmpty(sms_code))
            params.put("sms_code",sms_code);
        if(!TextUtils.isEmpty(code))
            params.put("code",code);
        if(!TextUtils.isEmpty(new_password))
            params.put("new_password",new_password);
        MapiUtil.getInstance().call(activity,setpass,params,new MapiUtil.MapiSuccessResponse(){
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
     * 更换手机号
     * @param activity
     * @param scene
     * @param mobile
     * @param sms_code
     * @param code
     * @param callback
     * @param exceptionCallback
     */
    public static void replacephone(Activity activity,String scene,String mobile,String sms_code,String code,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        if(!TextUtils.isEmpty(mobile))
            params.put("mobile",mobile);
        if(!TextUtils.isEmpty(sms_code))
            params.put("sms_code",sms_code);
        if(!TextUtils.isEmpty(code))
            params.put("code",code);
        MapiUtil.getInstance().call(activity,replacephone,params,new MapiUtil.MapiSuccessResponse(){
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
     * 编辑个人资料
     * @param activity
     * @param avatar_pic
     * @param nickname
     * @param callback
     * @param exceptionCallback
     */
    public static void saveinfo(Activity activity,String avatar_pic,String nickname,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(avatar_pic))
            params.put("avatar_pic",avatar_pic);
        if(!TextUtils.isEmpty(nickname))
            params.put("nickname",nickname);
        MapiUtil.getInstance().call(activity,saveinfo,params,new MapiUtil.MapiSuccessResponse(){
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

}
