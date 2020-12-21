package com.qiangzengy.eshop.thread;

import com.qiangzengy.eshop.request.ProductInventoryCacheRefreshRequest;
import com.qiangzengy.eshop.request.ProductInventoryDBUpdateRequest;
import com.qiangzengy.eshop.request.Request;
import com.qiangzengy.eshop.request.RequestQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
                //take()方法，删除队列头部元素，如果队列为空，一直阻塞到队列有元素并删除
                Request request = queue.take();

                boolean forceRefresh = request.isForceRefresh();
                /*
                 * 6、读请求去重优化
                 *
                 * 如果一个读请求过来，发现前面已经有一个写请求和一个读请求了，那么这个读请求就不需要压入队列中了
                 *
                 * 因为那个写请求肯定会更新数据库，然后那个读请求肯定会从数据库中读取最新数据，然后刷新到缓存中，自己只要hang一会儿就可以从缓存中读到数据了
                 *
                 * 7、空数据读请求过滤优化
                 *
                 * 可能某个数据，在数据库里面压根儿就没有，那么那个读请求是不需要放入内存队列的，而且读请求在controller那一层，直接就可以返回了，不需要等待
                 *
                 * 如果数据库里都没有，就说明，内存队列里面如果没有数据库更新的请求的话，一个读请求过来了，就可以认为是数据库里就压根儿没有数据吧
                 *
                 * 如果缓存里没数据，就两个情况，第一个是数据库里就没数据，缓存肯定也没数据; 第二个是数据库更新操作过来了，先删除了缓存，此时缓存是空的，但是数据库里是有的
                 *
                 * 但是的话呢，我们做了之前的读请求去重优化，用了一个flag map，只要前面有数据库更新操作，flag就肯定是存在的，你只不过可以根据true或false，判断你前面执行的是写请求还是读请求
                 *
                 * 但是如果flag压根儿就没有呢，就说明这个数据，无论是写请求，还是读请求，都没有过
                 *
                 * 那这个时候过来的读请求，发现flag是null，就可以认为数据库里肯定也是空的，那就不会去读取了
                 *
                 * 或者说，我们也可以认为每个商品有一个最最初始的库存，但是因为最初始的库存肯定会同步到缓存中去的，有一种特殊的情况，就是说，商品库存本来在redis中是有缓存的
                 *
                 * 但是因为redis内存满了，就给干掉了，但是此时数据库中是有值得
                 *
                 * 那么在这种情况下，可能就是之前没有任何的写请求和读请求的flag的值，此时还是需要从数据库中重新加载一次数据到缓存中的
                 */

                // 先做读请求的去重
                if(!forceRefresh) {
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    if(request instanceof ProductInventoryDBUpdateRequest) {
                        // 如果是一个更新数据库的请求，那么就将那个productId对应的标识设置为true
                        flagMap.put(request.getProductId(), true);
                    } else if(request instanceof ProductInventoryCacheRefreshRequest) {
                        Boolean flag = flagMap.get(request.getProductId());

                        /*可能某个数据，在数据库里面压根儿就没有，那么那个读请求是不需要放入内存队列的，而且读请求在controller那一层，直接就可以返回了，不需要等待

                        如果数据库里都没有，就说明，内存队列里面如果没有数据库更新的请求的话，一个读请求过来了，就可以认为是数据库里就压根儿没有数据吧

                        如果缓存里没数据，就两个情况，第一个是数据库里就没数据，缓存肯定也没数据; 第二个是数据库更新操作过来了，先删除了缓存，此时缓存是空的，但是数据库里是有的

                        但是的话呢，我们做了之前的读请求去重优化，用了一个flag map，只要前面有数据库更新操作，flag就肯定是存在的，你只不过可以根据true或false，判断你前面执行的是写请求还是读请求

                        但是如果flag压根儿就没有呢，就说明这个数据，无论是写请求，还是读请求，都没有过

                        那这个时候过来的读请求，发现flag是null，就可以认为数据库里肯定也是空的，那就不会去读取了

                        或者说，我们也可以认为每个商品有一个最最初始的库存，但是因为最初始的库存肯定会同步到缓存中去的，有一种特殊的情况，就是说，商品库存本来在redis中是有缓存的

                        但是因为redis内存满了，就给干掉了，但是此时数据库中是有值得

                        那么在这种情况下，可能就是之前没有任何的写请求和读请求的flag的值，此时还是需要从数据库中重新加载一次数据到缓存中的*/
                        if(Objects.isNull(flag)) {
                            //此时第一个读请求过来，将flagMap设置为false
                            flagMap.put(request.getProductId(), false);
                        }
                        // 如果是缓存刷新的请求，那么就判断，如果标识不为空，而且是true，就说明之前有一个这个商品的数据库更新请求
                        if(flag != null && flag) {
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
                        // 说明前面已经有一个数据库更新请求+一个缓存刷新请求了，大家想一想
                        if(flag != null && !flag) {
                            // 对于这种读请求，直接就过滤掉，不要放到后面的内存队列里面去了
                            return true;
                        }
                    }
                }
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
