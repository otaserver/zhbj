package com.daleyzou.zhbj.base.impl.menu;

import android.app.Activity;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 菜单详情页基类
 * pager里包含了activity和view！
 * View表示屏幕上的一块矩形区域，负责绘制这个区域和事件处理。View是所有widget类的基类，
 * 定义了抽象方法，initVIew()
 */
public abstract class BaseDetailPager {
    public Activity mActivity;
    public View mRootView;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BaseDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局
    public abstract View initView();

    //初始化数据
    public void initData(String key){}

    public abstract ViewPager getViewPager();
}
