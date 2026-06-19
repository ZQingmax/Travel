package com.mingde.entity;


import lombok.Data;


@Data
public class User extends Account {

    /** 主键ID */
    private Integer id;
    private String username; //用户名
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String phone;
    private String email;

}
