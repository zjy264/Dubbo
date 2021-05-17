package com.xxxx.api.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewsDto implements Serializable {
    private String url;
    private String type_name;
}
