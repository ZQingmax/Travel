package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

@Data
@TableName("`user`")
public class User extends Account {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String username; //用户名
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String phone;
    private String email;

}
