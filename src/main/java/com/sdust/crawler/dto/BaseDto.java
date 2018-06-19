package com.sdust.crawler.dto;

import lombok.Data;

/**
 * Created by LiuYuanZhe on 18/4/20.
 */
@Data
public class BaseDto {
    private long id;
    private String name;
    private Data updateTime;
    private Data createTime;

    public void get(){
        ClassLoader classLoader = getClass().getClassLoader();
//        File file =
    }
    public static void main(String[] args) {

    }
}
