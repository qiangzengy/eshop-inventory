package com.qiangzengy.eshop.thread;

import com.qiangzengy.eshop.request.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 *
 * @author qiangzeng
 * @date 2020/4/21 下午8:00
 */
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue){
        this.queue=queue;
    }

    @Override
    public Boolean call(){


        try {
            while(true) {
                // ArrayBlockingQueue
                // Blocking就是说明，如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住
                Request request = queue.take();

                System.out.println("===========日志===========: 工作线程处理请求，商品id=" + request.getProductId());
                // 执行这个request操作
                request.process();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }
}
