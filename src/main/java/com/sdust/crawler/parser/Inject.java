package com.sdust.crawler.parser;


import com.sdust.crawler.parser.bean.SpiderBean;
import com.sdust.crawler.parser.request.HttpRequest;
import com.sdust.crawler.parser.response.HttpResponse;

public interface Inject {
    public SpiderBean inject(Class<? extends SpiderBean> clazz, HttpRequest request, HttpResponse response);

}