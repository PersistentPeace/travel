package dao;

import model.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JdbcUtils;


import java.util.List;

public class CategoryDao {
    //常用, 抽取, 私有化
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    //找到所有分类的方法
    public List<Category> findAllCategory() {
        String sql = "select * from tab_category";

        //提升权限
        List<Category> list = null;
        //有可能出错, 报异常
        try {
            list =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        } catch (DataAccessException e) {
            //e.printStackTrace();
//            这个异常我们就不打印了, spring框架底层设计为了防止非空判断, 就设计成为会报异常.
        }
        return list;
    }
}
