package com.zjhj.sjxg.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by brain on 2017/5/24.
 */
public class LooperPagerAdapter extends PagerAdapter {

    private List<View> imageViewList ;

    public LooperPagerAdapter(List<View> imageViewList) {
        this.imageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        //返回实际要显示的图片数+2
        return imageViewList.size() + 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = (position - 1 + imageViewList.size())%imageViewList.size();
        View v = imageViewList.get(newPosition);
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(imageViewList.get(newPosition));
        return imageViewList.get(newPosition);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //注意不要remove  否则容易闪屏
        //  ((ViewPager)container).removeView((View) object);
    }

}
