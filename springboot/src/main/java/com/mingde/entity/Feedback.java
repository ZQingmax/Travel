package com.mingde.entity;

import lombok.Data;

@Data
public class Feedback {
    /**
     * ID
     */
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