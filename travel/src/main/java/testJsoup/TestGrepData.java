package testJsoup;

import model.Route;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.core.node.OuterHtml;
import service.RouteService;

import java.io.*;
import java.net.URLEncoder;

public class TestGrepData {
    public static void main(String[] args) throws Exception { //抛出个最大的异常省事
        //爬虫案例的实现步骤:

        //1_声明一个url代表我们要获取的服务端的某个页面,中文要编码
        String vv = URLEncoder.encode("国内", "utf8");
        String url = "http://www.jinmalvyou.com/search/index/view_type/1/keyword/" + vv;

        //2_利用Jsoup的形式获取到Document对象 Jsoup.connect(url).get();
        Document document = Jsoup.connect(url).get();

        //3_抓取当前页上的旅游线路列表数据标签, 利用document的选择器方法获取到页面中的一个个的产品html标签区域 (默认获取的是第1页数据)
        Elements elements = document.select(".rl-b-li");

        //4_抓取页面部分的末页 用doc.select(“.page .end”);
        Element endPage = document.select(".page .end").first();
        //<a class="end" href="/search/index/view_type/1/keyword/%E5%9B%BD%E5%86%85/p/42.html" data-ajax="false">末页</a>
        String href = endPage.attr("href"); // element.attr() 可以获取到对应的属性值.
        //截取字符串, 获得结束页码. 
        String pageNo = href.substring(href.indexOf("/p/") + 3, href.indexOf(".html"));
        int endPageNo = Integer.parseInt(pageNo);

        //5_利用for循环的形式获取各个页面数据
        for (int i = 1; i <= endPageNo; i++) {
            if (i > 1) {
                //6_在遍历中第一页已经获取不需要处理
                //7_非第一页的数据,我们要利用新的url重新获取document对象,
                // http://www.jinmalvyou.com/search/index/view_type/1/keyword/%E5%9B%BD%E5%86%85/p/2.html
                //这个地方有个坑!!!!!!!!!!!!!!!!!!!!!!一定要记得加上  http://
                url = "http://www.jinmalvyou.com/search/index/view_type/1/keyword/" + vv + "/p/" + i + ".html";
                //重新读取页面部分的产品html标签区域
                document = Jsoup.connect(url).get();

                //8_遍历产品区域标签，将数据封装到route对象上
                elements = document.select(".rl-b-li");
            }

            //通过rooteElement.selectFirst(“”);获取页面部分各个数据
            Route route = new Route();

            for (Element element : elements) {
                //此处的ele代表页面各个li项的列表数据, 找到一个li的模板, 然后根据响应的标签获取到响应的值.
                String rname = element.select(".pro-title a").first().html();
                String routeIntroduce = element.select(".pro-con-txt>br+p").first().html();
                double price = Double.parseDouble(element.select(".pro-price p strong").first().html());
                String src = element.select(".pro-img a img").first().attr("src");
                // src里面的内容: //img.jinmalvyou.com/img_size3/201703/m3cc6cf2d161de7dbf8a963b0451ea7cfe.jpg

                //封装数据到对象中.
                route.setRname(rname);
                route.setPrice(price);
                route.setRouteIntroduce(routeIntroduce);
                String imgName = src.substring(src.lastIndexOf("/")); // lastIndexOf(),从最后出现xxx的地方开始截取.

                //下载图片到本地:
                //    图片不能放,图片的处理是先下载, 然后将本地硬盘上的地址封装到对象上.利用刚才写的案例:
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("http:" + src);
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    InputStream is = entity.getContent();
                    //文件的名称也要截取一下.
                    File file = new File("E:\\picture", imgName);
                    OutputStream os = new FileOutputStream(file);
                    IOUtils.copy(is, os);
                    IOUtils.closeQuietly(os);
                    IOUtils.closeQuietly(is);
                }
                //封装img到route对象
                route.setRimage("E:\\picture" + imgName);
                //9_调用service__>dao的方法将数据插入到数据库中
                RouteService routeService = new RouteService();
                routeService.addroute(route);
            }
        }

    }
}
