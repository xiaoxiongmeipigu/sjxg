package com.zjhj.sjxg.util;


import com.zjhj.commom.result.MapiResourceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brain on 2016/7/29.
 */
public class JGJDataSource {


    public static final String TYPE_ONE = 0x01+"";
    public static final String TYPE_TWO = 0x02+"";
    public static final String TYPE_THREE = 0x03+"";
    public static final String TYPE_FOUR = 0x04+"";
    public static final String TYPE_FIVE = 0x05+"";
    public static final String TYPE_SIX = 0x06+"";
    public static final String TYPE_SEVEN = 0x07+"";
    public static final String TYPE_EIGHT = 0x08+"";

    public static List<MapiResourceResult> getArea(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("0","全部"));
        list.add(new MapiResourceResult("0","丽江"));
        list.add(new MapiResourceResult("0","马尔代夫"));
        list.add(new MapiResourceResult("0","九寨沟"));
        list.add(new MapiResourceResult("0","三亚"));
        list.add(new MapiResourceResult("0","普陀山"));
        list.add(new MapiResourceResult("0","乌镇"));
        list.add(new MapiResourceResult("0","九华山"));
        list.add(new MapiResourceResult("0","成都"));
        list.add(new MapiResourceResult("0","五台山"));
        list.add(new MapiResourceResult("0","凤凰古城"));

        return list;
    }

    /**
     * 获取首页功能名称
     * @return
     */
    public static List<MapiResourceResult> getMainResource(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult(TYPE_ONE,"新品上市"));
        list.add(new MapiResourceResult(TYPE_TWO,"当季热品"));
        list.add(new MapiResourceResult(TYPE_THREE,"促销折扣"));
        list.add(new MapiResourceResult(TYPE_FOUR,"限时抢购"));
        list.add(new MapiResourceResult(TYPE_FIVE,"服装"));
        list.add(new MapiResourceResult(TYPE_SIX,"美妆"));
        list.add(new MapiResourceResult(TYPE_SEVEN,"电器"));
        list.add(new MapiResourceResult(TYPE_EIGHT,"百货"));
        return list;
    }

    /**
     * 获取首页功能名称
     * @return
     */
    public static List<MapiResourceResult> getSeason(){
        List<MapiResourceResult> list = new ArrayList<>();
        list.add(new MapiResourceResult("1","春","1"));
        list.add(new MapiResourceResult("2","夏","1"));
        list.add(new MapiResourceResult("3","秋","1"));
        list.add(new MapiResourceResult("4","冬","1"));
        return list;
    }

}
