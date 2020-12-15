package com.qiangzengy.eshop.listener;

import com.qiangzengy.eshop.thread.RequestProcessorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午7:12
 *
 *
 * 系统初始化监听器
 */

public class InitListener implements ServletContextListener {

    Logger log = LoggerFactory.getLogger(InitListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        log.info("==============系统初始化监听器=====");
        // 初始化工作线程池和内存队列
        RequestProcessorThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
