package com.daleyzou.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daleyzou.zhbj.MainActivity;
import com.daleyzou.zhbj.NewsDetailActivity;
import com.daleyzou.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 5个标签页的基类
 */
public class BasePager {

    private static final String TAG = "BasePager";
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;

    //空的帧布局对象
    public FrameLayout flContent;

    //当前页面的布局文件对象
    public View mRootView;

    // 组图切换按钮
    public ImageButton btnPhoto;

    public BasePager(Activity activity) {
        mActivity = activity;
        Log.d(TAG, "BasePager被创建！");
        mRootView = initView();
    }

    /**
     * 初始化布局,
     *
     * @return
     */
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        //图片按钮，用于调出左侧隐藏的菜单。
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);


//        取消左侧的划出菜单，因为目前没有定义。
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggle();
//            }
//        });

        SearchView sv = (SearchView) view.findViewById(R.id.sv);

        // 设置该SearchView默认是否自动缩小为图标
        sv.setIconifiedByDefault(true);

        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);

        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("请输入...");

        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当点击搜索编辑框的时候执行，刚进入时默认点击搜索编辑框
            }
        });

        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // 用户输入字符时激发该方法
            @Override public boolean onQueryTextChange(String newText)
            {
                // 如果newText不是长度为0的字符串
                if (TextUtils.isEmpty(newText))
                {
                    // 清除ListView的过滤
//					lv.clearTextFilter();
                } else
                {
                    // 使用用户输入的内容对ListView的列表项进行过滤
//					lv.setFilterText(newText);
                }
                return true;
            }
            // 单击搜索按钮时激发该方法
            @Override public boolean onQueryTextSubmit(String query)
            {

                Toast.makeText(mActivity, "您的选择是:" + query, Toast.LENGTH_SHORT).show();

                doSearch(query);

                // 用此方法可以定义滑动到最后一页。
//        mViewPager.setCurrentItem(3);

                // 跳到新闻详情页面
                //TODO：最简单的实现，定义21个tab页，就是查询，直接跳转即可。yes！
//                系统会预加载当前tab页之前和之后的tab页的内容，方便快速展示。
//
//                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                //也可以定义SearchActivity
//                Intent intent = new Intent(mActivity, MainActivity.class);
//
//
//                intent.putExtra("url", "https://uland.taobao.com/coupon/edetail?e=O6djOr04rqObhUsf2ayXDGkgtRwkaNgC3BrGiMJ2aYHb5TINHYeg%2BMZ1aHPBiHFBBPnQG9SaDjpeiWqWXEvHpnynu2XW3aUk%2F4Nx%2FkoOWX6hnG2fpYFJ2rwEthZ%2FFmAQ9NbcFhtrtVpjMmGGcz6SnLh71VitJv0FSKOEUaHbnt8%3D&af=1&pid=mm_368070113_840900154_109420750311");
//                intent.putExtra("newsId", "615622054721");
//                mActivity.startActivity(intent);

                return false;
            }
        });
        return view;

    }

    public  void doSearch(String key){
        Log.d(TAG, "BasePAger.java  doSearch() for key:"+key);
    }

    /**
     * 打开或者关闭侧边栏，
     * 注释此方法，会禁止左方的滑动菜单的展示
     */
//    private void toggle() {
//        MainActivity mainUI = (MainActivity) mActivity;
//        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
//        slidingMenu.toggle();//如果当前状态是开，调用后就关；反之亦然
//    }

    /**
     * 初始化数据
     * 在继承关系下覆盖。
     */
    public void initData() {

    }
}
