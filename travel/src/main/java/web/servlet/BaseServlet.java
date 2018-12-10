package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet", urlPatterns = "/base") //这个url要不要都无所谓.
public class BaseServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    //这两句我们就不要了,我们直接重写service方法.
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //  a.获取当前文件(UserServlet)的字节码文件对象
            Class clazz = this.getClass();
            //  b. 有了字节码文件对象以后. 我们可以看这个对象里面有没有这个方法
            String action = request.getParameter("action");
            //这里也是个坑, getMethod只能获取到public 修饰的方法, 其他修饰的方法不能获取到.
            Method method = clazz.getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);//方法可能没有, 所以会有异常, 整体捕获一下
            if (null != method) {
                //暴力破解
                method.setAccessible(true);
                //执行方法:
                method.invoke(this, request, response); // 这里也会报异常. 我们将下面捕获的异常设置为最大的异常.
                // 注: 通过反射调用方法, 总归是有一个对象才能调用, 毕竟不是工具类. 所以 this就是当前对象, 谁调用, this就是指谁.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //以后其他的类就可以继承BaseServlet, 不用继承HttpServlet了.
}
