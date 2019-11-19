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
//        测试数据。
//        NewsMenu{
//            data=[
//            NewsMenuData{id=10000, title='测试', type=1,
//                    children=[
//                NewsTabData{id=10007, title='推荐', type=1, url='http://newsapi.sina.cn/XXX988&rand=926'},
//                NewsTabData{id=10095, title='关注', type=1, url='http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926'}
//]},
//            NewsMenuData{id=10002, title='没想好', type=10, children=null},
//            NewsMenuData{id=10003, title='不知道', type=2, children=null},
//            NewsMenuData{id=10004, title='不写了', type=3, children=null}
//]}
//        代码里只取了data中的第一个节点。
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
