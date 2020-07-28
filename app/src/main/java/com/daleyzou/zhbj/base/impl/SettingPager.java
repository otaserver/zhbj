package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 首页
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("个人设置页面，包括定义感兴趣的商品，收藏夹，消息等");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);
        //修改页面标题
        tvTitle.setText("个人设置");
        btnMenu.setVisibility(View.GONE);
    }
}
