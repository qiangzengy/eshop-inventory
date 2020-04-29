package com.qiangzengy.eshop.service.impl;

import com.qiangzengy.eshop.request.Request;
import com.qiangzengy.eshop.request.RequestQueue;
import com.qiangzengy.eshop.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author qiangzeng
 * @date 2020/4/22 下午6:43
 */

@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {

        try {
            //可能会多线程调用，在这里去重不太合适，可以放到RequestProcessorThread里做去重
            // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            // 将请求放入对应的队列中，完成路由操作
            queue.put(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取路由到的内存队列
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {

        RequestQueue requestQueue= RequestQueue.getInstance();
        // 先获取productId的hash值
        String key=String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queueSize() - 1) & hash;
        return requestQueue.getInstance(index);

    }

}
