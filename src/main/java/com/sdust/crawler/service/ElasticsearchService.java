package com.sdust.crawler.service;
//import org.elasticsearch.action.ActionFuture;
//import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
//import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.cluster.health.ClusterHealthStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.query.IndexQuery;
//import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by LiuYuanZhe on 18/5/23.
// */
////@Component
//public class ElasticsearchService {
//    private Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//    @Autowired
//    private Client esClient;
//
//    @PostConstruct
//    public void init(){
//        if (!elasticsearchTemplate.indexExists(AppFinal.ESFin.INDEX_NAME)) {
//            elasticsearchTemplate.createIndex(AppFinal.ESFin.INDEX_NAME);
//        }
//        elasticsearchTemplate.putMapping(JobDetailDto.class);
//    }
//
//    public boolean insertOrUpdateTaskInfo(List<JobDetailDto> jobDetailDtoList) {
//        List<IndexQuery> queries = new ArrayList<IndexQuery>();
//        for (JobDetailDto jobDetailDto : jobDetailDtoList) {
//            IndexQuery indexQuery = new IndexQueryBuilder().withId(jobDetailDto.getJobid()).withObject(jobDetailDto).build();
//            queries.add(indexQuery);
//        }
//        elasticsearchTemplate.bulkIndex(queries);
//        return true;
//    }
//
//    public boolean insertOrUpdateUserInfo(JobDetailDto jobDetailDto) {
//        try {
//            IndexQuery indexQuery = new IndexQueryBuilder().withId(jobDetailDto.getJobid()).withObject(jobDetailDto).build();
//            elasticsearchTemplate.index(indexQuery);
//            return true;
//        } catch (Exception e) {
////            logger.error("insert or update user info error.", e);
//            return false;
//        }
//    }
//
//    public boolean ping() {
//        try {
//            ActionFuture<ClusterHealthResponse> health = esClient.admin().cluster().health(new ClusterHealthRequest());
//            ClusterHealthStatus status = health.actionGet().getStatus();
//            if (status.value() == ClusterHealthStatus.RED.value()) {
//                throw new RuntimeException("elasticsearch cluster health status is red.");
//            }
//            return true;
//        } catch (Exception e) {
////            logger.error("ping elasticsearch error.", e);
//            return false;
//        }
//    }
//}
