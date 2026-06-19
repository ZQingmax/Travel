package com.mingde.entity;

import lombok.Data;


@Data
public class Account {

    private Integer id;
    private String username;
    private String password;
    private String role;
    private String newPassword;
    private String token;

}
