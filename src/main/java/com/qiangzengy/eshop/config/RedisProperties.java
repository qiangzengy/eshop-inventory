package com.qiangzengy.eshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午5:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisProperties {

    //private int expireSeconds;
    //private int commandTimeout;
    private String nodes;

}