package com.xxxx.api.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ShopDto implements Serializable {


    private String name;

    private BigDecimal price;

    private String pic;

    private String size;
}
