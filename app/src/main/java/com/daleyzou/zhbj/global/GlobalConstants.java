package com.daleyzou.zhbj.global;

public class GlobalConstants {
    //服务器主域名，
    public static final String SERVER_SITE_URL = "http://zns101.com:8083/";

    //增加一个网络联通性的验证接口，避免后期用json解析器解析json报错引起的退出。之所以没有端口是因为这个真的是静态文件。
    public static final String NETWORK_TEST_URL = "http://zns101.com/api/api0.json";

    //分类信息接口
    public static final String CATEGORY_URL = SERVER_SITE_URL+"api/api11-v1-category";

}

//完整访问url，参考保存在硬盘下的api11.json
//https://myresource-daleyzou.oss-cn-beijing.aliyuncs.com/categories.json
// 我的访问接口。
//http://zns101.com/api/api11-2.json
