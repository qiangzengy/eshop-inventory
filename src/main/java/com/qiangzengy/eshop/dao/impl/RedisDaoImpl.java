package com.qiangzengy.eshop.dao.impl;

import com.qiangzengy.eshop.dao.RedisDao;
import com.qiangzengy.eshop.utils.RedisCluster;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午6:25
 */

@Component
public class RedisDaoImpl implements RedisDao {

    @Resource
    private RedisCluster redisCluster;

    @Override
    public void setData(String key, String value) {
        redisCluster.instance().set(key,value);
    }

    @Override
    public String getKey(String key) {
       return redisCluster.instance().get(key);
    }


    @Override
    public void delete(String key) {
        redisCluster.instance().del(key);
    }
}
