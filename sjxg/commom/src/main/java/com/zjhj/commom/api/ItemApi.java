package com.zjhj.commom.api;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjhj.commom.result.MapiAddrResult;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiItemResult;
import com.zjhj.commom.result.MapiOrderResult;
import com.zjhj.commom.result.MapiResourceResult;
import com.zjhj.commom.result.MapiShopResult;
import com.zjhj.commom.result.MapiUserResult;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.commom.util.MapiUtil;
import com.zjhj.commom.util.RequestCallback;
import com.zjhj.commom.util.RequestExceptionCallback;
import com.zjhj.commom.util.RequestPageCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brain on 2017/5/18.
 */
public class ItemApi extends BasicApi{

    /**
     * 首页接口
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void main(Activity activity,String shop_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("shop_id",shop_id);
        MapiUtil.getInstance().call(activity,indexselling,params,new MapiUtil.MapiSuccessResponse(){
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
     * 顶级分类
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void topcategory(Activity activity, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,topcategory,params,new MapiUtil.MapiSuccessResponse(){
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

    /**
     * 顶级分类
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void secondary(Activity activity,String pid, final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("pid",pid);
        MapiUtil.getInstance().call(activity,secondary,params,new MapiUtil.MapiSuccessResponse(){
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

    /**
     * 商品列表
     * @param activity
     * @param page
     * @param limit
     * @param type
     * @param search
     * @param sort
     * @param season
     * @param texture
     * @param callback
     * @param exceptionCallback
     */
    public static void listindex(Activity activity,String page,String limit,String type,String search,String sort,String season,String texture,
                                 String class_id,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        if(!TextUtils.isEmpty(limit))
            params.put("limit",limit);
        if(!TextUtils.isEmpty(type))
            params.put("type",type);
        if(!TextUtils.isEmpty(search))
            params.put("search",search);
        if(!TextUtils.isEmpty(sort))
            params.put("sort",sort);
        if(!TextUtils.isEmpty(season))
            params.put("season",season);
        if(!TextUtils.isEmpty(texture))
            params.put("texture",texture);
        if(!TextUtils.isEmpty(class_id))
            params.put("class_id",class_id);
        MapiUtil.getInstance().call(activity,listindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 商品详情
     * @param activity
     * @param s_id
     *                  商品ID
     * @param callback
     * @param exceptionCallback
     */
    public static void listdetails(Activity activity,String s_id ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("s_id",s_id);
        MapiUtil.getInstance().call(activity,listdetails,params,new MapiUtil.MapiSuccessResponse(){
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
     * 添加收藏
     * @param activity
     * @param s_id
     * @param callback
     * @param exceptionCallback
     */
    public static void collectionadd(Activity activity,String s_id ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("s_id",s_id);
        MapiUtil.getInstance().call(activity,collectionadd,params,new MapiUtil.MapiSuccessResponse(){
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
     * 删除收藏
     * @param activity
     * @param c_id
     * @param callback
     * @param exceptionCallback
     */
    public static void collectiondel(Activity activity,String c_id ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("c_id",c_id);
        MapiUtil.getInstance().call(activity,collectiondel,params,new MapiUtil.MapiSuccessResponse(){
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
     * 加入购物车
     * @param activity
     * @param s_id
     * @param spec_id
     * @param num
     * @param callback
     * @param exceptionCallback
     */
    public static void joincar(Activity activity,String s_id,String spec_id,String num,String special_id,String type,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("s_id",s_id);
        params.put("spec_id",spec_id);
        params.put("num",num);
        if(!TextUtils.isEmpty(special_id))
            params.put("special_id",special_id);
        if(!TextUtils.isEmpty(type))
            params.put("type",type);
        MapiUtil.getInstance().call(activity,joincar,params,new MapiUtil.MapiSuccessResponse(){
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
     * 购物车列表
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void carlist(Activity activity,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,carcar,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                Gson gson = new Gson();
                List<MapiCarResult> result = gson.fromJson(json.getJSONArray("data").toJSONString(), new TypeToken<List<MapiCarResult>>(){}.getType());
                if(null==result){
                    result = new ArrayList<>();
                    callback.success(result);
                }else
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
     * 编辑购物车
     * @param activity
     * @param car_id
     * @param num
     * @param callback
     * @param exceptionCallback
     */
    public static void editcart(Activity activity,String car_id,String num,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("car_id",car_id);
        params.put("num",num);
        MapiUtil.getInstance().call(activity,cardit,params,new MapiUtil.MapiSuccessResponse(){
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
     * 购物车删除
     * @param activity
     * @param car_id
     * @param callback
     * @param exceptionCallback
     */
    public static void cartdel(Activity activity,String car_id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("car_id",car_id);
        MapiUtil.getInstance().call(activity,cardel,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                callback.success(json);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code,String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 会员收藏
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void collectionindex(Activity activity,String page,String limit,
                                 final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        if(!TextUtils.isEmpty(limit))
            params.put("limit",limit);
        MapiUtil.getInstance().call(activity,collectionindex,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 订单提交获取用户地址
     * @param activity
     * @param addr_id
     * @param shop_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderaddr(Activity activity,String addr_id,String shop_id,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("addr_id",addr_id);
        params.put("shop_id",shop_id);
        MapiUtil.getInstance().call(activity,orderaddr,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiAddrResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiAddrResult.class);
                callback.success(result);
            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code,String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 附近门店
     * @param activity
     * @param longitude
     * @param latitude
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void nearbyshop(Activity activity,String longitude,String latitude,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("page",page);
        if(!TextUtils.isEmpty(limit))
            params.put("limit",limit);
        MapiUtil.getInstance().call(activity,nearbyshop,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiShopResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiShopResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 常用门店
     * @param activity
     * @param callback
     * @param exceptionCallback
     */
    public static void commonshop(Activity activity,final RequestCallback callback,final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        MapiUtil.getInstance().call(activity,commonshop,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                Gson gson = new Gson();
                List<MapiShopResult> result = gson.fromJson(json.getJSONArray("data").toJSONString(), new TypeToken<List<MapiShopResult>>(){}.getType());
                if(null==result){
                    result = new ArrayList<>();
                    callback.success(result);
                }else
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
     * 预购
     * @param activity
     * @param scene
     * @param shop_id
     * @param order_type
     * @param addr_id
     * @param car_id
     * @param s_id
     * @param spec_id
     * @param special_id
     * @param num
     * @param callback
     * @param exceptionCallback
     */
    public static void placeorder(Activity activity,String scene,String shop_id,String order_type,String addr_id,String car_id,String s_id,String spec_id,String special_id,String num,
                                  final RequestCallback callback,final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("scene",scene);
        params.put("shop_id",shop_id);
        params.put("order_type",order_type);
        if(!TextUtils.isEmpty(addr_id))
            params.put("addr_id",addr_id);
        if(!TextUtils.isEmpty(car_id))
            params.put("car_id",car_id);
        if(!TextUtils.isEmpty(s_id))
            params.put("s_id",s_id);
        if(!TextUtils.isEmpty(spec_id))
            params.put("spec_id",spec_id);
        if(!TextUtils.isEmpty(special_id))
            params.put("special_id",special_id);
        if(!TextUtils.isEmpty(num))
            params.put("num",num);
        MapiUtil.getInstance().call(activity,placeorder,params,new MapiUtil.MapiSuccessResponse(){
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
     * 限时抢购列表页
     * @param activity
     * @param page
     * @param limit
     * @param callback
     * @param exceptionCallback
     */
    public static void flashlist(Activity activity,String page,String limit,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("page",page);
        if(!TextUtils.isEmpty(limit))
            params.put("limit",limit);
        MapiUtil.getInstance().call(activity,flashlist,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiItemResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiItemResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 限时抢购详情
     * @param activity
     * @param s_id
     *                  商品ID
     * @param callback
     * @param exceptionCallback
     */
    public static void flashdetails(Activity activity,String s_id ,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){

        Map<String,String> params = new HashMap<>();
        params.put("s_id",s_id);
        MapiUtil.getInstance().call(activity,flashdetails,params,new MapiUtil.MapiSuccessResponse(){
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
     * 个人中心-订单
     * @param activity
     * @param page
     * @param limit
     * @param confirm
     * @param callback
     * @param exceptionCallback
     */
    public static void userorder(Activity activity,String page,String limit,String confirm,final RequestPageCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("confirm",confirm);
        params.put("page",page);
        if(!TextUtils.isEmpty(limit))
            params.put("limit",limit);
        MapiUtil.getInstance().call(activity,userorder,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                List<MapiOrderResult> result = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("list").toJSONString(),MapiOrderResult.class);
                Integer count = json.getJSONObject("data").getInteger("page_count");
                if(null!=count){
                    callback.success(count,result);
                }

            }
        },new MapiUtil.MapiFailResponse(){
            @Override
            public void fail(Integer code, String failMessage) {
                exceptionCallback.error(code,failMessage);
            }
        });
    }

    /**
     * 取消订单
     * @param activity
     * @param order_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderdel(Activity activity,String order_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        MapiUtil.getInstance().call(activity,orderdel,params,new MapiUtil.MapiSuccessResponse(){
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
     * 确认订单
     * @param activity
     * @param order_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderconfirm(Activity activity,String order_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        MapiUtil.getInstance().call(activity,orderconfirm,params,new MapiUtil.MapiSuccessResponse(){
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
     * 个人中心-订单详情
     * @param activity
     * @param order_id
     * @param callback
     * @param exceptionCallback
     */
    public static void orderdetails(Activity activity,String order_id,final RequestCallback callback, final RequestExceptionCallback exceptionCallback){
        Map<String,String> params = new HashMap<>();
        params.put("order_id",order_id);
        MapiUtil.getInstance().call(activity,orderdetails,params,new MapiUtil.MapiSuccessResponse(){
            @Override
            public void success(JSONObject json) {
                DebugLog.i("json="+json);
                MapiOrderResult result = JSONObject.parseObject(json.getJSONObject("data").toJSONString(),MapiOrderResult.class);
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
