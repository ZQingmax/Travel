package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tourism")
public class Tourism {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String img;
    private String descr;
    private String content;
    private BigDecimal price;
    private Integer store;
    private String specials;
    private String date;
    @TableField(exist = false)
    private String sort;

    @TableField(exist = false)
    private Integer orderNum = 0;
}
