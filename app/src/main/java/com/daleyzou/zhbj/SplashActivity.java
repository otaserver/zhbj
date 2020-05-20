package com.daleyzou.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.daleyzou.zhbj.global.GlobalConstants;
import com.daleyzou.zhbj.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 启动页，也是app的主活动页
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;

    //为日志Log定义tag标签
    private static final String TAG = "SplashActivity";

    /**
     * 是否显示开屏动画。
     */
//    private boolean isShowSplash = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        getDataFromServer();

        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        // 旋转动画
        RotateAnimation animRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //旋转时间
        animRotate.setDuration(100);
        // 保持动画结束状态
        animRotate.setFillAfter(true);

        //缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScale.setDuration(100);

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(200);
        animAlpha.setFillAfter(true);

        // 动画集合
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animRotate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);

        // 启动动画
//        if(isShowSplash){
        rlRoot.startAnimation(set);
//        }

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束，跳转页面
                // 如果是第一次进入，跳新手引导
                // 否则跳主页面

                Intent intent;
                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
                if (isFirstEnter) {
                    //新手引导
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                } else {
                    // 主页面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();//结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.NETWORK_TEST_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //请求成功
                String result = responseInfo.result;
                Log.d(TAG, "测试网络联通性，服务器返回结果：" + result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "测试网络联通性，从服务器取回内容失败！", e.getCause());
            }
        });
    }
}
