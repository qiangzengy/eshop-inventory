package com.qiangzengy.eshop.service;

import com.qiangzengy.eshop.entity.ProductInventory;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:25
 */
public interface ProductInventoryService {

    /**
     * 更新商品库存
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除Redis中的商品库存的缓存
     * @param productInventory 商品库存
     */
    void remove(ProductInventory productInventory);


    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    void setData(ProductInventory productInventory);

    /**
     * 获取缓存
     * @param productId
     * @return
     */
    ProductInventory getProductInventoryCache(Integer productId);


    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    ProductInventory findProductInventory(Integer productId);
}
