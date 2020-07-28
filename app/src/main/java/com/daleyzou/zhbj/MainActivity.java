package com.daleyzou.zhbj;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daleyzou.zhbj.fragment.ContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * MainActivity是继承了SlidingFragmentActivity。
 */
public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        设置滑动菜单，但是本例中没有使用。
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        //全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //屏幕预留100像素宽度
        slidingMenu.setBehindOffset(450);

        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 参1：帧布局容器的id；参2：要替换的fragment；参3：要打上的标签的标签名
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
        //提交事务
        transaction.commitNow();
    }


    /**
     * 获取内容Fragment对象
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return contentFragment;
    }
}
