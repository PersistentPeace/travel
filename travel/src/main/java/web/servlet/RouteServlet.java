package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.PageBean;
import model.ResultInfo;
import service.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RouteServlet", urlPatterns = "/route")
public class RouteServlet extends BaseServlet {
    //常用, 抽取, 私有化
    private RouteService routeService = new RouteService();

    public void findCurPageRoutesWithPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //总结: 原料 目的
        //  servelt里面最终的目的是获取要响应到客户端的数据,resultInfo
        //  涉及到分页, 习惯将分页的信息封装到一个对象中. 兑现也要存到resultInfo中.
        //  不同的servlet中获取到pageBean对象需要不同的参数, 一把都是传递过来的有效参数.
        //  拿到: pageBean 送走: resultInfo

        //定义返回结果对象, 思路是将返回的结果先放到最外面. 有目标
        ResultInfo resultInfo = null;

        //以下这些步骤可能会有错误, 也为了我们能够最后响应错误的信息,所以我们同一try...catch一下.
        try {
            //定义当前页，默认用户访问当前页为1
            int curPage = 1; //这里是以因为后面也要用到这个数据.

            //获取用户当前请求第几页
            String curPageStr = request.getParameter("curPage");

            //获取当前要查询指定的分类导航cid
            String cid = request.getParameter("cid");

            //获取rname
            String rname = request.getParameter("rname");

            //注意: 经常要进行判断, 因为用户输入的内容可能有很多中.....
            //判断用户请求当前页的有效性(curPageStr不为空而且不是空的字符串),获取到的字符串形式的当前页转换为整型数字
            if (curPageStr != null && !"".equals(curPageStr)){
                curPage = Integer.parseInt(curPageStr);
            }

            //调用业务逻辑层根据当前页数据和分类id获取页面上所有数据PageBean对象
            PageBean pageBean = routeService.getPageBean(curPage,cid, rname); //增加一个参数rname

            //将数据封装到resultInfo中
            resultInfo = new ResultInfo(true,pageBean,null);

        } catch (Exception e) { // 获取一个大的Exception.
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }

        //将ResultInfo转换为json
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        //输出给浏览器
        response.getWriter().print(jsonData);
    }
}