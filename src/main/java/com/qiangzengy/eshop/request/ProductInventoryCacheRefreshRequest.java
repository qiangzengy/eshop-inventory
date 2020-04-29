package com.qiangzengy.eshop.request;

import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.service.ProductInventoryService;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:38
 */
public class ProductInventoryCacheRefreshRequest implements Request{


    private Integer productId;

    private ProductInventoryService productInventoryService;

    @Override
    public void process() {

        //从数据查询最新的库存数据
        ProductInventory productInventory= productInventoryService.inventoryCnt(productId);
        //将查询的库存数据写入rdis
        productInventoryService.setData(productInventory);

    }


    @Override
    public Integer getProductId() {
        return productId;
    }
}
