package com.sdust.crawler.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by xuejingwen on 2018/4/8.
 */
@Data
public class PhonePriceDto {

  private static final long serialVersionUID = 1;

  private long id;
  private String brand;
  private String model;
  private String price;
  private String status;
  private String alias;
  private String channel;
  private String url;
  private Date create_time;
  private Date update_time;
}
