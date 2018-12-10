package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CategoryDao;
import model.Category;
import redis.clients.jedis.Jedis;
import utils.JedisUtil;

import java.util.List;

public class CategoryService {
    //常用: 抽取, private修饰
    private CategoryDao categoryDao = new CategoryDao();

    public String findAllCategory() throws JsonProcessingException {
        //从jedis中获取分类列表数据,

        //1_获取实例jedis对象
        Jedis jedis = JedisUtil.getJedis();

        //2_获取jedis中的key为"categoryList"的value数据,返回jsonData
        String jsonData = jedis.get("categoryList");

        //3_判读获取数据有效性 如果jsonData为空或者是空的字符串,说明redis中没有分类列表数据
        if(jsonData == null || "".equals(jsonData)){
            //3_1_调用数据访问类从数据库获取分类列表数据
            List<Category> categoryList = categoryDao.findAllCategory();
            //3_2_将categoryList转换为json格式数据
            jsonData = new ObjectMapper().writeValueAsString(categoryList);
            //3_3_将从数据库获取的jsonData分类列表数据存储到redis中，为下一次获取做准备
            jedis.set("categoryList",jsonData);
        }
        //4_返回数据
        return jsonData;
    }
}
