package com.daleyzou.zhbj.base.impl.menu;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.daleyzou.zhbj.MainActivity;
import com.daleyzou.zhbj.R;
import com.daleyzou.zhbj.base.BaseMenuDetailPager;
import com.daleyzou.zhbj.domain.NewsMenu;
import com.daleyzou.zhbj.domain.NewsMenu.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * <p>
 * ViewPagerIndicator使用流程
 * 1.引入库
 * 2.解决冲突
 * 3.从例子程序中拷贝布局文件
 * 4.从例子程序中拷贝相关代码（指示器和viewpager绑定；重写getPageTitle返回标题）
 * 5.在清单文件中增加样式
 * 6.背景修改为白色
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private static final String TAG = "NewsMenuDetailPager";

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    TabPageIndicator mIndicator;

    /**
     * 页签网络数据
     */
    private ArrayList<NewsTabData> mTabData;

    /**
     * 页签标签集合
     */
    private ArrayList<TabDetailPager> mPagers;

//    传入的children数据。
//    NewsTabData{id=10007, title='推荐', type=1, url='http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926'},
//    NewsTabData{id=10095, title='关注', type=1, url='http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926'

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        // 初始化页签
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);//将viewpager和指示器绑定在一起

        mIndicator.setOnPageChangeListener(this);// 设置页面滑动监听
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 响应顶部菜单切换
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "当前位置：" + position);
        if (position == 0) {
            setSlidingMenuEnable(true);
        } else {
            setSlidingMenuEnable(false);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        // 指定指示器的标题
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            NewsMenu.NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }
    }

    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        // 跳到下个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
