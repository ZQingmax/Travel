package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

@Data
@TableName("travels")
public class Travels {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String cover;
    private String descr;
    private String content;
    @TableField("user_id")
    private Integer UserId;
    private String time;
    private Integer readCount;
    private String startDate;
    private Integer money;
    private Integer days;
    private String location;
    private String status;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userAvatar;
    @TableField(exist = false)
    private Integer praiseCount = 0;

    @TableField(exist = false)
    private String sort;

}
