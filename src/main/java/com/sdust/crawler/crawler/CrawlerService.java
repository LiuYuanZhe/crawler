package com.sdust.crawler.crawler;

import com.sdust.crawler.dto.PhonePriceDto;
import com.sdust.crawler.mapper.PhonePriceMapper;
import com.sdust.crawler.utils.HttpUtils;
import com.sdust.crawler.utils.JSoupUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuejingwen on 2018/4/8.
 */
@Service
public class CrawlerService {

  @Autowired
  private PhonePriceMapper phonePriceMapper;

  private final static int TOTALPAGE = 300;

  private final static String MODELKEY = "model";
  private final static String ALIASKEY = "alias";

  private final static String BAIDUURL = "https://www.baidu.com/s?ie=utf-8&" +
      "mod=1&isbd=1&isid=c57ffca1000138c3&wd={KEYWORD}&pn=00&oq={KEYWORD}&ie=utf-8&rsv_idx=1&rsv_pq=c57ffca1000138c3&rsv_t=dbe6egzIX%2FjjF%2FsPiUYWOsvK%2F6z8jlBWvv%2BQx1JilL0ZQBxBSgcN8bMXpmY&" +
      "bs={KEYWORD}&rsv_sid=undefined&_ss=1&clist=db84f0b9ce4df0ef%09db84eb07cd3c647b%09db84ede0cdc52ab5&hsug=&f4s=1&csor=0&_cr1=27257";

  private final static String CHANNEL = "中关村在线";
  private final static String QUERYKEY = "detail.zol.com.cn/cell";

  int corePoolSize = 16;
  int maximumPoolSize = 20;
  int keepAliveTime = 120;
  int workQueue = 20;


  public static void main(String[] args) {
    String str = "redmi 4 note "+ CHANNEL;
    String url = BAIDUURL.replaceAll("\\{KEYWORD\\}", URLEncoder.encode(str).replaceAll("\\+", "%20"));
    Map<String,String> headers = new HashMap<>();
    headers.put("Accept", "*/*");
//    headers.put("Accept-Encoding", "gzip, deflate, br");
    headers.put("Accept-Language", "zh-CN,zh;q=0.9");
    headers.put("Connection", "keep-alive");
    headers.put("Host", "www.baidu.com");
    headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
    headers.put("X-Requested-With", "XMLHttpRequest");
    String response = HttpUtils.get(url, headers);
    Document document = Jsoup.parse(response);
    Elements elements = JSoupUtils.findElementsByClass(document, "c-showurl");
    for(Element element : elements) {
      String detail = JSoupUtils.findTextByElement(element);
      if(detail != null && detail.contains(QUERYKEY)) {
        url = JSoupUtils.findAttributeValueByElement(element, "href");
        System.out.println(url);
        System.out.println(HttpUtils.get(url, headers));
        break;
      }
    }
  }

  public void crawlerByRequest(String brand, String model) {
    try {
//    1.拼装百度查询条件
      String keyworkd = brand + " " + model;
    } catch(Exception e) {

    }
  }

  public void crawlerTime() throws Exception {
    List<PhonePriceDto> dataList = new ArrayList<>();
    List<Map<String, String>> brandList = crawlerBrand();
    if (brandList == null || brandList.isEmpty()) {
      return;
    }
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(workQueue));
    long start = System.currentTimeMillis();
    threadPool.execute(new Runnable() {
      @Override
      public void run() {
        for (Map<String, String> brandMap : brandList){
          for (int i = 0; i < TOTALPAGE; i++) {
            String brand = brandMap.get("brand");
            String url = brandMap.get("url");
            if (i > 0) {
              url = url.substring(0, url.lastIndexOf("_")) + "_" + (i + 1) + ".html";
            }
            System.out.println(url);
            try {
              String response = HttpUtils.get(url, null);
              Document documnet = Jsoup.parse(response);
              Elements elements = documnet.getElementsByAttribute("data-follow-id");
              if (elements == null || elements.isEmpty()) {
                break;
              }
              System.out.println(elements.size());
              for (Element element : elements) {
                PhonePriceDto phonePriceDto = parse(element, brand);
                dataList.add(phonePriceDto);
              }
          if (dataList.size() > 1000) {
            phonePriceMapper.insertBatch(dataList);
            System.out.println("insert数据,数据长度:"+dataList.size());
            dataList.clear();
          }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    });
    threadPool.shutdown();
    while(true) {
      if(threadPool.isTerminated()) {
        System.out.println(dataList.size());
        System.out.println("执行完毕耗时时间:::::"+(System.currentTimeMillis()-start));
        break;
      }
    }
  }


  /**
   * 获取中关村所有手机型号
   *
   * @return
   */
  private List<Map<String, String>> crawlerBrand() {
    List<Map<String, String>> list = new ArrayList<>();
    try {
      String response = HttpUtils.get("http://detail.zol.com.cn/cell_phone_index/subcate57_list_1.html", null);
      Document document = Jsoup.parse(response);
      Elements elements = document.getElementById("J_BrandAll").getElementsByTag("a");
      for (Element element : elements) {
        try {
          Map<String, String> map = new HashMap<>();
          String brand = element.text();
          map.put("brand", brand);
          System.out.println("当前手机品牌:::::" + brand);
          String url = "http://detail.zol.com.cn/" + element.attr("href");
          System.out.println("当前手机url:::::" + url);
          map.put("url", url);
          list.add(map);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  private PhonePriceDto parse(Element element, String brand) {
    PhonePriceDto phonePriceDto = new PhonePriceDto();
    Date date = new Date();
    phonePriceDto.setCreate_time(date);
    phonePriceDto.setUpdate_time(date);
    try {
      phonePriceDto.setChannel("中关村");
      phonePriceDto.setBrand(brand);
      Element titelEle = JSoupUtils.findElementFromElements(element.getElementsByTag("a"), 1);
      String url = JSoupUtils.findAttributeValueByElement(titelEle, "href");
      phonePriceDto.setUrl("http://detail.zol.com.cn" + url);
      String title = titelEle.html();
      String alias = null;
      try {
        title = title.substring(0, title.indexOf("<")).trim();
      } catch (Exception e) {

      }
      if (StringUtils.isBlank(title)) {
//        Map<String, String> modelMap = crawlerModelAndAlias("http://detail.zol.com.cn" + url);
//        title = modelMap.get(MODELKEY);
//        alias = modelMap.get(ALIASKEY);
      }
      phonePriceDto.setModel(title);
      String price = JSoupUtils.findElementByClass(element, "price-row").text();
      phonePriceDto.setPrice(price.replaceAll("[^0-9]", ""));
      String status = "";
      if (price.contains("停产")) {
        status = "停产";
      } else if (price.contains("上市")) {
        status = "即将上市";
      } else if (price.contains("概念")) {
        status = "概念产品";
      } else if (price.contains("价格")) {
        status = "价格面议";
      } else if (price.contains("暂无")) {
        status = "暂无报价";
      }
      phonePriceDto.setStatus(status);
//      if (StringUtils.isBlank(alias)) {
//        alias = crawlerModelAndAlias("http://detail.zol.com.cn" + url).get(ALIASKEY);
//      }
      phonePriceDto.setAlias(alias);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return phonePriceDto;
  }

  private Map<String, String> crawlerModelAndAlias(String url) {
    Map<String, String> map = new HashMap<>();
    try {
      String response = HttpUtils.get(url, null);
      Document document = Jsoup.parse(response);
      map.put(ALIASKEY, JSoupUtils.findTextByElement(JSoupUtils.findElementByClass(document, "product-model__alias")));
      map.put(MODELKEY, JSoupUtils.findTextByElement(JSoupUtils.findElementByClass(document, "product-model__name")));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }
}
