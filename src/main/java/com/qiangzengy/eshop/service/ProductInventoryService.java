package com.qiangzengy.eshop.service;

import com.qiangzengy.eshop.entity.ProductInventory;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:25
 */
public interface ProductInventoryService {

    void updateProductInventory(ProductInventory productInventory);

    void remove(ProductInventory productInventory);

    ProductInventory inventoryCnt(Integer productId);

    void setData(ProductInventory productInventory);


}
