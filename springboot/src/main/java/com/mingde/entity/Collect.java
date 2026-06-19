package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

@Data
@TableName("collect")
public class Collect {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer fid;
    private Integer userId;
    private String time;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String title;
}
