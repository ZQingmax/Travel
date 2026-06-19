package com.mingde.entity;


import lombok.Data;


@Data
public class Travels {
    private Integer id;
    private String title;
    private String cover;
    private String descr;
    private String content;
    private Integer UserId;
    private String time;
    private Integer readCount;
    private String startDate;
    private Integer money;
    private Integer days;
    private String location;
    private String status;
    private String userName;
    private String userAvatar;
    private Integer praiseCount = 0;

    private String sort;

}
