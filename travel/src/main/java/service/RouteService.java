package service;

import dao.RouteDao;
import model.Category;
import model.PageBean;
import model.Route;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteService {
    private RouteDao routeDao = new RouteDao();

    //获取PageBean对象
    public PageBean getPageBean(int curPage, String cid, String rname) throws InvocationTargetException, IllegalAccessException {
//把握住主线: 怎么得到pageBean对象
// pageBean = 页面参数 + 当前页数据
// 页面参数需要: curPage, pageSize, count

        //1_计算分页参数信息 :select count(*) from tab_route where rflag='1' and cid=? ....这个参数是动态生成的.
        //1_1_根据编号统计当前类别下的总共记录条数
        int count = routeDao.getCount(cid,rname); //双重查询
        //1_2_创建PageBean对象,计算分页参数
        PageBean pageBean = new PageBean(curPage, 3, count);

        //2_获取当前请求页中的的数据, 这个查询需要用到多表查询的知识.
        //2_1_调用DAO层查询当前页中的路线信息,返回的是List<Map<String,Object>>类型

        List<Map<String, Object>> mapList = routeDao.getCurPageWithPage(cid, pageBean.getStartIndex(), pageBean.getPageSize(),rname);

        //2_2_将list<Map>转换为List<Route>类型
        //调用私有方法进行转换
        List<Route> list = convertMapListToList(mapList);

        //2_3_设置list到pageBean上
        pageBean.setData(list);

        //2_4_返回pageBean对象
        return pageBean;
    }

    //将List<Map<String, Object>> mapList 转换成为 List<Route> list
    private List<Route> convertMapListToList(List<Map<String, Object>> mapList) throws InvocationTargetException, IllegalAccessException {
        //将mapList转换成为List
        List<Route> list = null;
        if (mapList != null && mapList.size() > 0) {
            //创建一个ArrayList存放转换后的每个route对象
            list = new ArrayList<Route>();
            //遍历list
            for (Map<String, Object> map : mapList) {
                //map里面的数据就是{rid:1,rname:xxx,price:999....}类型
                //创建两个对象, 我们希望将route表中的数据封装到route对象中, 将category表中的数据封装到category对象中.
                //为啥要创建category?
                //  因为Route类里面有一个private Category category; 封装了一个分类.
                //  做这样件事是为了以后, 因为有可能开发中查出来的旅游路线上后面跟上该旅游路线所属的类别(分类名称).
                //  由于类别在一个表中, 具体的路由路线在另一个表中, 所以在route中又封装了这么一个对像.
                Route route = new Route();
                Category category = new Category();

                //使用BeanUtils工具类, 就可以将route里面的数据封装到route上, 将category上面的数据封装到category上.
                //以前是BeanUtils.populate(user, request.getParameterMap()); 注意, 使用这个工具类要导入两个包.将请求的参数封装到对象中
                // 封装的原理:
                //  拿到route对象以, 并对map进行遍历,获取map的key.
                //  看route的成员对象中有没有map对应的key.
                //  如果有, 就会调用route对象里面对应的setXXX方法,将map中的value值赋值进去.
                //  如果没有, 就不会进行赋值.
                BeanUtils.populate(route,map);
                BeanUtils.populate(category,map);

                //由于route里面有category对象, 所以我们将category对象封装到route里面
                route.setCategory(category);

                //route对象封装好了以后, 添加到list里面
                list.add(route);
            }
        }
        return list;
    }

    public void addroute(Route route) {
        routeDao.addroute(route);
    }
}
