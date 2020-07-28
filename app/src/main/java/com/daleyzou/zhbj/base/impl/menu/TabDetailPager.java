package com.daleyzou.zhbj.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.daleyzou.zhbj.NewsDetailActivity;
import com.daleyzou.zhbj.R;
import com.daleyzou.zhbj.domain.Api11Bean.NewsTabData;
import com.daleyzou.zhbj.domain.Api21Bean;
import com.daleyzou.zhbj.domain.Api21Bean.NewsData;
import com.daleyzou.zhbj.utils.CacheUtils;
import com.daleyzou.zhbj.utils.PrefUtils;
import com.daleyzou.zhbj.view.PullRefreshListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//https://github.com/wyouflf/xUtils3
//https://gitee.com/wyouflf/xUtils3
//封装了http请求，数据库映射，图片加载的一些常用方法。
//TODO：替换为新版的xutils

/**
 * 新闻Tab页。每个category下的数据。
 */
public class TabDetailPager extends BaseDetailPager {

    private static final String TAG = "TabDetailPager";

    /**
     * 单个页签的网络数据
     */
    private NewsTabData mTabData;


    private String mUrl;
    private ArrayList<NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;

    private String mPackageDate;

    @ViewInject(R.id.lv_tab_detail_list)
    private PullRefreshListView pullRefreshListView;

    /**
     * 下一页数据链接
     */
    private String mMoreUrl;

    @Override
    public ViewPager getViewPager(){
        return null;
    }


    public TabDetailPager(Activity activity) {
        super(activity);
    }

    TabDetailPager(Activity mActivity, NewsTabData newsTabData) {
        super(mActivity);
        mTabData = newsTabData;
        mUrl = newsTabData.url;
        Log.d(TAG, "mUrl:" + mUrl);
    }

    @Override
    public View initView() {
//        view = new TextView(mActivity);
//        //view.setText(mTabData.title);  此处空指针
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);


        // 5.前端界面设置回调
        pullRefreshListView.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {

            @Override
            public void onRefersh() {
                Log.d(TAG, "onRefersh(): key is null" );
                String key="";
                // 刷新数据
                getDataFromServer(key);
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore(): mMoreUrl is:|"+mMoreUrl+"|");

                // 判断是否有下一页数据,
                if (!mMoreUrl.isEmpty()) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    pullRefreshListView.onRefreshComplete(true);// 没有数据时也要收起控件
                }
            }
        });

//        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d(TAG, "position： " + position);
//                Log.d(TAG, "id： " + id);
//
//
//                int headerViewsCount = lvList.getHeaderViewsCount();// 获取头布局数量
//                position = position - headerViewsCount;// 需要减去头布局的占位
//
//                Log.d(TAG, "第 " + position + "被点击了！");
//
//                Api21Bean.NewsData news = mNewsList.get(position);
//
//                // read_ids: 记录已经被点击的新闻item的id
//                String readIds = PrefUtils.getString(mActivity, "read_ids", "");
//                if (!readIds.contains(news.newsId + "")) {// 只有不包含当前id才追加
//                    readIds = readIds + news.newsId + ",";
//                    PrefUtils.setString(mActivity, "read_ids", readIds);
//                }
//
//                // 要将被点击的item的文字改为灰色
//                TextView tvItemTitle = view.findViewById(R.id.tv_item_title);
//                tvItemTitle.setTextColor(Color.GRAY);
//                tvItemTitle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(mActivity, "标题实现点击TextView事件", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // 要将被点击的item的文字改为灰色
//                TextView tvItemCatName= view.findViewById(R.id.tv_item_category_name);
//                tvItemCatName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(mActivity, "分类字段实现点击TextView事件", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                // 跳到新闻详情页面
//                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
//                Log.d(TAG, " intent url:" + news.link);
//                Log.d(TAG, " intent newsId:" + news.newsId);
//                intent.putExtra("url", news.link);
//                intent.putExtra("newsId", news.newsId);
//                mActivity.startActivity(intent);
//            }
//        });
        return view;
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        Log.d(TAG, "getMoreDataFromServer: " + mMoreUrl);
//        if(mMoreUrl.isEmpty()){
//            return;
//        }
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, true);
                // 收起下拉刷新控件
                pullRefreshListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "Request failed. Url:" + mMoreUrl + ", Cause:" + s, e.fillInStackTrace());
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                // 收起下拉刷新控件
                pullRefreshListView.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData(String key) {
        Log.d(TAG, "initData()"+mUrl+mActivity+key);
//        view.setText(mTabData.title);
        String cache="";
        if(key!=null||key!=""||key!="null"){
            cache = CacheUtils.getCache(mUrl+"?keyword="+key, mActivity);
        }
        else{
            cache = CacheUtils.getCache(mUrl, mActivity);
        }

        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }
        getDataFromServer(key);
    }

    private void getDataFromServer(String key) {
        HttpUtils utils = new HttpUtils();
//        Log.d(TAG, "getDataFromServer()"+mUrl+key);

        String url ="";
        if(key!=null||key!=""||key!="null"){
            url =mUrl+"?keyword="+key;
        }
        else{
            url= mUrl;
        }

        Log.d(TAG, "getDataFromServer()"+url);

        //根据url，发送请求。
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d(TAG, "onSuccess result:" + result);
                processData(result, false);
                CacheUtils.setCache(mUrl, result, mActivity);
                // 收起下拉刷新控件
                pullRefreshListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "Request failed. Url:" + mUrl + ", Cause:" + s, e.fillInStackTrace());
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                // 收起下拉刷新控件
                pullRefreshListView.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result, boolean isMore) {
//        Log.d(TAG, "processData:" + result);
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        //从json中解析并加载数据。
        //可能会出现解析异常
        try {
            Api21Bean api21Data = gson.fromJson(result, Api21Bean.class);


//        mMoreUrl = GlobalConstants.MORE_URL;
//        赋值给类变量，用于翻页。
            mMoreUrl = api21Data.data.nextPageUrl;
            Log.d("processData mMoreUrl:", mMoreUrl);


            mPackageDate = api21Data.packageDate;

            if (!isMore) {
                // 列表新闻
                mNewsList = api21Data.data.feed;
                if (mNewsList != null) {
                    mNewsAdapter = new NewsAdapter();
                    pullRefreshListView.setAdapter(mNewsAdapter);
                }

            } else {
                // 加载更多数据
                ArrayList<Api21Bean.NewsData> moreNews = api21Data.data.feed;
                // 将数据追加到原来的集合中
                mNewsList.addAll(moreNews);
                // 刷新listview
                mNewsAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.e(TAG, "err Data:" + result);
            e.printStackTrace();
        }
    }


    class NewsAdapter extends BaseAdapter {
        private BitmapUtils mBitmapUtils;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        private NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

//            Log.d(TAG, " 进入getView()方法"+position);

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = convertView.findViewById(R.id.tv_item_title);
                holder.tvDate = convertView.findViewById(R.id.tv_item_date);
                holder.tvPrice = convertView.findViewById(R.id.tv_item_price);
                holder.tvCatName = convertView.findViewById(R.id.tv_item_category_name);

                convertView.setTag(holder);
                Log.d(TAG, " holder："+holder.tvTitle.getText());
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mActivity, "标题实现点击TextView事件", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, " position:" + position);
                    Api21Bean.NewsData news = mNewsList.get(position);

                    // 跳到新闻详情页面
                    Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                    Log.d(TAG, " intent url:" + news.link);
                    Log.d(TAG, " intent newsId:" + news.newsId);
                    intent.putExtra("url", news.link);
                    intent.putExtra("newsId", news.newsId);
                    mActivity.startActivity(intent);
                }
            });

            holder.tvCatName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "分类字段实现点击TextView事件" + position, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, " position:" + position);
                    Api21Bean.NewsData news = mNewsList.get(position);

                    Toast.makeText(mActivity, "跳转到" + news.catName, Toast.LENGTH_LONG).show();

                    // 跳到新闻详情页面
//                    Intent intent = new Intent(mActivity, NewsDetailActivity.class);
//                    Log.d(TAG, " intent url:" + news.link);
//                    Log.d(TAG, " intent newsId:" + news.newsId);
//                    intent.putExtra("url", news.link);
//                    intent.putExtra("newsId", news.newsId);
//                    mActivity.startActivity(intent);
                }
            });


            Api21Bean.NewsData news = (Api21Bean.NewsData) getItem(position);
            holder.tvTitle.setText(news.title);

//            holder.tvIntro.setText(news.intro);

            //转换为时间格式
            //TODO:测试用例写异常数据的处理

//            Date dNow = new Date(Long.parseLong(news.pubDate) * 1000);
//            holder.tvDate.setText(sDateFormat.format(dNow));

            holder.tvDate.setText(news.couponStartTime);

            // 根据本地记录标记已读、未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(news.newsId + "")) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            holder.tvPrice.setText(news.price);
            holder.tvCatName.setText(news.catName);

            mBitmapUtils.display(holder.ivIcon, news.pic);
            return convertView;
        }
    }

    /**
     * Tab页中的数据项
     */
    private static class ViewHolder {
        private ImageView ivIcon;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvPrice;
        //分类的名称
        private TextView tvCatName;
    }
}
