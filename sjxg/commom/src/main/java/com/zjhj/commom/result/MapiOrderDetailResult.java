package com.zjhj.commom.result;

import java.util.List;

/**
 * Created by brain on 2017/7/28.
 */
public class MapiOrderDetailResult extends MapiBaseResult{

    private List<MapiCarResult> list;

    public List<MapiCarResult> getList() {
        return list;
    }

    public void setList(List<MapiCarResult> list) {
        this.list = list;
    }
}
