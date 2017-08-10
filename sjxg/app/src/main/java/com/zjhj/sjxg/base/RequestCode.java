package com.zjhj.sjxg.base;

/**
 * Created by brain on 2016/7/3.
 */
public class RequestCode {
    public static final int add_addr = 0x01;
    public static final int CAMERA = 0x03;
    public static final int PICTURE = 0x04;
    public static final int SEARCH_HIS = 0x05;
    public static final int SHOP_LIST = 0x06;
    public static final int SEARCH_ADDR = 0x07;
    public static final int SEARCH_RESULT = 0x08;
    public static final int LOCATION_QUEST = 0x09;
    public static final int sel_addr = 0x10;

    /**
     * 二维码请求的type
     */
    public static final String REQUEST_SCAN_TYPE="type";
    /**
     * 普通类型，扫完即关闭
     */
    public static final int REQUEST_SCAN_TYPE_COMMON=0;
    /**
     * 服务商登记类型，扫描
     */
    public static final int REQUEST_SCAN_TYPE_REGIST=1;


    /**
     * 扫描类型
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     * 二维码：REQUEST_SCAN_MODE_QRCODE_MODE
     *
     */
    public static final String REQUEST_SCAN_MODE="ScanMode";
    /**
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     */
    public static final int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
    /**
     * 二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_QRCODE_MODE = 0X200;
    /**
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    public static final int REQUEST_SCAN_MODE_ALL_MODE = 0X300;

}
