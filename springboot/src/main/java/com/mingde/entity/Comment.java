package com.mingde.entity;


import lombok.Data;


import java.util.List;

@Data
public class Comment {
    private Integer id;
    private String content;
    private Integer userId;
    private Integer pid;
    private String time;
    private Integer fid;
    private String module;
    private Integer rootId;
    private String userName;
    private String userAvatar;

    private String parentUserName;// 父评论的作者姓名（仅子评论有）

    private List<Comment> children;// 子评论列表
}
