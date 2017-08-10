package com.zjhj.sjxg.util;

import android.content.Intent;

import com.zjhj.commom.application.AppContext;
import com.zjhj.commom.result.MapiCarResult;
import com.zjhj.commom.result.MapiCartItemResult;
import com.zjhj.sjxg.activity.BandPhoneActivity;
import com.zjhj.sjxg.activity.ForgetActivity;
import com.zjhj.sjxg.activity.LoginActivity;
import com.zjhj.sjxg.activity.MainActivity;
import com.zjhj.sjxg.activity.ModifyPhoneActivity;
import com.zjhj.sjxg.activity.ModifyPsdActivity;
import com.zjhj.sjxg.activity.RegisterActivity;
import com.zjhj.sjxg.activity.SearchActivity;
import com.zjhj.sjxg.activity.SetPsdActivity;
import com.zjhj.sjxg.activity.addr.AddAddrActivity;
import com.zjhj.sjxg.activity.addr.AddrListActivity;
import com.zjhj.sjxg.activity.balance.BalanceActivity;
import com.zjhj.sjxg.activity.collect.CollectListActivity;
import com.zjhj.sjxg.activity.item.ItemDetailActivity;
import com.zjhj.sjxg.activity.item.ItemListActivity;
import com.zjhj.sjxg.activity.limit.ItemLimitActivity;
import com.zjhj.sjxg.activity.limit.LimitDetailActivity;
import com.zjhj.sjxg.activity.order.ComOrderActivity;
import com.zjhj.sjxg.activity.order.OrderDetailActivity;
import com.zjhj.sjxg.activity.order.UnComOrderActivity;
import com.zjhj.sjxg.activity.person.PersonEditActivity;
import com.zjhj.sjxg.activity.purcase.PurcaseActivity;
import com.zjhj.sjxg.activity.shop.ShopListActivity;
import com.zjhj.sjxg.activity.webview.WebviewControlActivity;

import java.util.ArrayList;


/**
 * Created by brain on 2016/6/22.
 */
public class ControllerUtil {

    /**
     * 我的下单列表
     */
   /* public static void go2IndentOrder() {
        Intent intent = new Intent(AppContext.getInstance(), IndentOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }*/

    /**
     * 首页
     */
    public static void go2Main() {
        Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 登录
     */
    public static void go2Login() {
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }


        /**
         * h5页面
         */
    public static void go2WebView(String url, String title,String shareTitle,String shareContext,String shareLOGO, boolean isShare) {
        Intent intent = new Intent(AppContext.getInstance(), WebviewControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("isShare", isShare);
        intent.putExtra("shareTitle", shareTitle);

        intent.putExtra("shareContext", shareContext);
        intent.putExtra("shareLOGO", shareLOGO);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 注册
     */
    public static void go2Register() {
        Intent intent = new Intent(AppContext.getInstance(), RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 忘记密码
     */
    public static void go2Forget() {
        Intent intent = new Intent(AppContext.getInstance(), ForgetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 设置密码
     */
    public static void go2SetPsd(String code) {
        Intent intent = new Intent(AppContext.getInstance(), SetPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code",code);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 产品列表
     */
    public static void go2ItemList(String type) {
        Intent intent = new Intent(AppContext.getInstance(), ItemListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fromType",type);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 产品详情
     */
    public static void go2ItemDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), ItemDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 限购详情
     */
    public static void go2LimitDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), LimitDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 预订页面
     */
    public static void go2Balance(ArrayList<MapiCartItemResult> itemList, ArrayList<MapiCarResult> carList,String scene) {
        Intent intent = new Intent(AppContext.getInstance(), BalanceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("itemList",itemList);
        intent.putExtra("carList",carList);
        intent.putExtra("scene",scene);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 个人资料
     */
    public static void go2PersonEdit() {
        Intent intent = new Intent(AppContext.getInstance(), PersonEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 收货地址列表
     */
    public static void go2AddrList() {
        Intent intent = new Intent(AppContext.getInstance(), AddrListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 新增收货地址
     */
    public static void go2AddAddr() {
        Intent intent = new Intent(AppContext.getInstance(), AddAddrActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改绑定手机号
     */
    public static void go2ModifyPhone() {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPhoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改绑定手机号第二步
     */
    public static void go2BandPhone(String code) {
        Intent intent = new Intent(AppContext.getInstance(), BandPhoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code",code);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 收藏列表
     */
    public static void go2CollectList() {
        Intent intent = new Intent(AppContext.getInstance(), CollectListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 修改密码
     */
    public static void go2ModifyPsd() {
        Intent intent = new Intent(AppContext.getInstance(), ModifyPsdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 门店列表
     */
    public static void go2ShopList() {
        Intent intent = new Intent(AppContext.getInstance(), ShopListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 未完成订单
     */
    public static void go2UnComOrder() {
        Intent intent = new Intent(AppContext.getInstance(), UnComOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 完成订单
     */
    public static void go2ComOrder() {
        Intent intent = new Intent(AppContext.getInstance(), ComOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void go2OrderDetail(String id) {
        Intent intent = new Intent(AppContext.getInstance(), OrderDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 搜索
     */
    public static void go2Search() {
        Intent intent = new Intent(AppContext.getInstance(), SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 购物车
     */
    public static void go2Purcase() {
        Intent intent = new Intent(AppContext.getInstance(), PurcaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

    /**
     * 限时抢购列表
     */
    public static void go2ItemLimit() {
        Intent intent = new Intent(AppContext.getInstance(), ItemLimitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppContext.getInstance().startActivity(intent);
    }

}
