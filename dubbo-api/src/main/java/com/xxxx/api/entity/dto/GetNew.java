package com.xxxx.api.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetNew  implements Serializable {
    private String bookname;

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
