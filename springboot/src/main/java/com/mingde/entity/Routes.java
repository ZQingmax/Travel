package com.mingde.entity;


import lombok.Data;



@Data
public class Routes {
    private Integer id;
    private String name;
    private Integer days;
    private String tips;
    private String content;
    private String img;
    private String location;
    private String longitude;
    private String latitude;
}
