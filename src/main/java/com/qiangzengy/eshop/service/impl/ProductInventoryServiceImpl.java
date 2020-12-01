package com.qiangzengy.eshop.service.impl;

import com.qiangzengy.eshop.dao.RedisDao;
import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.mapper.ProductInventoryMapper;
import com.qiangzengy.eshop.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:26
 */

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {


    @Resource
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisDao redisDao;

    /**
     * 修改数据库库存
     * @param productInventory
     */
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
        System.out.println("已修改：productInventoryId："+productInventory.getProductId());

    }

    /**
     * 删除redis缓存
     */

    @Override
    public void remove(ProductInventory productInventory){
        String key="product:inventory:"+productInventory.getProductId();
        redisDao.delete(key);
        System.out.println("已删除redis缓存：productInventoryId："+productInventory.getProductId());

    }

    /**
     * 写入缓存
     * @param productInventory
     */
    @Override
    public void setData(ProductInventory productInventory) {
        String key="product:inventory:"+productInventory.getProductId();
        String value=String.valueOf(productInventory.getInventoryCnt());
        System.out.println("写入缓存数据 ："+value);
        redisDao.setData(key,value);
    }

    /**
     * 获取商品库存的缓存
     * @param productId
     * @return
     */
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt;
        String key = "product:inventory:" + productId;
        String result = redisDao.getKey(key);
        if(result != null && !"".equals(result)) {
            System.out.println( "获取商品库存的缓存 不为空 ： "+result);
            try {
                inventoryCnt = Long.valueOf(result);
                return new ProductInventory(productId, inventoryCnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }
}
