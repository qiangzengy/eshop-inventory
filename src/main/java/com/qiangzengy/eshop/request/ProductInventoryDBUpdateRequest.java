package com.qiangzengy.eshop.request;

import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.service.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * （1）删除缓存
 * （2）更新数据库
 * @author qiangzeng
 * @date 2020/4/21 下午9:33
 */
public class  ProductInventoryDBUpdateRequest implements Request {

    static Logger log = LoggerFactory.getLogger(RequestQueue.class);

    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {


        log.info("===========日志===========: 数据库更新请求开始执行，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
        // 删除redis中的缓存
        productInventoryService.remove(productInventory);

        // 为了模拟演示先删除了redis中的缓存，然后还没更新数据库的时候，读请求过来了，这里可以人工sleep一下
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        // 修改数据库中的库存
        productInventoryService.updateProductInventory(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
