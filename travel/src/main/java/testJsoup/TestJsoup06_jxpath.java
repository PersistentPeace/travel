package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.File;
import java.util.List;

public class TestJsoup06_jxpath {
    public static void main(String[] args) {
        try {
            //获取document对象
            String path = TestJsoup06_jxpath.class.getClassLoader().getResource("books.xml").getPath();
            File file = new File(path);
            Document document = Jsoup.parse(file, "utf-8");

            //获取JXDocument对象
            JXDocument jxDocument = JXDocument.create(document);

            // 调用对象里面的方法, 方法支持XPath语句来寻找节点
            List<JXNode> jxNodes = jxDocument.selN("//book/@id");
            for (JXNode jxNode : jxNodes) {
                System.out.println(jxNode);
            }

} catch (Exception e) {
e.printStackTrace();
        }
    }
}
