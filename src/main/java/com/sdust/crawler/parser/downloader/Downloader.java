package com.sdust.crawler.parser.downloader;


import com.sdust.crawler.parser.request.HttpRequest;
import com.sdust.crawler.parser.response.HttpResponse;

/**
 * 下载器，负责将Scheduler里的请求下载下来，系统默认采用HttpClient作为下载引擎。
 * 
 * @author huchengyi
 *
 */
public interface Downloader {

	public HttpResponse download(HttpRequest request, boolean shutdown);
	
	public void shutdown();
}
