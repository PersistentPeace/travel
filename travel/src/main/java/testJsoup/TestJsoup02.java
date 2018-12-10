package testJsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class TestJsoup02 {
    public static void main(String[] args) {
        //利用jsoup获取已经存在文件中的docment对象

        //由于可能会有异常, 提前try...catch一下.
        try {
            //1. 获取到这个文件的路径
            //读取配置文件: 要用当前类的字节码文件的类加载器. 有了类加载器. 就可以读取某个文件的资源.
            // getResource()方法默认是当前项目下的resource目录里面的我呢间, getPath()是获取当前文件的路径.
            String path = TestJsoup02.class.getClassLoader().getResource("books.xml").getPath();
            // System.out.println(path);
            // 结果为/E:/project/Java/web/travel/target/classes/books.xml 由此可见是绝对路径.

            //2. 定义资源文件对象
            File file = new File(path);

            //3. 利用jsop下的api进行解析
            Document document = Jsoup.parse(file, "utf-8");
            System.out.println(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
