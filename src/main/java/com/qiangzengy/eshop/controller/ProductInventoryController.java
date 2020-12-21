package com.qiangzengy.eshop.controller;

import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.request.ProductInventoryCacheRefreshRequest;
import com.qiangzengy.eshop.request.ProductInventoryDBUpdateRequest;
import com.qiangzengy.eshop.request.Request;
import com.qiangzengy.eshop.service.ProductInventoryService;
import com.qiangzengy.eshop.service.RequestAsyncProcessService;
import com.qiangzengy.eshop.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品库存Controller
 *
 * 模拟的场景：
 *
 *（1）一个更新商品库存的请求过来，然后此时会先删除redis中的缓存，然后模拟卡顿5秒钟
 *（2）在这个卡顿的5秒钟内，我们发送一个商品缓存的读请求，因为此时redis中没有缓存，就会来请求将数据库中最新的数据刷新到缓存中
 *（3）此时读请求会路由到同一个内存队列中，阻塞住，不会执行
 *（4）等5秒钟过后，写请求完成了数据库的更新之后，读请求才会执行
 *（5）读请求执行的时候，会将最新的库存从数据库中查询出来，然后更新到缓存中
 *
 * 如果是不一致的情况，可能会出现说redis中还是库存为100，但是数据库中也许已经更新成了库存为99了
 *
 * 现在做了一致性保障的方案之后，就可以保证说，数据是一致的
 *
 * 包括这个方案在内，还有后面的各种解决方案，首先都是针对我自己遇到过的特殊场景去设计的
 *
 * 可能这个方案就不一定完全100%适合其他的场景，也许还要做一些改造才可以，本来你学习一个课程，它就不是万能的，你可能需要嚼烂了，吸收了，改造了，才能应用到自己的场景中
 *
 * 另外一个，也有一种可能，就是说方案比较复杂，即使我之前做过，也许有少数细节我疏忽了，没有在课程里面讲解，导致解决方案有一些漏洞或者bug
 *
 * 我讲解方案，主要是讲解架构思想，或者是设计思想，技术思想，有些许漏洞，希望大家谅解
 *
 */


@RestController
@Slf4j
public class ProductInventoryController {

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;
    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * 更新商品库存
     */
    @RequestMapping("/updateProductInventory")
    public Response updateProductInventory(ProductInventory productInventory) {
        log.info("===========日志===========: 接收到更新商品库存的请求，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
        Response response;
        try {
            Request request = new ProductInventoryDBUpdateRequest(
                    productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }

        return response;
    }


    /**
     * 获取商品库存
     */
    @RequestMapping("/getProductInventory")
    public ProductInventory getProductInventory(Integer productId) {
        log.info("===========日志===========: 接收到一个商品库存的读请求，商品id=" + productId);
        ProductInventory productInventory;

        try {
            Request request = new ProductInventoryCacheRefreshRequest(
                    productId, productInventoryService, false);
            requestAsyncProcessService.process(request);

            // 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
            // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime ;
            long waitTime = 0L;

            // 等待超过200ms没有从缓存中获取到结果
            while (waitTime <= 200) {
                // 面向用户的读请求控制在200ms

                /*
                 * 测试 由于先前测试好多遍，都无法将数据写入缓存，根据debug根据发现，hang时间的问题（需要hang的久一点)
                 * 如果一个读请求过来，发现前面已经有一个写请求和一个读请求了，那么这个读请求就不需要压入队列中了
                 * 因为那个写请求肯定会更新数据库，然后那个读请求肯定会从数据库中读取最新数据，然后刷新到缓存中，
                 * 自己只要hang一会儿就可以从缓存中读到数据了
                 */
                /*if(waitTime > 180000) {
                    break;
                }*/

                // 尝试去redis中读取一次商品库存的缓存数据
                productInventory = productInventoryService.getProductInventoryCache(productId);

                // 如果读取到了结果，那么就返回
                if (productInventory != null) {
                    log.info("===========日志===========: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
                    return productInventory;
                } else {// 如果没有读取到结果，那么等待一段时间
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }

            // 直接尝试从数据库中读取数据
            productInventory = productInventoryService.findProductInventory(productId);

            /*
             * 8、深入的去思考优化代码的漏洞
             *
             * 一个读请求过来，将数据库中的数刷新到了缓存中，flag是false，然后过了一会儿，redis内存满了，自动删除了这个额缓存
             *
             * 下一次读请求再过来，发现flag是false，就不会去执行刷新缓存的操作了
             *
             * 而是hang在哪里，反复循环，等一会儿，发现在缓存中始终查询不到数据，然后就去数据库里查询，就直接返回了
             *
             * 这种代码，就有可能会导致，缓存永远变成null的情况
             *
             * 最简单的一种，就是在controller这一块，如果在数据库中查询到了，就刷新到缓存里面去，以后的读请求就又可以从缓存里面读了
             *
             */
            if(productInventory != null) {


                // 将缓存刷新一下
                // 这个过程，实际上是一个读操作的过程，但是没有放在队列中串行去处理，还是有数据不一致的问题
                request = new ProductInventoryCacheRefreshRequest(
                        productId, productInventoryService, true);
                request.process();

                //刷新缓存，为了解决lru过期产生的问题
                requestAsyncProcessService.process(request);

                // 代码会运行到这里，只有三种情况：
                // 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false
                // 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
                // 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，基本是比较坑了）
                // 所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
                // 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库

            }
            return productInventory;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
