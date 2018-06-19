package com.sdust.crawler.controller;

import com.sdust.crawler.crawler.BossService;
import com.sdust.crawler.crawler.CrawlerService;
import com.sdust.crawler.dto.JobDetailDto;
import com.sdust.crawler.mapper.CitycodeMapper;
import com.sdust.crawler.mapper.JobMapper;
import com.sdust.crawler.service.IRedisService;
import com.sdust.crawler.test.GetTimeFromLoggerTxt;
import com.sdust.crawler.test.MainTest;
import com.sdust.crawler.test.TestClient;
import com.sdust.crawler.utils.ElasticClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xuejingwen on 2018/4/8.
 */
@RestController
@RequestMapping("test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

//  @Autowired
//  private PhonePriceMapper phonePriceMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private CitycodeMapper citycodeMapper;

    @Autowired
    private BossService bossService;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private GetTimeFromLoggerTxt matchcitycode;

    @Autowired
    private IRedisService iRedisService;


    @RequestMapping(value = "testConnection")
    @ResponseBody
    public String testHealthy(){
        return "crawler status ok!";
    }

//    @Autowired
//    private ElasticsearchService elasticsearchService;
    /**
     * 爬取手机信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    public String test() throws Exception {
//    System.out.println(phonePriceMapper.queryByConditions(new PhonePriceDto()));
//    PhonePriceDto phonePriceDto = new PhonePriceDto();
//    phonePriceDto.setBrand("123");
//    phonePriceDto.setCreate_time(new Date());
//    PhonePriceDto phonePriceDto2 = new PhonePriceDto();
//    phonePriceDto2.setBrand("123");
//    phonePriceDto2.setCreate_time(new Date());
//    PhonePriceDto phonePriceDto3 = new PhonePriceDto();
//    phonePriceDto3.setBrand("123");
//    phonePriceDto3.setCreate_time(new Date());
//    List<PhonePriceDto> list = new ArrayList<>();
//    list.add(phonePriceDto);
//    list.add(phonePriceDto2);
//    list.add(phonePriceDto3);
//    phonePriceMapper.insertBatch(list);
        long start = System.currentTimeMillis();
        crawlerService.crawlerTime();
        return "HELLO WORLD" + (System.currentTimeMillis() - start);
    }

    /**
     * 爬取boss单个页面
     * @return
     */
    @RequestMapping(value = "/crawlerBoss", method = {RequestMethod.POST, RequestMethod.GET})
    public String boss() {
//    JobDto jobDto = new JobDto();
//    jobDto.setJobName("test");jobDto.setSalary("test");jobDto.setCompany("test");
//    jobDto.setFeature("test");jobDto.setRequirement("test");jobDto.setRecruiter("testrec");
//    List<JobDto> list = new ArrayList<>();
//    list.add(jobDto);
//    jobMapper.insertBatch(list);
        bossService.crawlerBoss();
        return "hello world";
    }

    /**
     * 爬取citycode落入db,包含正则表达式,读取文件io操作
     * @return
     */
    @RequestMapping(value = "/citycodeToDB", method = {RequestMethod.POST, RequestMethod.GET})
    public String matchCityCode(){
        List list = matchcitycode.getCityList();
        citycodeMapper.insertBatchCity(list);
        return "ok";
    }

    /**
     * 爬取boss列表
     * @return
     */
    @RequestMapping(value = "/getcitycodeList", method = {RequestMethod.GET,RequestMethod.POST})
    public String getlist(){
        bossService.getJoblist();
        return "ok";
    }

    /**
     *测试redis与mybatis取得citycode队列
     */
    @RequestMapping(value = "/testredis", method = {RequestMethod.GET,RequestMethod.POST})
    public void testcitycode(){
        List<String> list = citycodeMapper.queryCitycodeBynum(20);
        System.out.println(list.size()+":::::::::::"+list.get(0));
        for (String str:list){
            logger.info(str);
        }
//        boolean flag = iRedisService.putJobSet("liuyuanzhe");
//        System.out.println(flag);
    }

    @RequestMapping(value = "/testaspect", method = {RequestMethod.GET,RequestMethod.POST})
    public void testSize(){
        bossService.aspecttest();
        System.out.println("调用成功");
    }

    @RequestMapping(value = "/testes", method = {RequestMethod.GET,RequestMethod.POST})
    public void testESservice(){
//        boolean flag = elasticsearchService.ping();
//        System.out.println(flag);
//        JobDetailDto json = new MainTest().crawlerByHttp("2214055a1e6a10ef1n193ti4ElI~");
////        List<JobDetailDto> list = new ArrayList<>();
////        list.add(json);
//        elasticsearchService.insertOrUpdateUserInfo(json);
    }

    @RequestMapping(value = "/details")
    public void crawlerDetails(){
        bossService.crawlerDetail();
    }



}
