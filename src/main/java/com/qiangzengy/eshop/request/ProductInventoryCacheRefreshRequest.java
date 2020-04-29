package com.qiangzengy.eshop.request;

import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.service.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重新加载数据库缓存
 * @author qiangzeng
 * @date 2020/4/21 下午9:38
 */
public class ProductInventoryCacheRefreshRequest implements Request{

    static Logger log = LoggerFactory.getLogger(RequestQueue.class);


    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品库存Service
     */
    private ProductInventoryService productInventoryService;

    /**
     * 是否强制刷新缓存
     */
    private boolean forceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId,
                                               ProductInventoryService productInventoryService,
                                               boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {

        //从数据查询最新的库存数据
        ProductInventory productInventory= productInventoryService.findProductInventory(productId);
        log.info("已查询到商品最新的库存数量，商品id=" + productId + ", 商品库存数量=" + productInventory.getInventoryCnt());
        //将查询的库存数据写入redis
        System.out.println(" ============将查询的库存数据写入redis");
        productInventoryService.setData(productInventory);

    }


    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }
}
