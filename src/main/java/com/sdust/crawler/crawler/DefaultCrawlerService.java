package com.sdust.crawler.crawler;

import com.sdust.crawler.parser.request.HttpGetRequest;
import com.sdust.crawler.utils.ChaojiyingUtil;
import com.sdust.crawler.utils.CrawlerUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by LiuYuanZhe on 18/5/21.
 */
public abstract class DefaultCrawlerService {

    public static final Logger logger = LoggerFactory.getLogger(BossService.class);


    /**
     *
     * @param request header信息装填
     * @return
     */
    public HttpGetRequest addHeader(HttpGetRequest request){
        request.addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        request.addHeader("accept-encoding","gzip, deflate, br");
        request.addHeader("accept-language","zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        request.addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        request.addHeader("upgrade-insecure-requests","1");
        return request;
    }

    public void capcha(WebDriver capdriver, String citycode){
        try {
            CrawlerUtils.sleep(1000);
            WebElement verifyImg = CrawlerUtils.findElementByDriver(capdriver,"/html/body/div/div/form/p/img", CrawlerUtils.XPATH);
            logger.info("src:"+verifyImg.getAttribute("src"));
            Map<String, String> imgPath = ChaojiyingUtil.putChaptaToLocalReturnCookie("https://www.zhipin.com"+verifyImg.getAttribute("src"),
                    CrawlerUtils.generateCookie(capdriver), CrawlerUtils.getHeaders(),citycode);
            CrawlerUtils.sendKeys(CrawlerUtils.findElementByDriver(capdriver, "//*[@id=\"captcha\"]", CrawlerUtils.XPATH), ChaojiyingUtil.PostPic("1004", imgPath.get("imgPath")));
            CrawlerUtils.findElementByDriver(capdriver,"/html/body/div/div/form/div/button",CrawlerUtils.XPATH).click();
            CrawlerUtils.sleep(1000);
        }catch (Exception e){
            logger.error("验证码处理模块出现错误");
        }
    }
}
