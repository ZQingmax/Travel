package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;

import lombok.Data;

@Data
@TableName("admin")
public class Admin extends Account {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 账号 */
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String phone;
    private String email;

}
