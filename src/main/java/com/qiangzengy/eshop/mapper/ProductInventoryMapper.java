package com.qiangzengy.eshop.mapper;

import com.qiangzengy.eshop.entity.ProductInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:23
 */

@Mapper
public interface ProductInventoryMapper {

    void updateProductInventory(ProductInventory productInventory);

    ProductInventory inventoryCnt(@Param("productId")Integer productId);


}
