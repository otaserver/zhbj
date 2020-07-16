package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.daleyzou.zhbj.base.BasePager;

/**
 * 首页
 */
public class VideoPager extends BasePager {

    public VideoPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("短视频");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
//
        flContent.addView(view);
        //修改页面标题
        tvTitle.setText("短视频");
        btnMenu.setVisibility(View.GONE);

    }

}
