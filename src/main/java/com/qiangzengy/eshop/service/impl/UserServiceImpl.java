package com.qiangzengy.eshop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiangzengy.eshop.dao.RedisDao;
import com.qiangzengy.eshop.entity.User;
import com.qiangzengy.eshop.mapper.UserMapper;
import com.qiangzengy.eshop.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-04-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisDao redisDao;

    @Override
    public User getCachedUserInfo() {
        redisDao.setData("cached_user_lisi", "{\"name\": \"lisi\", \"age\":28}");
        String userJSON = redisDao.getKey("cached_user_lisi");
        JSONObject userJSONObject = JSONObject.parseObject(userJSON);
        User user = new User();
        user.setName(userJSONObject.getString("name"));
        user.setAge(userJSONObject.getInteger("age"));
        return user;

    }
}
