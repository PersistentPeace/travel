package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestJsoup01 {
    public static void main(String[] args) {
        //如何通过jsoup获取对字符串形式的xml数据
        String xmlString = "<?xml version='1.0' encoding='UTF-8'?><books><book id='0001'><name>JavaWeb开发教程</name><author>张孝祥</author><sale>100.00元</sale></book><book id='0002'><name>三国演义</name><author>罗贯中</author><sale>100.00元</sale></book></books>";

        //如果通过依赖没有导进来, 可以通过手动的方式导入jar包. 注意, 这个jar包要放到WEB-INF里面的lib目录中, 没有就创建.
        //通过jsoup.parse()获取到document
        Document document = Jsoup.parse(xmlString);


    }
}
