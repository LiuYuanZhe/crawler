package com.sdust.crawler.dto;

//import org.springframework.data.elasticsearch.annotations.Document;

import com.sdust.crawler.utils.AppFinal;
import lombok.Data;
import lombok.ToString;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldIndex;
//import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by LiuYuanZhe on 18/4/20.
 */
//@Document(indexName = AppFinal.ESFin.INDEX_NAME,type = AppFinal.ESFin.TYPE_NAME)
@Data
@ToString
public class JobDetailDto {
    //0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
//    @Field(type = FieldType.keyword,store = true)
    private String jobid;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String jobname;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String jobDetail;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String teamInfo;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String companyInfo;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String GongShangInfo;
//    @Field(type = FieldType.text,analyzer = "ik", searchAnalyzer="ik", store = true)
    private String workLocation;
}
