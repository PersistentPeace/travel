package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TestJsoup03 {
    public static void main(String[] args) {

        try {
            //1. 利用jsoup获取网络上一个资源对应的document对象.
            String  url = "http://www.itcast.cn";
            //虽然访问url解析"http://ww.itcast.cn"返回的是一个网页的hmtl文档数据, 但是html文档也是xml文档格式数据

            Document document = Jsoup.connect(url).get(); //这个可能会获取失败, 所以try catch一下
            System.out.println(document); //得到的就是页面的信息.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
