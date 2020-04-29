package com.qiangzengy.eshop.utils;

import com.qiangzengy.eshop.config.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午6:03
 */

@Configuration
public class RedisCluster {

    @Autowired
    private RedisProperties redisProperties;

    public JedisCluster instance(){
        String [] serverArray=redisProperties.getNodes().split(",");
        Set<HostAndPort> nodes=new HashSet<>();

        for (String ipPort:serverArray){
            String [] ipPortPair=ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));

        }
        return new JedisCluster(nodes);

    }

}
