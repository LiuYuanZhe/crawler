package com.sdust.crawler.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdust.crawler.dto.JobDetailDto;
import com.sdust.crawler.dto.JobDto;
import com.sdust.crawler.mapper.CitycodeMapper;
import com.sdust.crawler.mapper.JobMapper;
import com.sdust.crawler.parser.downloader.HttpClientDownloader;
import com.sdust.crawler.parser.request.HttpGetRequest;
import com.sdust.crawler.parser.response.HttpResponse;
import com.sdust.crawler.service.IRedisService;
import com.sdust.crawler.utils.CrawlerUtils;
import com.sdust.crawler.utils.ElasticClientUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Created by LiuYuanZhe on 18/4/12.
 * 尽量在小的方法体内处理异常,防止小问题影响大流程
 */
@Service
public class BossService extends DefaultCrawlerService{

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private CitycodeMapper citycodeMapper;

//    private WebDriver driver;

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private ElasticClientUtils elasticClientUtils;

    private List<JobDto> jobList;

//    int corePoolSize = 16;
//    int maximumPoolSize = 20;
//    int keepAliveTime = 120;
//    int workQueue = 20;

    private final static int MAXINDEX = 10;

    public static void main(String[] args) {
        new BossService().crawlerBoss();
    }

    private void parseJob(Element element,List<JobDto> list,String citycode){
        /**
         * todo:保存job-id,丢入redis或者mysql,set数据类型
         * https://www.zhipin.com/job_detail/d284229cd101428c1n1_3t68EVU~
         * https://www.zhipin.com/job_dcetail/{$jobid}
         */
        String jobid = element.getElementsByAttribute("data-jid").attr("data-jid");
        boolean ifredis = iRedisService.putJobSet(jobid);
        if (!ifredis){
            logger.info("rredis insert false+"+jobid);
            logger.info("可能已存在数据");
        }
        JobDto jobDto = new JobDto();
//        String data = element.text();
        //0.职位1.薪资2.要求3.公司名称4.公司特点5.发布人6.发布时间
//        String[] ss = data.split("\n+");//去掉null值后的数据
//        zhiwei = CrawlerUtils.findElementByElement(element,"#main > div > div.job-list > ul > li > div > div.info-primary > h3 > a > div.job-title",CrawlerUtils.CSS).getText();
//        price = CrawlerUtils.findElementByElement(element,"#main > div > div.job-list > ul > li > div > div.info-primary > h3 > a > span",CrawlerUtils.CSS).getText();
//        enterprice = CrawlerUtils.findElementByElement(element,"#main > div > div.job-list > ul > li > div > div.info-company > div > h3 > a",CrawlerUtils.CSS).getText();
//        placeexpr = CrawlerUtils.findElementByElement(element,"#main > div > div.job-list > ul > li > div > div.info-primary > p",CrawlerUtils.CSS).getText();
//        feature = CrawlerUtils.findElementByElement(element,"#main > div > div.job-list > ul > li > div > div.info-company > div > p",CrawlerUtils.CSS).getText();
//        jobDto.setJobName(zhiwei);jobDto.setSalary(price);jobDto.setCompany(enterprice);
        String zw[] = element.text().split(" +");
        String zhiwei = zw[0];
        String price = zw[1];
        String enterprice = zw[4];
        String placeexpr = zw[3];
        String feature = zw[5];
        String recuritor = zw[6];
        String rtime = zw[7];
        String location = zw[2];
        jobDto.setJobName(zhiwei);jobDto.setSalary(price);jobDto.setCompany(enterprice);
        jobDto.setFeature(feature);jobDto.setRequirement(placeexpr);jobDto.setJobid(jobid);
        jobDto.setCitycode(citycode);jobDto.setRecruiter(recuritor);jobDto.setDateTime(rtime);
        jobDto.setLocation(location);
        list.add(jobDto);
//        System.out.println("zhiwei:"+zhiwei+";"+"price:"+price+" enterprice:"+enterprice+" placeexpr:"+placeexpr+"  feature:"+feature+" jobid:"+jobid);
        logger.info(jobDto.toString());
    }

    public void crawlerBoss(){
        WebDriver driver;
        try {
            jobList = new ArrayList<>();
            int index = 1;
            //网址规则c0000000-p000000
            String citycode = "101010100";
            String jobcode = "100101";
            String url = "https://www.zhipin.com/c"+citycode+"-p"+jobcode+"/?page="+index;
            //https://www.zhipin.com/c{citycode}-p{jobcode}/?page=?
            driver = CrawlerUtils.initWebDriver();
            driver.get(url);
            for (;index <= MAXINDEX;index++){
//                每个城市最多有十页岗位,循环抓取
//                在日志里面输出一下当前页面url和response.status
                String content = driver.getPageSource();
                Element document = Jsoup.parse(content);
//                String zhiweicss = "#main > div > div.job-list > ul > li > div";
                List<Element> list = document.select("#main > div > div.job-list > ul > li > div");
//                List<WebElement> list =  CrawlerUtils.findElementsByDriver(driver,zhiweicss,CrawlerUtils.CSS);
//                String cssAhref = "#main > div > div.job-list > ul > li > div > div.info-primary > h3 > a";
//                CrawlerUtils.findElementsByDriver(driver,"#main > div > div.job-list > ul > li > div > div.info-primary > h3 > a",CrawlerUtils.CSS);
                for (Element element:list){
                    parseJob(element,jobList,citycode);
                    logger.info("当前队列长度:::::::::::::::::::::::::::::::::",jobList.size());
                }
                insertDB(jobList);
            }
            System.out.println(jobList.size());
//            Document document = Jsoup.parse(driver.getPageSource());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 判断是否入库,如果记录条数大于1000,则入库
     * @param list 插入队列
     * @return 状态
     */
    private boolean insertBoolean(List list){
        return list.size()>=60;
    }

    /**
     * job进入mysql
     * @param list 插入队列
     */
    private void insertDB(List<JobDto> list){
        if (insertBoolean(list)){
            //// TODO: 18/4/24 入库操作
            jobMapper.insertBatch(list);
            System.out.println("插入数据长度:"+list.size());
            list.clear();
        }
    }


    public void crawlerDetail(){
        String jobid;
        TransportClient client = elasticClientUtils.getClient(ElasticClientUtils.ip,ElasticClientUtils.port);
        int i = 1000;
        while (iRedisService.getJobSetSize()>=0&&i>0){
            try {
                i--;
                System.out.println("爬取次数倒数:"+i);
                // TODO: 18/5/9 具体爬取detail内容
                jobid = iRedisService.popTaskid();
                JobDetailDto result = crawlerByHttp(jobid);
                JSONObject json = (JSONObject)JSONObject.toJSON(result);
                if (result.getJobDetail()!=null){
                    logger.info(result.toString());
                    elasticClientUtils.saveJson(json,client);
                }else {
                    continue;
                }
                logger.info("爬取等待");
                Random random = new Random();
                int second = (random.nextInt(8)+5);
                logger.info("wait seconds:"+second);
                CrawlerUtils.sleep(second*1000);
            }catch (Exception ex){
                ex.printStackTrace();
                logger.info("捕捉异常");
            }

        }
    }

    /**
     * crawler by driver
     * 爬取jobdetail 返回一个json对象
     * 0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
     * @param jobId 工作id
     * @return 如果抓取成功,返回json格式0为jobid,如果抓取失败,返回格式json格式0为failure
     * @throws Exception
     */
    private JSON crawlerByDriver(String jobId) throws Exception{
        logger.info("爬取任务开始::::::  爬取id:{}"+jobId);
        JSONObject json = new JSONObject();
        try {
            String jobUrl = "https://www.zhipin.com/job_detail/"+jobId+".html";
            WebDriver driver = CrawlerUtils.initWebDriver();
//        driver.get("https://www.zhipin.com/job_detail/2214055a1e6a10ef1n193ti4ElI~.html");
            driver.get(jobUrl);
            String res = driver.getPageSource();
            WebElement nameEle = CrawlerUtils.findWebElement(driver,"#main > div.job-banner > div > div > div.info-primary > div.name", CrawlerUtils.CSS);
//            WebElement detailEle = CrawlerUtils.findWebElement(driver,"#main > div.job-box > div > div.job-detail > div.detail-content",CrawlerUtils.CSS);
//        1.职位描述2.团队介绍3.公司介绍4.工商信息5.工作地址
            Document document = Jsoup.parse(res);
            List<Element> list = document.select("#main > div.job-box > div > div.job-detail > div.detail-content > div.job-sec");
            json.put("0",jobId);
            json.put("1",nameEle.getText().replaceAll("/"," "));
            json.put("2",list.get(0).text());
            json.put("3",list.get(1).text());
            json.put("4",list.get(2).text());
            json.put("5",list.get(3).text());
            json.put("6",list.get(4).text());
            JobDetailDto jddto = new JobDetailDto();
            jddto.setJobid(jobId);jddto.setJobname(nameEle.getText().replaceAll("/"," "));jddto.setJobDetail(list.get(0).text());
            jddto.setTeamInfo(list.get(1).text());jddto.setCompanyInfo(list.get(2).text());jddto.setGongShangInfo(list.get(3).text());
            jddto.setWorkLocation(list.get(4).text());
            System.out.println();
            CrawlerUtils.destoryWebDriver(driver,"");
            return json;
        }catch (Exception e){
            e.printStackTrace();
            json.put("0","failure");
            return json;
        }

    }

    /**
     * crawler detail by Http 返回json格式
     * 0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
     * @param jobId 工作id
     * @return 如果抓取成功,返回json格式0为jobid,如果抓取失败,返回格式json格式0为failure
     * @throws Exception
     */
    private JobDetailDto crawlerByHttp(String jobId){
        logger.info("爬取任务开始::::::  爬取id:{}"+jobId);
        try {
            String jobUrl = "https://www.zhipin.com/job_detail/"+jobId+".html";
            logger.info("正在爬取url:",jobUrl);
            HttpClientDownloader httpDownloader = new HttpClientDownloader();
            HttpGetRequest request = new HttpGetRequest(jobUrl);
            request = addHeader(request);
            HttpResponse response = httpDownloader.download(request,false);
            if (response.getStatus()==200){
                JobDetailDto jddto = new JobDetailDto();
                logger.info("爬取任务状态:{}",response.getStatus());
                String content = response.getContent();
                Document doc = Jsoup.parse(content);
                Elements nameE = doc.select("#main > div.job-banner > div > div > div.info-primary > div.name");
                nameE.text();
                //0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
                List<Element> list = doc.select("#main > div.job-box > div > div.job-detail > div.detail-content > div.job-sec");
                //        1.职位描述2.团队介绍3.公司介绍4.工商信息5.工作地址
                logger.info("获取的页面队列长度list size:"+list.size());
                if (list.size()==5){
                    jddto.setJobid(jobId);jddto.setJobname(nameE.text().replaceAll("/"," "));jddto.setJobDetail(list.get(0).text());
                    jddto.setTeamInfo(list.get(1).text());jddto.setCompanyInfo(list.get(2).text());jddto.setGongShangInfo(list.get(3).text());
                    jddto.setWorkLocation(list.get(4).text());
                }else if (list.size()==4){
                    jddto.setJobid(jobId);jddto.setJobname(nameE.text().replaceAll("/"," "));
                    jddto.setJobDetail(list.get(0).text());
                    jddto.setCompanyInfo(list.get(1).text());jddto.setGongShangInfo(list.get(2).text());
                    jddto.setWorkLocation(list.get(3).text());
                }else if (list.size()==3){
                    jddto.setJobid(jobId);jddto.setJobname(nameE.text().replaceAll("/"," "));
                    jddto.setJobDetail(list.get(0).text());
                    jddto.setGongShangInfo(list.get(1).text());
                    jddto.setWorkLocation(list.get(2).text());
                }
                return jddto;
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("爬取任务出错,jobid:"+jobId);
            return null;
        }
    }

    /**
     *
     * @param citycode
     * @param jobList
     */
    private void crawlerBossByCitycode(String citycode,List jobList){
        WebDriver driver = null;
        int index = 1;
        String jobcode = "100101";
        try {
            driver = CrawlerUtils.initWebDriver();
            for (;index <= MAXINDEX;index++){
//                每个城市最多有十页岗位,循环抓取
//                在日志里面输出一下当前页面url和response.status
//                logger.info();
                try {
                    //每10s爬取一次页面
                    long startTime = System.currentTimeMillis();
                    CrawlerUtils.sleep(10000);
                    //构造joburl
                    //https://www.zhipin.com/c{citycode}-p{jobcode}/?page=?
                    String url = "https://www.zhipin.com/c"+citycode+"-p"+jobcode+"/?page="+index;
                    driver.get(url);
                    logger.info("crawler page url:{}"+driver.getCurrentUrl());
//                    如果出现输入验证码的页面,图片识别验证码
                    if (driver.getCurrentUrl().contains("captcha")){
                        logger.info("验证码页面出现");
                        capcha(driver,citycode);
                        logger.info("爬取城市code"+citycode + "点击完毕");
                    }
                    String content = driver.getPageSource();
                    long endTime = System.currentTimeMillis();
                    logger.info("获取页面所需时间:"+((endTime-startTime)/1000)+"s");
                    Element document = Jsoup.parse(content);
                    List<Element> list = document.select("#main > div > div.job-list > ul > li > div");
                    for (Element element:list){
                        parseJob(element,jobList,citycode);
                        logger.info("当前队列长度:::::::::::::::::::::::::::::::::"+jobList.size());
                    }
                    insertDB(jobList);
                }catch (Exception ex){
                    ex.printStackTrace();
                    logger.info("当前正在爬取url,爬取出现异常.",driver.getCurrentUrl());
                }
            }
            logger.info("list size:"+jobList.size());
//            Document document = Jsoup.parse(driver.getPageSource());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("爬取页面出现异常,根据上次打印url开始");
        }finally {
            //每1分钟爬取一次城市
            CrawlerUtils.sleep(120000);
            CrawlerUtils.destoryWebDriver(driver,"");
            logger.info(citycode+"编号城市爬取完毕");
        }
    }

    public String getJoblist(){
        List<String> list = citycodeMapper.queryCitycodeBynum(10);
        jobList = new ArrayList<>();
        logger.info(list.size()+":::::::::::"+list.get(0));
        for (String citycode:list){
            try {
                logger.info("爬取城市code"+citycode);
                crawlerBossByCitycode(citycode,jobList);
            }catch (Exception e){
                logger.error("");
            }
        }
        if (jobList.size()!=0){
//            System.out.println(jobList.size());
            jobMapper.insertBatch(jobList);
            logger.info("插入数据长度:"+jobList.size());
            jobList.clear();
        }
        System.out.println();
        return "";
    }

    public void aspecttest(){
        System.out.println("haha");
        System.out.println("**************123");
        logger.info("**************");
    }

}

