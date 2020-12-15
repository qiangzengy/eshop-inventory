package com.qiangzengy.eshop.controller;


import com.qiangzengy.eshop.entity.User;
import com.qiangzengy.eshop.service.UserService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiModelProperty(value = "查询")
    @RequestMapping(value = "findAll",method = RequestMethod.GET)
    public List<User> findAll(){
       return userService.list(null);
    }


    @ApiModelProperty(value = "查询缓存")
    @RequestMapping(value = "findRedis/{id}",method = RequestMethod.GET)
    public User findRedis(@PathVariable("id") Long id){
        return userService.getCachedUserInfo(id);
    }

}

