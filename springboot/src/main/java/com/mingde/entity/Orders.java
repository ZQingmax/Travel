package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orders")
public class Orders {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String tourismImg;

    /**
     * 商品价格
     */
    private BigDecimal tourismPrice;

    /**
     * 商品ID
     */
    private Integer tourismId;

    /**
     * 购买数量
     */
    private Integer num;

    /**
     * 商品总价
     */
    private BigDecimal total;

    /**
     * 下单人ID
     */
    private Integer userId;

    /**
     * 下单时间
     */
    private String time;

    /**
     * 支付单号
     */
    private String payNo;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 订单状态
     */
    private String status;
}