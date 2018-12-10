package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ResultInfo;
import service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CategoryServlet", urlPatterns = "/Category")
public class CategoryServlet extends BaseServlet {
    //有了BaseServlet,doPost和doGet就可以不要了. 因为里面会执行service 方法.
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    //由于频繁要用到这个对象, 因此将它抽取出来. 注意要加上private修饰, 更安全.
    private CategoryService categoryService = new CategoryService();

    //这里, 可以考虑将protected 改为 public , 因为public 用反射就不用使用getDeclaredMethod()
    public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1_定义返回数据对象
        ResultInfo resultInfo = null;

        //这里可能有异常, 提前try..catch一下
        try {
            //2_调用业务逻辑层获取分类列表json格式数据
            String jsonData = categoryService.findAllCategory();

            //3_构造返回数据对象
            resultInfo = new ResultInfo(true, jsonData, null);

        } catch (Exception e) {
            e.printStackTrace();
            //如果有异常
            resultInfo = new ResultInfo(false);

        }

        //4_将resultInfo对象转换为json格式数据
        String jsonString = new ObjectMapper().writeValueAsString(resultInfo);
        //5_输出jsonData
        response.getWriter().print(jsonString);
    }

}
