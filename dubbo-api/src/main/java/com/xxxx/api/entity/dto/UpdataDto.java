package com.xxxx.api.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdataDto implements Serializable {
    private String old_name;
    private String name;
    private String author;
    private String price;
    private String publish;
    private String data;
    private String translator;
    private String introduction;
    private String type_name;
    private String pic;
}
