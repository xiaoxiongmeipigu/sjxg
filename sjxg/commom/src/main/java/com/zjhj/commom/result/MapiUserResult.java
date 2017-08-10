package com.zjhj.commom.result;

import java.io.Serializable;

/**
 *Created by brain on 2016/6/14
 */
public class MapiUserResult extends MapiBaseResult {
    private String token;
    private String avatar;
    private String mobile;
    private String user_code;
    private String status;
    private String avatar_pic;
    private String nickname;
    private String last_login_shop;
    private MapiCityItemResult shopInfo;

    public MapiCityItemResult getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(MapiCityItemResult shopInfo) {
        this.shopInfo = shopInfo;
    }

    public String getLast_login_shop() {
        return last_login_shop;
    }

    public void setLast_login_shop(String last_login_shop) {
        this.last_login_shop = last_login_shop;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar_pic() {
        return avatar_pic;
    }

    public void setAvatar_pic(String avatar_pic) {
        this.avatar_pic = avatar_pic;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
