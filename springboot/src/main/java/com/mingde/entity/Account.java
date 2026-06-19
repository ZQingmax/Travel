package com.mingde.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Account {

    @TableField(exist = false)
    private Integer id;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String password;
    @TableField(exist = false)
    private String role;
    @TableField(exist = false)
    private String newPassword;
    @TableField(exist = false)
    private String token;

}
