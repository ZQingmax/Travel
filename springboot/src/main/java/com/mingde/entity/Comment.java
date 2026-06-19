package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

import java.util.List;

@Data
@TableName("comment")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer userId;
    private Integer pid;
    private String time;
    private Integer fid;
    private String module;
    private Integer rootId;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userAvatar;

    @TableField(exist = false)
    private String parentUserName;// 父评论的作者姓名（仅子评论有）

    @TableField(exist = false)
    private List<Comment> children;// 子评论列表
}
