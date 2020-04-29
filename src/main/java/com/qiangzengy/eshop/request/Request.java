package com.qiangzengy.eshop.request;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午7:41
 */
public interface Request {

    void process();

    Integer getProductId();

    boolean isForceRefresh();

}
