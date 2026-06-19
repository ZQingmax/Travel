package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

@Data
@TableName("routes")
public class Routes {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer days;
    private String tips;
    private String content;
    private String img;
    private String location;
    private String longitude;
    private String latitude;
}
