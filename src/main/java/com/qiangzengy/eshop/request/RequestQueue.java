package com.qiangzengy.eshop.request;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求队列
 * @author qiangzeng
 * @date 2020/4/21 下午7:42
 */

@Slf4j
public class RequestQueue {

    private RequestQueue (){}


    //内存队列
    private   List<ArrayBlockingQueue<Request>>queues=new ArrayList<>();

    /**
     * 标识位map
     */
    public Map<Integer, Boolean> getFlagMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 单例
     */
    public static class Singleton{
        private static final RequestQueue queue=new RequestQueue();
    }


    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestQueue getInstance(){
        log.info("==========初始化内存队列======");
       return Singleton.queue;

    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue){

        this.queues.add(queue);

    }


    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize() {
        return queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getInstance(int index){
        return queues.get(index);
    }



}
