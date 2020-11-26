package com.daleyzou.zhbj.domain;

import java.util.ArrayList;

/**
 * 分类信息的封装
 *
 * 使用Gson解析时，对象书写技巧：
 *  1.逢{}创建对象，逢[]创建集合（ArrayList）
 *  2.所有字段要和json返回字段一致
 * @author scott
 */
//完整的api11-2.json的映射，包含retcode和extend和data三个主节点。但retcode和extend没有使用，在java中去除。
public class Api11Bean {
    public ArrayList<NewsMenuData> data;

    @Override
    public String toString() {
        return "Api11Bean{" +
                "data=" + data +
                '}';
    }

//侧边栏菜单对象，
//    对应api11-2.json中的数据，前就定义了第一个。
//    "id": 10000,
//    "title": "研值快报",
//    "type": 1,
//    "children":[{
//            "id":"3654866799",
//            "title":"文化娱乐",
//            "type": 1,
//            "url": "http://zns101.com/api/api21-3654866799-0.json"
//    },......]
    public class NewsMenuData{
        public String id;
        public String title;
        public int type;


        //例如：children在目前定义的"title": "研值快报",中是一个20个的一级分类。
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", children=" + children +
                    '}';
        }
    }

//页签的对象，对应api11-2.json中的data,children。所以api21-XXX是没有定义常量在GlobalConstants.java中。
//    {
//        "id":"2817066264",
//            "title":"日用百货",
//            "type": 1,
//            "url": "http://zns101.com/api/api21-2817066264-0.json"
//    },
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;
        public String abbr;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", abbr=" + abbr +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


}
