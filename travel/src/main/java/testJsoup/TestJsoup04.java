package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class TestJsoup04 {
    public static void main(String[] args) {
        try {
            //获取document对象
            String path = TestJsoup04.class.getClassLoader().getResource("index.html").getPath();
            File file = new File(path);
            Document document = Jsoup.parse(file, "utf-8");

            //需求1：获取index.html中所有h3标签名称的元素列表并打印输出
            Elements elementsByTag = document.getElementsByTag("h3"); //Elements是一个ArrayList集合.继承自ArrayList
            for (Element element : elementsByTag) {
                //System.out.println(element.nodeName() + "  " + element.tagName() + "  " + element.html());
                //nodeName与tagName是一回事. 都是标签的名称.
            }

            //需求2：获取index.html中所有元素含有class属性值为item并打印输出
            Elements elementsByClass = document.getElementsByClass("item");
            for (Element element : elementsByClass) {
                //System.out.println(element.nodeName() + "  " + element.tagName() + "  " + element.html());
            }

            //需求3：获取index.html中所有元素含有属性data-toggle所有元素列表并打印输出
            Elements elementsByAttribute = document.getElementsByAttribute("data-toggle");
            for (Element element : elementsByAttribute) {
//                System.out.println(element.nodeName() + "  " + element.tagName() + "  " + element.html());
            }

            //需求4：获取index.html中元素属性id="footer"的一个元素并打印输出
            Element elementById = document.getElementById("footer");
//            System.out.println(elementById.tagName() + "  " + elementById.html());


            //需求5：获取index.html中元素属性id="footer"的一个元素的父元素标签名称并打印输出
            Element pNode = elementById.parent();
//            System.out.println(pNode.tagName()+"  " + pNode.html()); //他的父元素是body, 所以里面是body的所有内容

            //需求6：获取index.html中元素属性id="footer"的元素的父元素的子元素列表的长度个数
            int size = pNode.children().size();
            System.out.println(size);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
