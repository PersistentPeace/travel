package dao;

import model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JdbcUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public User getUserByUserName(String username) {
        String sql = "select * from tab_user where username=?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            //不注释会报错, 不过这种错误是正常的.
//            e.printStackTrace();
        }
        return user;
    }

    public void register(User user) {
        String sql = "insert into tab_user values (null,?,?,?,?,?,?,?,?,?)";
        Object[] params = {
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        };
        int update = jdbcTemplate.update(sql, params);
    }

    public int active(String code) {
        String sql = "update tab_user set status='y' where status='n' and code=?";
        return jdbcTemplate.update(sql,code);
    }

    public User findUser(String username) {
        //根据用户名查找用户
        String sql = "select * from tab_user where username=?";
        User dbUser = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        return dbUser;
    }

    /**********************************************自己写的**********************************************************/
//    //查询数据库中的激活码  : 老师的写法, 将两个语句变成一句话来写了.
//    public Boolean findDbcode(String code) {
//        String sql = "select * from tab_user where code=?";
//        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
//        if(user != null) {
//            return true; //有人
//        }
//        return false; // 没人
//    }
//    //将用户的状态改成激活状态
//    public void update(String code) {
//        String sql = "update tab_user set status = 'y' where code=?";
//        int update = jdbcTemplate.update(sql, code);
//    }

}
