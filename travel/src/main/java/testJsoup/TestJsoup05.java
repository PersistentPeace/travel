package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class TestJsoup05 {
    public static void main(String[] args) {
        try {
            //获取document对象
            String path = TestJsoup05.class.getClassLoader().getResource("index.html").getPath();
            File file = new File(path);
            Document document = Jsoup.parse(file, "utf-8");

            //通过选择器的方式解析数据
            //需求1：获取id="footer"元素并输出元素名称
            Elements elements = document.select("#footer");
//            System.out.println(elements); // 结果为footer及footer里面的所有内容

            //需求2：获取index.html中所有元素含有class属性值为item并打印输出元素体内容
            elements  = document.select(".item");
            for (Element element : elements) {
//                System.out.println(element.tagName() + "  " + element.html());
            }


            //需求3：获取index.html中所有h3标签名称的元素列表并打印输出元素名称
            elements  = document.select("h3");
            for (Element element : elements) {
//                System.out.println(element.tagName() + "  " + element.html());
            }

            //需求4：获取index.html中含有属性data-toggle所有元素并打印输出元素名称和元素体数据
            elements  = document.select("[data-toggle]");
            for (Element element : elements) {
//                System.out.println(element.tagName() + "  " + element.html());
            }

            //需求5：获取index.html中属性id值为"banner"的所有div子元素并打印元素数据
            elements  = document.select("#banner div");
            for (Element element : elements) {
                System.out.println(element.tagName() + "  " + element.html());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
