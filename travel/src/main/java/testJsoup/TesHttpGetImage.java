package testJsoup;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.net.www.http.HttpClient;

import java.io.*;

public class TesHttpGetImage {
    public static void main(String[] args) throws IOException {
        //如何获取图片
        String url = "http://img.jinmalvyou.com/img_size4/201703/m4cc6cf2d161de7dbf8a963b0451ea7cfe.jpg";

        //获取到HttpClient对象  HttpClients.createDefault(); 这就相当于是http的客户端.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //实例化一个HttpGet对象 (参数是:要请求的路径) 相当于get请求.
        HttpGet httpGet = new HttpGet(url);
        //执行httpClient下的execute方法(参数是httpGet对象),返回response对象  http/1.1 200 ok
        //相当于在自己的机器上创建了一个socket, 通过网络向服务端发送一个请求.
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        //response对象http状态码为200代表通信过程正常。 //response.getStatusLine().getStatusCode()
        if(httpResponse.getStatusLine().getStatusCode() == 200) {
            //通过response获取到HttpEntity对象,代表返回的数据资源
            HttpEntity entity = httpResponse.getEntity();//entity代表想赢回来的资源.
            //通过HttpEntity对象返回一个输入流
            InputStream is = entity.getContent(); // 获取到一个输入流.
            //声明一个文件输出流(保证目录结构是存在的)
            File file = new File("E:\\","tt.jpg"); // 前面的是文件的路径, 后面是名称. 没有会创建.
            OutputStream os = new FileOutputStream(file);
            //利用IOUtil将输入流中的数据输出到文件中
            IOUtils.copy(is,os); // IOUtils是org.apche.commons.io包下的jar包, 导包不雅导错了.
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }
}
