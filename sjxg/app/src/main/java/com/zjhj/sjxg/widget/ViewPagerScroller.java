package com.zjhj.sjxg.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 设置ViewPager的滑动速度
 * @author brain
 * @since 2015.7.16
 */
public class ViewPagerScroller extends Scroller{

	private int scrollDuration = 2000;//滑动速度

	public ViewPagerScroller(Context context) {
		super(context);
	}


	public ViewPagerScroller(Context context,Interpolator interpolator){
		super(context,interpolator);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy,scrollDuration);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, scrollDuration);
	}

	public int getScrollDuration() {
		return scrollDuration;
	}


	/**
	 * 设置滑动速度
	 * @param scrollDuration
	 */
	public void setScrollDuration(int scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	/**
	 * 通过反射设置ViewPager滑动速度
	 * @param viewPager
	 */
	public void initViewPagerScroll(ViewPager viewPager){
		try { 

			Field mScroller = ViewPager.class.getDeclaredField("mScroller"); 

			mScroller.setAccessible(true); 

			mScroller.set(viewPager, this); 

		} catch(Exception e) { 

			e.printStackTrace(); 

		} 
	}

}
