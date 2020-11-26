package com.daleyzou.zhbj.domain;

import java.util.ArrayList;

/**
 * 页签详情对象
 * gson通过此bean来映射对象。
 * Api21的映射对象。
 */
public class Api21Bean {

    public class NewsTab {
        public String nextPageUrl;
        public ArrayList<NewsData> feed;
    }


    /**
     * 新闻列表对象
     */
    public class NewsData {
        public String newsId;
        public String pic;
        public String title;
        public String link;
        public String price;
        public String catName;
        public String catId;
        public String couponStartTime;
        public String couponEndTime;
    }
}
