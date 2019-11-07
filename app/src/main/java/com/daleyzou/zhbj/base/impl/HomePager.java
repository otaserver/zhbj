package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daleyzou.zhbj.base.BaseMenuDetailPager;
import com.daleyzou.zhbj.base.BasePager;
import com.daleyzou.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.daleyzou.zhbj.domain.NewsMenu;
import com.daleyzou.zhbj.global.GlobalConstants;
import com.daleyzou.zhbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 首页
 */
public class HomePager extends BasePager {
    //菜单详情页集合
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    private NewsMenu mNewsData;
    //为日志Log定义tag标签
    private static final String TAG = "HomePager";

    public HomePager(Activity activity) {
        super(activity);
        Log.d(TAG, "HomePager被创建！");
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("显示最新的特价信息");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);

        //修改页面标题
        tvTitle.setText("首页");

        //隐藏菜单按钮
        btnMenu.setVisibility(View.GONE);

        //先判断有没有缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            Log.d(TAG, "发现缓存啦！");
            processData(cache);
        } else {
            //请求服务器获取数据
            getDataFromServer();
        }
    }


    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //请求成功
                String result = responseInfo.result;
                Log.d(TAG, "服务器返回结果：" + result);
                processData(result);

                //写缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "从服务器取回内容失败！", e.getCause());
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processData(String json) {
        // Gson为google处理json的类库。
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);
        Log.d(TAG, "Gson对象:" + mNewsData.toString());

        //初始化菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));

        // 将新闻菜单详情页设为默认界面
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     *
     * @param position
     */
    public void setCurrentDetailPager(int position) {

        //重新改FarameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        // 当前页面的布局
        View view = pager.mRootView;

        // 清除以前的布局
        flContent.removeAllViews();
        // 给帧布局添加布局
        flContent.addView(view);
        // 初始化页面数据
        pager.initData();

        // 更新标题
        tvTitle.setText(mNewsData.data.get(position).title);

        // 如果是组图页面，需要显示切换按钮
        btnPhoto.setVisibility(View.INVISIBLE);
    }
}
