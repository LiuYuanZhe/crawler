package com.sdust.crawler.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Created by LiuYuanZhe on 18/4/20.
 */
@Data
@ToString
public class JobDto extends BaseDto {
    private static final long serialVersionUID = 1;

    private long id;
//    工作名称
    private String jobName;
//    薪资
    private String salary;
//    公司姓名
    private String company;
//    职位需要
    private String requirement;
//    公司特点
    private String feature;
//    招聘发布人
    private String recruiter;
//    发布日期
    private String dateTime;
//    jobdetailid
    private String jobid;
//    citycode 根据得到城市代码
    private String citycode;
    //location
    private String location;
}

