package com.qiangzengy.eshop.dao;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午6:24
 */
public interface RedisDao {

    void setData(String key,String value);
    String getKey(String key);
    void delete(String key);


}
