package com.qiangzengy.eshop.service.impl;

import com.qiangzengy.eshop.dao.RedisDao;
import com.qiangzengy.eshop.entity.ProductInventory;
import com.qiangzengy.eshop.mapper.ProductInventoryMapper;
import com.qiangzengy.eshop.service.ProductInventoryService;
import org.springframework.boot.context.properties.PropertyMapper;
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

    @Resource
    private RedisDao redisDao;

    /**
     * 修改数据库库存
     * @param productInventory
     */
    @Override
    public void updateProductInventory(ProductInventory productInventory) {

        productInventoryMapper.updateProductInventory(productInventory);

    }

    /**
     * 删除redis缓存
     */

    @Override
    public void remove(ProductInventory productInventory){

        String key="product:inventory:"+productInventory.getProductId();

        redisDao.delete(key);

    }


    /**
     * 查询数据库库存
     * @param productId
     * @return
     */
    @Override
    public ProductInventory inventoryCnt(Integer productId) {
        return productInventoryMapper.inventoryCnt(productId);
    }

    /**
     * 写入缓存
     * @param productInventory
     */
    @Override
    public void setData(ProductInventory productInventory) {
        String key="product:inventory"+productInventory.getProductId();
        String value=String.valueOf(productInventory.getInventoryCnt());
        redisDao.setData(key,value);
    }
}
