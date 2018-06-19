package com.sdust.crawler.utils;


import com.google.common.base.Function;
import com.sdust.crawler.service.PhantomJSDriverNew;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrawlerUtils {
    public static AtomicBoolean switchFlag = new AtomicBoolean(true);
    public final static String CSS = "css";
    public final static String XPATH = "xpath";
    public final static String ID = "id";

    public static WebElement findElementByElement(WebElement element, String selectString, String type) {
        WebElement result = null;
        try {
            if (type.equals(CSS)) {
                result = element.findElement(By.cssSelector(selectString));
            } else if (type.equals(XPATH)) {
                result = element.findElement(By.xpath(selectString));
            }
            return result;
        } catch (NoSuchElementException e) {
            return result;
        }

    }

    public static List<WebElement> findElementsByDriver(WebDriver driver, String selectString, String type) {
        List<WebElement> elements = new ArrayList<>();
        try {
            if (type.equals(CSS)) {
                elements = driver.findElements(By.cssSelector(selectString));
            } else if (type.equals(XPATH)) {
                elements = driver.findElements(By.xpath(selectString));
            }
            return elements;
        } catch (NoSuchElementException e) {
            return elements;
        }
    }

    public static WebElement findElementByDriver(WebDriver driver, String selectString, String type) {
        WebElement result = null;
        try {
            if (type.equals(CSS)) {
                result = driver.findElement(By.cssSelector(selectString));
            } else if (type.equals(XPATH)) {
                result = driver.findElement(By.xpath(selectString));
            } else if (type.equals(ID)) {
                result = driver.findElement(By.id(selectString));
            }
            return result;
        } catch (NoSuchElementException e) {
            return result;
        }
    }

    public static List<WebElement> findElementsByElement(WebElement element, String selectString, String type) {
        List<WebElement> elements = new ArrayList<>();
        try {
            if (type.equals(CSS)) {
                elements = element.findElements(By.cssSelector(selectString));
            } else if (type.equals(XPATH)) {
                elements = element.findElements(By.xpath(selectString));
            }
            return elements;
        } catch (NoSuchElementException e) {
            return elements;
        }
    }

    public static void sleep(long time) {
        try {
            System.out.println("爬取boss延时:"+time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static String getWebelementAttribute(WebElement webElement, String attributeName) {
        try {
            String attributeValue = (webElement == null) ? "" : webElement.getAttribute(attributeName);
            System.out.println("attributeValue======================" + attributeValue);
            return attributeValue;
        } catch (Exception e) {
            return "";
        }
    }

    public static WebDriver getDriver() {

        Capabilities caps = new DesiredCapabilities();
        ((DesiredCapabilities) caps).setJavascriptEnabled(true);
        // ((DesiredCapabilities)
        // caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
        // "D:\\development\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
//		((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
//				"/usr/soft/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                SpiderVariable.PHANTOMJS_PATH);
        ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");


        WebDriver driver = new PhantomJSDriver(caps);
//		 driver= new ChromeDriver();
        return driver;
    }

    public static WebElement findWebElement(WebDriver driver, final String selectString, final String type) {
        return new WebDriverWait(driver, 10).until(new ExpectedCondition<WebElement>() {

            @Override
            public WebElement apply(WebDriver driver) {
                // TODO Auto-generated method stub
                return CrawlerUtils.findElementByDriver(driver, selectString, type);
            }
        });
    }

    /**
     * mac开发环境driver生成
     * @return webdriver-phantomjs
     * @throws Exception
     */
    public static WebDriver initWebDriver() throws Exception {
//        System.setProperty("webdriver.chrome.driver", pathContext.getDriver_path());
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, pathContext.getDriver_path());
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "resourceTimeout", 100000);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
                "--web-security=false",
                "--ignore-ssl-errors=true",
                "--disk-cache=true",
                "--webdriver-loglevel=NONE"/*,
            "--handlesAlerts=true"*/
        });

        WebDriver driver = null;
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/rqw1991/Downloads/javaother/" +
                "毕业设计/2.1.1/bin/phantomjs");
        PhantomJSDriverService pservice = PhantomJSDriverService.createDefaultService(caps);
        driver = new PhantomJSDriverNew(pservice, caps);
        driver.manage().timeouts().setScriptTimeout(2, TimeUnit.SECONDS);
        return driver;
    }

    public static void destoryWebDriver(WebDriver driver, String taskHashcode) {
        if (driver != null) {
            driver.close();
            driver.quit();
            driver = null;
        }
//        if (StringUtils.isNotBlank(taskHashcode) &&
//                StaticTaskDriver.get(taskHashcode) != null) {
//            StaticTaskDriver.remove(taskHashcode);
//        }
    }

    public static String getWebElementText(WebElement ele) {
        try {
            return ele.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取enabled的元素，如果超时，返回null
     *
     * @return
     */
    public static WebElement findEnabledWebelement(WebDriver driver, By by, long seconds) {
        try {
            return new WebDriverWait(driver,seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
//            return new WebDriverWait(driver, 10).until(new ExpectedCondition<WebElement>() {
//                @Override
//                public WebElement apply(WebDriver driver) {
//                    // TODO Auto-generated method stub
//                    WebElement element = CrawlerUtils.findElementByDriver(driver,
//                            selectString,
//                            selectType);
//                    if (element.isEnabled()) {
//                        return element;
//                    } else {
//                        return null;
//                    }
//
//                }
//            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 等待10s
     *
     * @param driver
     * @param selectString
     * @param selectType
     * @return
     */
    public static WebElement findWaitElement(WebDriver driver, final String selectString, final String selectType) {
        try {
            return new WebDriverWait(driver, 10).until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
//                    CrawlerUtils.sleep(5000);
                    return CrawlerUtils.findElementByDriver(driver, selectString, selectType);
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    public static void sendKeys(WebElement element,String value){
        element.clear();
        element.sendKeys(value);
    }

    /**
     * 判断元素是否在指定时间内存在。
     * 只要元素出现在dom结构中（不管属性是显示还是隐藏） 马上返回true
     * 在指定时间仍不存在与dom结构则返回false。
     * 适用于ajax
     *
     * @param by 元素
     * @param seconds 指定秒数
     * @return 出现返回true 否则返回false
     */
//    public static boolean waitForElementPresence(WebDriver driver,final By by, int seconds) {
//        try {
//            new WebDriverWait(driver, seconds).until(ExpectedConditions.presenceOfElementLocated(by));
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    /**
     * 判断元素在指定时间是否显示
     * 元素是否在指定时间内显示（存在dom结构且属性为显示）马上返回true
     * 如果到指定时间仍未显示（不存在与dom结构 或者存在于dom结构但属性为‘隐藏’）则返回false
     * 适用于ajax
     *
     * @param by 元素
     * @param seconds 指定秒数
     * @return 出现返回true 否则返回false
     */
    public static boolean waitForElementVisible(WebDriver driver,final By by, int seconds) {
        try {
            new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断元素是否在指定时间内隐藏或者消失
     * 如果元素消失(不存在于dom结构 或者属性为 ‘隐藏’)则立刻返回true
     * 如果指定时间后元素仍然存在（存在于dom结构且属性为‘显示’）则返回false
     *
     * @param by 元素
     * @param seconds 秒数
     * @return
     */
    public static boolean waitForElementInvisible(WebDriver driver,final By by, int seconds) {
        try {
            new WebDriverWait(driver, seconds).until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断页面是否加载完成
     * if document.readyState = complete?
     * @param driver
     */
    public static void waitForPageLoad(WebDriver driver){
        Function<WebDriver,Boolean> waitFn = new Function<WebDriver, Boolean>(){
            @Override
            public Boolean apply(WebDriver driver){
                return ((JavascriptExecutor)driver).executeScript("return document.readyState")
                        .equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(waitFn);
    }

    /**
     * 根据driver生成header
     * @param driver
     * @return
     * @throws Exception
     */
    public static String generateCookie(WebDriver driver) throws Exception {
        Set<Cookie> cookies = driver.manage().getCookies();
        StringBuffer buffer = new StringBuffer("");
//        cookies = driver.manage().getCookies();
        if (cookies != null && !cookies.isEmpty()) {
            for (Cookie cookie : cookies) {
                buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
        }

        String result = "";
        if (buffer.length() > 0) {
            result = buffer.substring(0, buffer.length() - 2);
        }
        return result;
    }

    /**
     * 构造httpheader
     * @return
     */
    public static Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap();
        map.put("Accept", "*/*");
        map.put("Accept-Encoding", "gzip, deflate, br");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        map.put("Connection", "keep-alive");
        map.put("Host", "www.zhipin.com");
//        map.put("Referer", "https://gz.ac.10086.cn/aicas/login");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:59.0) Gecko/20100101 Firefox/59.0");
        return map;
    }
}
