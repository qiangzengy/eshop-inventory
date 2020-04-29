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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer productId;

    @ApiModelProperty(value = "商品库存")
    private long inventoryCnt;


    public ProductInventory() {

    }

    public ProductInventory(Integer productId, long inventoryCnt) {
        this.productId = productId;
        this.inventoryCnt = inventoryCnt;
    }
}
