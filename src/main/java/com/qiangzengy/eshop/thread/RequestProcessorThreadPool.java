package com.qiangzengy.eshop.thread;

import com.qiangzengy.eshop.request.Request;
import com.qiangzengy.eshop.request.RequestQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *
 * 执行请求线程池
 *
 * @author qiangzeng
 * @date 2020/4/21 下午7:14
 *
 *
 */

public class RequestProcessorThreadPool {

   static Logger log = LoggerFactory.getLogger(RequestQueue.class);


    /**
     * 单例有很多种方式去实现：我采取绝对线程安全的一种方式
     *
     * 静态内部类的方式，去初始化单例
     *
     * @author Administrator
     *
     */
    private static class SingletonHolder {
        private static final RequestProcessorThreadPool threadPool =
                new RequestProcessorThreadPool();
    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestProcessorThreadPool getInstance(){
        log.info("========初始化线程池======");
        return SingletonHolder.threadPool;
    }

    /**
     * 初始化方法
     */
    public static void init(){
        log.info("=====执行init方法========");
        getInstance();
    }


    /**
     * 线程池
     */


    /**
     * 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，
     * 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
     * 说明：Executors返回的线程池对象的弊端如下：
     * 1）FixedThreadPool和SingleThreadPool:
     *   允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
     * 2）CachedThreadPool:
     *   允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
     *
     */

    //ExecutorService threadPool= Executors.newFixedThreadPool(10);

    ThreadPoolExecutor threadPool=new ThreadPoolExecutor(
            10,
            200,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100000),
             Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );


    /**
     * 构造方法
     */
    private RequestProcessorThreadPool() {

        log.info("=======执行RequestProcessorThreadPool构造方法=====");

        RequestQueue requestQueue = RequestQueue.getInstance();

        for(int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }

    }

}
