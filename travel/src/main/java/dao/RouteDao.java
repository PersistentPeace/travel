package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import utils.JdbcUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    //获取总数
    public int getCount(String cid, String rname) {
        // 如果这么写, 就把后面的参数写死了.这样不好, 不利于扩展
        //String sql = "select count(*) from tab_route where rflag='1' and cid=?";
        // 所以我们动态的拼凑, 这里也是个坑, 容易出现拼接前后语句连接的太紧密.
        String sql = "select count(*) from tab_route where rflag='1' ";
        List paramList = new ArrayList();
        //添加需要判断条件:如果cid不为空或者是空字符串
        if (!cid.equals("null") && !"".equals(cid)) {
            //先凑好sql语句
            sql += " and cid=?  ";
            //在凑好参数
            int i = Integer.parseInt(cid);
            paramList.add(i);
        }
        //添加rname的条件判断, 由此可见, 这种拼接的写法大大提高了sql语句的可扩展性.
        if (!rname.equals("null") && !"".equals(rname)) {
            //先凑好sql语句
            sql += " and rname like ? ";
            //在凑好参数: 注意, %要注意流出空格, 以防黏连.
            // 空格是个坑啊!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            paramList.add("%" + rname + "%");
        }

        int count = jdbcTemplate.queryForObject(sql, Integer.class, paramList.toArray());
        return count;
    }

    //查询当前页的数据
    public List<Map<String, Object>> getCurPageWithPage(String cid, int startIndex, int pageSize, String rname) {
        // 固定写法, 可以这样写,但是不好, 因为以后要添加内容的时候无法添加
        // String sql = "select * from tab_category c, tab_route r where r.rid = c.cid and rflag=1 and c.cid=? limit ?, ?";
        // 注意, 要将后面加上拼接的字符串后面要加上一个空格, 防止黏连
        String sql = "select * from tab_category c, tab_route r where r.cid = c.cid and rflag=1 ";

        //这里的拼凑与之前一样.
        List paramList = new ArrayList();
        //添加需要判断条件:如果cid不为空或者是空字符串   这个!"".equals()的!不能丢!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (!cid.equals("null") && !"".equals(cid)) {
            int i = Integer.parseInt(cid);
            //先凑好sql语句:因为有两个cid. 不写清楚会报错
            sql += " and r.cid=? ";
            //在凑好参数
            paramList.add(i);
        }

        //添加rname的条件判断, 由此可见, 这种拼接的写法大大提高了sql语句的可扩展性.
//        这个!"".equals()的!不能丢!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (!rname.equals("null") && !"".equals(rname)) {  //注意, 这里是"字符串的null" 不是null或者""
            //先凑好sql语句
            sql += " and rname like ? ";
            //在凑好参数, 注意, %要注意流出空格, 以防黏连.
            paramList.add("%" + rname + "%");
        }

        //除此之外: sql还要加上limit ?, ?
        sql += " limit ?, ? ";
        // 参数还要加上
        paramList.add(startIndex);
        paramList.add(pageSize);

        //这里还是打印一下, 可以帮助快速定位错误所在
        System.out.println(sql);
        //记得要转成数组, 因为可变参数的本质是数组.
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, paramList.toArray());
        //返回结果是一个list, 里面有3个map, 以键值对的形式保存着数据
        // {
        //  {rid:1,rname:xxx,price:999....},
        //  {rid:1,rname:xxx,price:999....},
        //  {rid:1,rname:xxx,price:999....}
        // }
        //将结果也打印一下
        System.out.println(mapList); //作用是快速定位错误
        return mapList;
    }
}
