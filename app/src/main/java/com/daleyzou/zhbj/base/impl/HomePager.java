package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daleyzou.zhbj.base.impl.menu.BaseDetailPager;
import com.daleyzou.zhbj.base.impl.menu.MenuDetailPager;
import com.daleyzou.zhbj.domain.Api11Bean;
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
 * 首页,没有左侧的隐藏菜单。
 */
public class HomePager extends BasePager {

    //菜单详情页集合，已经转换为对象的新闻列表。
    private ArrayList<BaseDetailPager> mMenuDetailPagers;

    //APP11的分类。
    private Api11Bean.NewsMenuData app211Data;

    //为日志Log定义tag标签
    private static final String TAG = "HomePager";

    public HomePager(Activity activity) {
        super(activity);
        Log.d(TAG, "HomePager被创建！");
    }

    @Override
    public void initData() {
//        要给帧布局填充布局对象
//        TextView view = new TextView(mActivity);
//        view.setText("显示最新的特价信息");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//
//        flContent.addView(view);
//        动态布局，这个设置就没有用了。

        //修改页面标题
        tvTitle.setText("首页");

        //隐藏菜单按钮,其实是左上方的那个三个横线。
        btnMenu.setVisibility(View.GONE);
//        btnMenu.setVisibility(View.VISIBLE);

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
//                Log.d(TAG, "服务器返回结果：" + result);
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
        app211Data = gson.fromJson(json, Api11Bean.NewsMenuData.class);
        Log.d(TAG, "Gson对象:" + json);

        //初始化菜单详情页
        mMenuDetailPagers = new ArrayList<BaseDetailPager>();
//        代码里只取了data中的第一个节点。
        mMenuDetailPagers.add(new MenuDetailPager(mActivity, app211Data.children));

        // 将新闻菜单详情页设为默认界面,因为json里就只有第一个节点。这里也只能设置为0.
        setCurrentDetailPager(0);
    }


    @Override
    public void doSearch(String key) {
        //重新改FarameLayout添加内容
        BaseDetailPager pager = mMenuDetailPagers.get(0);
        Log.d(TAG, "HomePAger.java  doSearch() for key:" + key + pager);
        //在这里定位Tab页的位置。
        // TODO：魔法数字，应该使用关键字来定位tab！
        int posOfSearchTab = 21;
        pager.getViewPager().setCurrentItem(posOfSearchTab);
        //通过传递key和初始化数据来进行查询！
        pager.setKey(key);
        pager.initData(key);
    }

    /**
     * 设置菜单详情页，相当于刷新。
     *
     * @param position
     */
    public void setCurrentDetailPager(int position) {
        Log.d(TAG, "setCurrentDetailPager, position：" + position);

        //重新改FarameLayout添加内容
        BaseDetailPager pager = mMenuDetailPagers.get(position);
        // 当前页面的布局
        View view = pager.mRootView;

        // 清除以前的布局
        flContent.removeAllViews();
        // 给帧布局添加布局
        flContent.addView(view);

        String key = "";
        // 初始化页面数据
        pager.initData(key);

        // 更新标题
        tvTitle.setText(app211Data.title);

        //这里就是第一条记录，实际设置值为"研值快报"
        Log.d(TAG, "setCurrentDetailPager, title:" + app211Data.title);

        // 如果是组图页面，需要显示切换按钮
        btnPhoto.setVisibility(View.INVISIBLE);
    }
}
