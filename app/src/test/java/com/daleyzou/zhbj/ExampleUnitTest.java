package com.daleyzou.zhbj;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

    }

    /**
     * 测试空格转换为\xa0，gson解析失败的情况。
     * \xa0表示不间断空白符.
     *
     * @throws IOException
     */

    @Test
    public void gson_special_char() throws IOException {
//        String fileName = "C:\\workspace2020\\0-github.com\\zhbj\\app\\src\\test\\java\\special_char2.json";
//
//        StringBuffer buffer = new StringBuffer();
//        BufferedReader bf = new BufferedReader(new FileReader(fileName));
//        String s = null;
//        while ((s = bf.readLine()) != null) {
//            buffer.append(s.trim() + System.getProperty("line.separator"));
//        }
//
//        String input = buffer.toString();
//        Gson gson = new GsonBuilder().setPrettyPrinting()
//                .setLenient() // 设置GSON的非严格模式setLenient()
//                .disableHtmlEscaping().create();
//
//
//
////        String myJson=   gson.toJson(o);//将gson转化为json
//
//        //可能会出现解析异常
//        Api21Bean newsTabBean = gson.fromJson(input, Api21Bean.class);
//        System.out.printf(""+newsTabBean.data.feed.get(0).title);
//        System.out.printf(""+newsTabBean.data.feed.get(1).title);

    }


}