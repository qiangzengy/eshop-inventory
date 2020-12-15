package com.qiangzengy.eshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qiangzeng
 * @date 2020/4/21 下午9:12
 */

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductInventory implements Serializable {

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer productId;

    @ApiModelProperty(value = "商品库存")
    @TableId(value = "inventory_cnt")
    private long inventoryCnt;


    public ProductInventory() {

    }

    public ProductInventory(Integer productId, long inventoryCnt) {
        this.productId = productId;
        this.inventoryCnt = inventoryCnt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public long getInventoryCnt() {
        return inventoryCnt;
    }

    public void setInventoryCnt(long inventoryCnt) {
        this.inventoryCnt = inventoryCnt;
    }
}
