package com.mingde.entity;


import lombok.Data;


@Data
public class Article {
    private Integer id;
    private String title;
    private String descr;
    private String cover;
    private String content;
    private Integer readCount;
    private String date;
    private Integer collectCount = 0;


}
