package com.qiangzengy.eshop.service;

import com.qiangzengy.eshop.request.Request;

/**
 *
 * 请求路由
 * @author qiangzeng
 * @date 2020/4/22 下午6:42
 */
public interface RequestAsyncProcessService {

    void process(Request request);

}
