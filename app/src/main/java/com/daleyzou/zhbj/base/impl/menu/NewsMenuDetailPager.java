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

    //页签网络数据
    private ArrayList<NewsMenu.NewsTabData> mTabData;
    //页签标签集合
    private ArrayList<TabDetailPager> mPagers;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
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
