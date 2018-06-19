package com.sdust.crawler.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdust.crawler.dto.JobDetailDto;
import com.sdust.crawler.parser.downloader.HttpClientDownloader;
import com.sdust.crawler.parser.request.HttpGetRequest;
import com.sdust.crawler.parser.request.HttpPostRequest;
import com.sdust.crawler.parser.request.HttpRequest;
import com.sdust.crawler.parser.response.HttpResponse;
import com.sdust.crawler.service.PhantomJSDriverNew;
import com.sdust.crawler.utils.ChaojiyingUtil;
import com.sdust.crawler.utils.CrawlerUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiuYuanZhe on 18/5/11.
 */
public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);
    public static void main(String[] args){

        WebDriver driver = null;
        try {
            JobDetailDto jto = new JobDetailDto();
            System.out.println(jto.getJobid()==null);
            //es
//            JobDetailDto jobDetailDto = new MainTest().crawlerByHttp("2214055a1e6a10ef1n193ti4ElI~");
//            System.out.println(JSON.toJSONString(jobDetailDto));
//            JSON json = doPost("http://47.104.0.219:9200",null);
//            System.out.println(jobDetailDto);
//            JSONObject json = (JSONObject)JSONObject.toJSON(jobDetailDto);
//            JSONObject j = doPost("http://47.104.0.219:9200/index/job",json);
//            System.out.println(j);
//            String content = "";
            //capcha
//            String url = "https://www.zhipin.com/captcha/popUpCaptcha?redirect=https%3A%2F%2Fwww.zhipin.com%2Fc101050500-p100101%2F%3Fpage%3D2";
//            driver = CrawlerUtils.initWebDriver();
//            driver.get(url);
//            WebElement verifyImg = CrawlerUtils.findElementByDriver(driver,"/html/body/div/div/form/p/img", CrawlerUtils.XPATH);
//            logger.info("src:"+verifyImg.getAttribute("src"));
//            Map<String, String> imgPath = ChaojiyingUtil.putChaptaToLocalReturnCookie(verifyImg.getAttribute("src"),
//                    CrawlerUtils.generateCookie(driver), CrawlerUtils.getHeaders(),"asd");
//            CrawlerUtils.sendKeys(CrawlerUtils.findElementByDriver(driver, "//*[@id=\"captcha\"]", CrawlerUtils.XPATH), ChaojiyingUtil.PostPic("1004", imgPath.get("imgPath")));
//            CrawlerUtils.findElementByDriver(driver,"/html/body/div/div/form/div/button",CrawlerUtils.XPATH).click();
//            System.out.println();
//            CrawlerUtils.findElementByDriver(driver,"html.standard body.home-body.promotion-four div#wrap div#main.inner.home-inner div.home-box div.home-main div.search-box div.search-form form div.search-form-con div.city-sel",CrawlerUtils.CSS).click();
//            WebElement element = CrawlerUtils.findWebElement(driver,"#main > div > div.home-main > div.search-box > div.search-form > form > div.search-form-con",CrawlerUtils.CSS);
//            System.out.println();
//            String jobUrl = "https://www.zhipin.com/job_detail/"+"2214055a1e6a10ef1n193ti4ElI~"+".html";
//            String jobid = "2214055a1e6a10ef1n193ti4ElI~";
//            JSON json = new MainTest().crawlerByHttp(jobid);
//            System.out.println(json);
//            HttpClientDownloader httpDownloader = new HttpClientDownloader();
//            HttpGetRequest request = new HttpGetRequest(url);
//            request = new MainTest().addHeader(request);
//            HttpResponse response = httpDownloader.download(request,false);
//            String content = response.getContent();
//            Document doc = Jsoup.parse(content);
//            Elements nameE = doc.select("#main > div.job-banner > div > div > div.info-primary > div.name");
//            nameE.text();
//            //0.岗位描述 1.团队介绍 2.公司介绍 3.工商信息 4.工作地址
//            List<Element> elist = doc.select("#main > div.job-box > div > div.job-detail > div.detail-content > div.job-sec");
//            WebDriver driver = CrawlerUtils.initWebDriver();
//            driver.get("https://www.zhipin.com/job_detail/2214055a1e6a10ef1n193ti4ElI~.html");
//            String res = driver.getPageSource();
//            String jobid = "2214055a1e6a10ef1n193ti4ElI~";
//            WebElement nameEle = CrawlerUtils.findWebElement(driver,"#main > div.job-banner > div > div > div.info-primary > div.name", CrawlerUtils.CSS);
//            WebElement detailEle = CrawlerUtils.findWebElement(driver,"#main > div.job-box > div > div.job-detail > div.detail-content",CrawlerUtils.CSS);
////        1.职位描述2.团队介绍3.公司介绍4.工商信息5.工作地址
//            Element document = Jsoup.parse(res);
//            List<Element> list = document.select("#main > div.job-box > div > div.job-detail > div.detail-content > div.job-sec");
//            JSONObject json = new JSONObject();
//            json.put("0",jobid);
//            json.put("1",nameEle.getText().replaceAll("/"," "));
//            json.put("2",list.get(0).text());
//            json.put("3",list.get(1).text());
//            json.put("4",list.get(2).text());
//            json.put("5",list.get(3).text());
//            json.put("6",list.get(4).text());
//            System.out.println();;
        }catch (Exception e){
            e.printStackTrace();
        }
// finally {
//            CrawlerUtils.destoryWebDriver(driver,"");
//        }

    }

//    /**
//     * crawler by driver
//     * 爬取jobdetail 返回一个json对象
//     * 0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
//     * @param jobId
//     * @return 如果抓取成功,返回json格式0为jobid,如果抓取失败,返回格式json格式0为failure
//     * @throws Exception
//     */
//    public JSON crawlerByDriver(String jobId) throws Exception{
//        logger.info("爬取任务开始::::::  爬取id:{}"+jobId);
//        JSONObject json = new JSONObject();
//        try {
//            String jobUrl = "https://www.zhipin.com/job_detail/"+jobId+".html";
//            WebDriver driver = CrawlerUtils.initWebDriver();
////        driver.get("https://www.zhipin.com/job_detail/2214055a1e6a10ef1n193ti4ElI~.html");
//            driver.get(jobUrl);
//
//            String res = driver.getPageSource();
//            WebElement nameEle = CrawlerUtils.findWebElement(driver,"#main > div.job-banner > div > div > div.info-primary > div.name", CrawlerUtils.CSS);
//            WebElement detailEle = CrawlerUtils.findWebElement(driver,"#main > div.job-box > div > div.job-detail > div.detail-content",CrawlerUtils.CSS);
////        1.职位描述2.团队介绍3.公司介绍4.工商信息5.工作地址
//            Document document = Jsoup.parse(res);
//
//            List<Element> list = document.select("#main > div.job-box > div > div.job-detail > div.detail-content > div.job-sec");
//            json.put("0",jobId);
//            json.put("1",nameEle.getText().replaceAll("/"," "));
//            json.put("2",list.get(0).text());
//            json.put("3",list.get(1).text());
//            json.put("4",list.get(2).text());
//            json.put("5",list.get(3).text());
//            json.put("6",list.get(4).text());
//            System.out.println();
//            CrawlerUtils.destoryWebDriver(driver,"");
//            return json;
//        }catch (Exception e){
//            e.printStackTrace();
//            json.put("0","failure");
//            return json;
//        }
//
//    }
//
//    /**
//     * crawler by Http 返回json格式
//     * 0.jobid 1.名称 2.岗位描述 3.团队介绍 4.公司介绍 5.工商信息 6.工作地址
//     * @param jobId
//     * @return 如果抓取成功,返回json格式0为jobid,如果抓取失败,返回格式json格式0为failure
//     * @throws Exception
//     */
    public JobDetailDto crawlerByHttp(String jobId){
        logger.info("爬取任务开始::::::  爬取id:{}"+jobId);
        JSONObject json = new JSONObject();
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
//                json.put("0",jobId);
//                json.put("1",nameE.text().replaceAll("/"," "));
//                json.put("2",list.get(0).text());
//                json.put("3",list.get(1).text());
//                json.put("4",list.get(2).text());
//                json.put("5",list.get(3).text());
//                json.put("6",list.get(4).text());
                jddto.setJobid(jobId);jddto.setJobname(nameE.text().replaceAll("/"," "));jddto.setJobDetail(list.get(0).text());
//                jddto.setTeamInfo(list.get(1).text());
                jddto.setCompanyInfo(list.get(1).text());jddto.setGongShangInfo(list.get(2).text());
                jddto.setWorkLocation(list.get(3).text());
    //            JSON result = new JSONObject();
                return jddto;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("爬取任务出错");
            json.put("0","failure");
            return null;
        }
        json.put("0","failure");
        return null;
    }
//
//    /**
//     *
//     * @param request
//     * @return
//     */
    public HttpGetRequest addHeader(HttpGetRequest request){
        request.addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        request.addHeader("accept-encoding","gzip, deflate, br");
        request.addHeader("accept-language","zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        request.addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        request.addHeader("upgrade-insecure-requests","1");
//        request.addHeader("","");
//        request.addHeader("","");
        return request;
    }
    public static JSONObject doPost(String url,JSONObject json){
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
//            StringEntity s = new StringEntity(json.toString());
//             s.setContentEncoding("UTF-8");
//             s.setContentType("application/json");//发送json数据需要设置contentType
//             post.setEntity(s);
            org.apache.http.HttpResponse res =  client.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                                 HttpEntity entity = res.getEntity();
                                 String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                                 response = JSON.parseObject(result);
                             }
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
        return response;
    }
}
