package com.xxxx.api.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookVO2 implements Serializable {

    private String name;

    private String introduction;

    private String price;

    private String author;

    private String type;

    private String publish;

    private String translator;

    private String data;

    private String pic;
}
