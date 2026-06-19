package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

@Data
@TableName("article")
public class Article {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String descr;
    private String cover;
    private String content;
    private Integer readCount;
    private String date;
    @TableField(exist = false)
    private Integer collectCount = 0;

}
