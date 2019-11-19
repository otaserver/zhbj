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
public class TaskPager extends BasePager {
    public TaskPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("每天领红包，增加用户粘性");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);
        //修改页面标题
        tvTitle.setText("任务");
        btnMenu.setVisibility(View.VISIBLE);
    }
}
