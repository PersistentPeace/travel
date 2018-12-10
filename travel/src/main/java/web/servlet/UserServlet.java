package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.media.sound.UlawCodec;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import exception.PasswordErrorException;
import exception.UserNameExistsException;
import exception.UserNameNotNullException;
import model.ResultInfo;
import model.User;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;
import utils.Md5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet{
    //UserServlet这个类由于继承了BaseServlet这个类, 首先要执行init方法, BaseServlet里面有(继承自HttpServlet)
    //执行完成后要执行service方法, BaseServlet里面重写了. UserServlet执行继承自BaseServlet的service方法.
    // 这里要注意的就是service 方法里面的this指的就是子类

    //由于很多功能都需要调用业务层, 所以, 将他放到类内方法外.
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //这里要用一个servlet来处理很多的请求.
        //获取标识符, 判断本次要执行哪个功能个
        /*String action = request.getParameter("action");
        if ("register".equals(action)) {
            register(request, response);
        } else if ("active".equals(action)) {
            active(request, response);
        } else if ("login".equals(action)) {
            login(request, response);
        } else if ("getLoginUserData".equals(action)) {
            getLoginUserData(request,response);
        } else if ("loginOut".equals(action)) {
            loginOut(request,response);
        }*/
        //虽然这样写已经很好了, 但是如果功能很多的情况下, 这种写法还是很繁琐的.

        //这个方法里面其实就两个步骤:
        //  a. 获取action的值
        //  b. 看有没有值与action相等的方法.
        // 因此我们可以通过反射来创建:

        /*try {
            //  a.获取当前文件(UserServlet)的字节码文件对象
            Class clazz = this.getClass();
            //  b. 有了字节码文件对象以后. 我们可以看这个对象里面有没有这个方法
            String action = request.getParameter("action");
            Method method = clazz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);//方法可能没有, 所以会有异常, 整体捕获一下
            if(null != method) {
                //暴力破解
                method.setAccessible(true);
                //执行方法:
                method.invoke(this,request,response); // 这里也会报异常. 我们将下面捕获的异常设置为最大的异常.
                // 注: 通过反射调用方法, 总归是有一个对象才能调用, 毕竟不是工具类. 所以 this就是当前对象, 谁调用, this就是指谁.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //这段代码虽然这样写没有问题, 但是不同的servlet请求过来, 那以上反射的代码就要重复了, 因此我们将此抽取出一个父类.让当前类继承父类即可.

    }

    //这里也是一个坑, alt + enter 创建的方法为public方法, 我们要的是protected,否则别的类访问不到.

    //退出
    protected void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        request.getSession().invalidate();
        //跳转到登录页面
        response.sendRedirect("/travel/login.html");
    }

    //获取登录用户信息
    protected void getLoginUserData (HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        //1_定义返回数据对象
        ResultInfo resultInfo = null;

        //2_获取用户登录数据
        User user = (User) request.getSession().getAttribute("loginUser");

        //3_判断登录数据有效性
        if(user == null) {
            //3_1_用户没有登录，直接存储false状态，代表获取数据失败
            resultInfo = new ResultInfo(false);
        } else {
            //3_2_用户有登录数据，状态为true，并且存储登录的数据进行返还
            resultInfo = new ResultInfo(true,user,null); // 这里, 写null 是因为没有参数为flag,data的构造
        }
        //4_将返回数据转换为json
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);

        //5_返回给浏览器
        response.getWriter().print(jsonData);
    }

    //登录
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        //定义返回客户端数据对象
        ResultInfo resultInfo = null;

        User dbUser = null;
        //查找用户名与密码的过程可能发生异常, 所以我们try..catch一下
        try {
//获取请求数据
            //获取用户名与密码
            String username = request.getParameter("username");
            String password = request.getParameter("password");
//对请求数据进行简单加工, 将数据发送到service
            //对密码加密(由于数据库存的是加密密码，所以登录时也要加密)
            String md5Password = Md5Util.encodeByMd5(password);
            //验证登录验证码(自己完成)
//接收返回来的数据
            //调用业务逻辑登录方法,返回dbUser对象
            dbUser = userService.login(username, md5Password);
            if (dbUser != null) {
                //如果dbUser不为空登录成功，将用户信息写入session,创建resultInfo实例对象
                request.getSession().setAttribute("loginUser", dbUser);
                resultInfo = new ResultInfo(true);
            }
        } catch (UserNameNotNullException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (UserNameExistsException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (PasswordErrorException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
//响应数据到前端
        //获取返回浏览器的json数据
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        //输出到浏览器
        //如果是成功, 响应的数据是{flag:true}
        //如果失败,响应的是{flag:false,errorMsg:""}
        //打印一下, 看下结果.
//        System.out.println(jsonData);
        response.getWriter().print(jsonData);
    }

    //激活的代码
    protected void active(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        //获取激活码
        String code = request.getParameter("code");
        //调用业务逻辑类UserService进行激活,返回激活结果
        boolean flag = userService.active(code);
        if (flag) {
            //如果激活成功，跳转到登录页面
            response.sendRedirect("/travel/login.html");
        } else {
            //激活失败，显示激活失败
            response.getWriter().print("激活失败...");
        }
    }

    //注册方法
    protected void register(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        //1. 定义返回结果对象
        ResultInfo resultInfo = null; // 先让他为null

        //2.验证码验证(在session中讲过,自己完成)
        request.getParameter("check"); //生成验证码的时候就已经在session里面存了一份.这个自己验证.


        try {
            //3.获取数据并封装数据到User对象, 这么做的目的是将请求来的数据打包好发送到service层.
            User user = new User();
            BeanUtils.populate(user, request.getParameterMap());

            //4.调用业务逻辑层注册用户,返回注册是否成功
            boolean flag = userService.register(user);

            //5.获取注册结果
            if (flag) {
                resultInfo = new ResultInfo(true);
            }

            //通过这种捕获异常的方式, 可以达到一个方法得到多个值的效果
        } catch (UserNameNotNullException e) {
            resultInfo = new ResultInfo(false, "用户名不能为空..");
        } catch (UserNameExistsException e) {
            resultInfo = new ResultInfo(false, "用户名已经被注册..");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //6.将resultInfo转换为json数据返回给客户端
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        //7.输出给浏览器
        // 发送给浏览器的无非几种情况:
        //      {"flag":true}
        //      {"flag":false, "errorMsg":""}
        response.getWriter().print(jsonData);
    }

/*************************************************    自己的方法    **************************************************/
    //激活方法(自己的写法)
//    protected void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
////获取参数
//        String code = request.getParameter("code");
//        System.out.println(code);
////处理参数
//        //调用service层的方法,
//        Boolean bool = userService.active(code);
////获取结果
//        //结果就是boolean
////响应结果
//        if (bool) {
//            response.sendRedirect("/travel/login.html");
//        } else {
//            response.getWriter().print("激活失败....");
//        }
//    }
}
