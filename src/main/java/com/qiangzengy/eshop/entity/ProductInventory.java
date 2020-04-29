package com.qiangzengy.eshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:12
 */

@Data
public class ProductInventory {

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer productId;

    @ApiModelProperty(value = "商品库存")
    private long inventoryCnt;


    public ProductInventory(Integer productId, long inventoryCnt) {
        this.productId = productId;
        this.inventoryCnt = inventoryCnt;
    }
}
