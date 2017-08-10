package com.zjhj.commom.result;

import java.util.List;

/**
 * Created by brain on 2017/7/27.
 */
public class MapiOrderResult extends MapiBaseResult{

    private String total_amount;
    private String express_fee;
    private String goods_total;
    private String date;
    private String status;
    private String cover_pic;
    private String trade;
    private String addtime;
    private String order_type;
    private String count;
    private String color_name;
    private String size_name;
    private String price;
    private String s_discount;
    private String s_price;
    private String shop_mobile;
    private String shop_name;
    private String shop_address;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private MapiAddrResult address;

    public MapiAddrResult getAddress() {
        return address;
    }

    public void setAddress(MapiAddrResult address) {
        this.address = address;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_mobile() {
        return shop_mobile;
    }

    public void setShop_mobile(String shop_mobile) {
        this.shop_mobile = shop_mobile;
    }

    public String getS_price() {
        return s_price;
    }

    public void setS_price(String s_price) {
        this.s_price = s_price;
    }

    public String getS_discount() {
        return s_discount;
    }

    public void setS_discount(String s_discount) {
        this.s_discount = s_discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(String express_fee) {
        this.express_fee = express_fee;
    }

    public String getGoods_total() {
        return goods_total;
    }

    public void setGoods_total(String goods_total) {
        this.goods_total = goods_total;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private List<MapiCarResult> list;

    public List<MapiCarResult> getList() {
        return list;
    }

    public void setList(List<MapiCarResult> list) {
        this.list = list;
    }
}
