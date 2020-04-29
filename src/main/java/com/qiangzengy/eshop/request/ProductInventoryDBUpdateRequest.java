package com.qiangzengy.eshop.request;

import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.service.ProductInventoryService;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:33
 */
public class ProductInventoryDBUpdateRequest implements Request {


    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;



    @Override
    public void process() {

        //删除缓存
        productInventoryService.remove(productInventory);
        //修改数据库库存
        productInventoryService.updateProductInventory(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }
}
