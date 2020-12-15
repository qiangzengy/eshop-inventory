package com.qiangzengy.eshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiangzengy.eshop.dao.RedisDao;
import com.qiangzengy.eshop.entity.User;
import com.qiangzengy.eshop.mapper.UserMapper;
import com.qiangzengy.eshop.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-04-21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisDao redisDao;

    @Override
    public User getCachedUserInfo(Long id) {
        String USER_KEY = "user:id:";
        String userJSON = redisDao.getKey(USER_KEY +id);
        if (StringUtils.isEmpty(userJSON)){
            log.info("缓存中没有数据，去数据查询，更新缓存。。。。。。");
            User byId = this.getById(id);
            redisDao.setData(USER_KEY +id,JSONObject.toJSONString(byId));
            return byId;
        }else {
            log.info("缓存中有数据，直接返回缓存中数据。。。。。");
            return JSONObject.parseObject(userJSON,User.class);
        }
    }
}
