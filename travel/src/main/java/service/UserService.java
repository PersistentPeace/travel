package service;

import dao.UserDao;
import exception.PasswordErrorException;
import exception.UserNameExistsException;
import exception.UserNameNotExistsException;
import exception.UserNameNotNullException;
import model.User;
import utils.MailUtil;
import utils.Md5Util;
import utils.UuidUtil;

import javax.xml.ws.Response;
import java.util.Locale;

public class UserService {
    private UserDao userDao = new UserDao();

    //注册方法
    public boolean register(User user) throws UserNameNotNullException, UserNameExistsException, Exception {
        //数据验证-用户名不能为空（由于客户端浏览器可以禁用js，所以后端为了安全也进行基础数据验证）
        //去提示用户并且用户自己处理掉，使用抛出自定义异常
        if (user.getUsername() == null || "".equals(user.getUsername())) {
            throw new UserNameNotNullException("用户名不能为空....");
        }
        //根据用户输入的用户名去查找数据库对应用户
        User dbUser = userDao.getUserByUserName(user.getUsername());
        if (null != dbUser) {
            //如果数据库用户不为空，说明用户名已被注册, 抛出自定义异常
            throw new UserNameExistsException("用户名已经被注册");
        }

        //激活状态为未激活
        user.setStatus("n");

        //激活码,用于激活
        user.setCode(UuidUtil.getUuid());

        //对密码加密(MD5加密，消息摘要第五版加密算法,不可逆的加密算法)
        String psd = Md5Util.encodeByMd5(user.getPassword());
        // 注意, 这一步会报错, 但是alt+ enter 会将UserNameNotNullException, UserNameExistsException变换成为一个最大的异常Exception
        //所以, 我们要手动添加这个异常Exception.
        user.setPassword(psd);

        //实现注册添加用户
        userDao.register(user);
        //发送激活邮件,根据用户提供的注册邮箱发送激活邮件,返回true
        //注意:
        // 请求的参数: action= active&code
        //user.getCode()激活码
        MailUtil.sendMail(user.getEmail(), "<a href='http://localhost:8080/travel/user?action=active&code=" + user.getCode() + "'>用户激活</a>");
        return true;
    }

    //激活方法
    public boolean active(String code) {
        //调用数据访问层进行激活，返回影响行数
        int num = userDao.active(code);
        //返回激活结果，影响行数>0说明激活成功，否则激活失败
        return num > 0;  // 这一步也很优秀. 如果数据库返回的行数 > 0 , 说明没有改了. 说明激活成功.
    }

    //登录方法
    public User login(String username, String md5Password) throws UserNameNotNullException, PasswordErrorException, UserNameExistsException {
        //1_判断如果用户名为空,抛出用户名不能为空的异常
        if (username == null || "".equals(username.trim())) { //这里要注意, trim()
            throw new UserNameNotNullException("用户名不能为空");
        }
        //2_根据用户名查找数据库用户对象,返回用户对象dbUser
        User dbUser = userDao.findUser(username);

        //3_如果dbUser为空,抛出用户名不存在异常
        if(dbUser == null) {
            throw new UserNameExistsException("用户名不存在");
        }
        //4_如果密码不为空而且不等于dbUser中的密码,抛出密码错误异常
        // 这里也是一个坑, 字符串比较一定要是.equals方法. 不能用==比较.
        if(md5Password != null &&  !md5Password.equals(dbUser.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }
        //5_返回登录成功的数据库用户
        return dbUser;
    }

/****************************************************  自己的写法   ***************************************************/
    //激活方法(自己的写法)
//    public Boolean active(String code) {
//        //调用dao层的激活方法查询dao层的激活码
//        Boolean bool = userDao.findDbcode(code);
////        如果与数据库中的激活码相同, 则注册成功:
//        if (bool == true) {
//            //  将数据库中的status由n改为y
//            userDao.update(code);
//            //  跳转到登录页面
//            return true;
//        } else {
//            //如果不一致, 响应激活失败.
//            return false;
//        }
//    }
}
