package com.qiangzengy.eshop.service;

import com.qiangzengy.eshop.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-04-21
 */
public interface UserService extends IService<User> {

    User getCachedUserInfo(Long id);

}
