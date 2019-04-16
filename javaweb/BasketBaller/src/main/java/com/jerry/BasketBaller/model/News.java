package com.jerry.BasketBaller.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jerry.BasketBaller.utils.Date2LongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class News implements Serializable {
    private Integer id;

    private String title;

    private String article_url;

    private String images;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createtime;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updatetime;


}