package com.mingde.entity;

import lombok.Data;


import java.math.BigDecimal;

@Data
public class Tourism {
    private Integer id;
    private String name;
    private String img;
    private String descr;
    private String content;
    private BigDecimal price;
    private Integer store;
    private String specials;
    private String date;
    private String sort;

    private Integer orderNum = 0;
}
