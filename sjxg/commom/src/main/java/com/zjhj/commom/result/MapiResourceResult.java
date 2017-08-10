package com.zjhj.commom.result;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by brain on 2016/7/22.
 */
public class MapiResourceResult extends MapiBaseResult {
    private String ZD_ID;
    private String remark;
    private int version;
    private String poster;
    private String title;

    private String e_id;
    private String info;
    private String postage;
    private String icon;

    private boolean isSel;
    private String img;
    private String img_url;
    private String url;
    private String pic_url;
    private String use_begin_time;
    private String use_end_time;

    private String date;
    private String guide_name;
    private String merchant_count;

    private String pic;
    private String p_id;
    private String cover_pic;
    private String type;
    private String color_name;
    private String size_name;
    private String fare;
    private String s_id;

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private List<MapiResourceResult> list;

    public List<MapiResourceResult> getList() {
        return list;
    }

    public void setList(List<MapiResourceResult> list) {
        this.list = list;
    }

    public String getGuide_name() {
        return guide_name;
    }

    public void setGuide_name(String guide_name) {
        this.guide_name = guide_name;
    }

    public String getMerchant_count() {
        return merchant_count;
    }

    public void setMerchant_count(String merchant_count) {
        this.merchant_count = merchant_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUse_begin_time() {
        return use_begin_time;
    }

    public void setUse_begin_time(String use_begin_time) {
        this.use_begin_time = use_begin_time;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSel() {
        return isSel;
    }

    public void setSel(boolean sel) {
        isSel = sel;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public MapiResourceResult(){

    }

    public MapiResourceResult(String id,String name){
        this.id = id;
        this.name = name;
    }

    public MapiResourceResult(String id,String name,String type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getZD_ID() {
        return ZD_ID;
    }

    public void setZD_ID(String ZD_ID) {
        this.ZD_ID = ZD_ID;
    }

    //重写hashcode和equals使得根据id来判断是否是同一个bean

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapiResourceResult)) {
            return false;
        }
        MapiResourceResult other = (MapiResourceResult) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
