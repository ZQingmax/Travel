package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

@Data
@TableName("feedback")
public class Feedback {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 反馈人ID
     */
    private Integer userId;

    /**
     * 反馈时间
     */
    private String time;

    /**
     * 管理员回复
     */
    private String reply;
}