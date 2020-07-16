package com.daleyzou.zhbj.domain;

import java.util.ArrayList;

/**
 * 分类信息的封装
 *
 * 使用Gson解析时，对象书写技巧：
 *  1.逢{}创建对象，逢[]创建集合（ArrayList）
 *  2.所有字段要和json返回字段一致
 *
 */
//完整的api11-2.json的映射，包含retcode和extend和data三个主节点。
public class NewsMenu {
    public ArrayList<NewsMenuData> data;

    //侧边栏菜单对象，对应api11-2.json中的  "id": 10000,"title": "研值快报","type": 1，children,数组，目前就定义了第一个。
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
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }
}
