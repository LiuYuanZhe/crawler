package com.sdust.crawler.parser.downloader;

import com.sdust.crawler.parser.request.HttpPostRequest;
import com.sdust.crawler.parser.request.HttpRequest;
import com.sdust.crawler.parser.response.HttpResponse;
import com.sdust.crawler.parser.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 利用httpclient下载
 *  
 * @author huchengyi
 *
 */
public class HttpClientDownloader extends AbstractDownloader {
	
	private static Log log = LogFactory.getLog(HttpClientDownloader.class);
	
	private CloseableHttpClient httpClient;
	
	private HttpClientContext cookieContext;

	private static int retryCount = 3;
	
	public HttpClientDownloader() {
		
		cookieContext = HttpClientContext.create();
		cookieContext.setCookieStore(new BasicCookieStore());
//
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
//		try {
//			//构造一个信任所有ssl证书的httpclient
//			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
//				@Override
//				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					return true;
//				}
//			}).build();
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
//			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//			           .register("http", PlainConnectionSocketFactory.getSocketFactory())
//			           .register("https", sslsf)
//			           .build();
//		} catch(Exception ex) {
//			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//            .register("http", PlainConnectionSocketFactory.getSocketFactory())
//            .register("https", SSLConnectionSocketFactory.getSocketFactory())
//            .build();
//		}
		RequestConfig clientConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
//		PoolingHttpClientConnectionManager syncConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//		syncConnectionManager.setMaxTotal(1000);
//		syncConnectionManager.setDefaultMaxPerRoute(50);
		httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(clientConfig)
//				.setConnectionManager(syncConnectionManager)
				.setRetryHandler(new HttpRequestRetryHandler() {
					@Override
					public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
						boolean retry = (executionCount <= retryCount);
						if(log.isDebugEnabled() && retry) {
							log.debug("retry : " + executionCount);
						}
						return retry;
					}
				}).build();
	}

	/**
	 *
	 * @param request
	 * @param closable true:httpclient.close()
	 * @return
	 */
	@Override
	public HttpResponse download(HttpRequest request,boolean closable) {
		HttpResponse response = null;
		try {
			response = download(request,120000);
		}catch (Exception e){
			response = new HttpResponse();
			log.error("下载失败:request:url="+request.getUrl());
		}finally {
			if(closable)
				this.shutdown();
		}
		return response;
	}

	public HttpResponse download(HttpRequest request, int timeout) throws DownloadException {

		//add wangguanglong
		StringBuffer paramsBuffer = new StringBuffer();
		for(Map.Entry<String, String> entry : request.getParameters().entrySet()) {
			paramsBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		String urlParams = paramsBuffer.length()>0? paramsBuffer.substring(0,paramsBuffer.length()-1):paramsBuffer.toString();
		if (StringUtils.isNotEmpty(urlParams)){
			if (request.getUrl().contains("?")){
				request.setUrl(request.getUrl()+"&"+urlParams);
			}else {
				request.setUrl(request.getUrl()+"?"+urlParams);
			}

		}

		HttpRequestBase reqObj = null;
		if(request instanceof HttpPostRequest) {//post
			HttpPostRequest post = (HttpPostRequest)request;
			reqObj = new HttpPost(post.getUrl());
			//post fields
			List<NameValuePair> fields = new ArrayList<NameValuePair>();
			for(Map.Entry<String, Object> entry : post.getFields().entrySet()) {
				NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				fields.add(nvp);
			}
			try {
				HttpEntity entity = new UrlEncodedFormEntity(fields, "UTF-8");
				((HttpEntityEnclosingRequestBase) reqObj).setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {//get
			reqObj = new HttpGet(request.getUrl());
		}
		//header
//		boolean isMobile = SpiderThreadLocal.get().getEngine().isMobile();
//		reqObj.addHeader("User-Agent", UserAgent.getUserAgent(isMobile));
		for(Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
			reqObj.setHeader(entry.getKey(), entry.getValue());
		}
		//request config
		RequestConfig.Builder builder = RequestConfig.custom()
		.setConnectionRequestTimeout(1000)//从连接池获取连接的超时时间
		.setSocketTimeout(timeout)//获取内容的超时时间
		.setConnectTimeout(timeout)//建立socket连接的超时时间
		.setRedirectsEnabled(false);
		//proxy
//		HttpHost proxy = null;
//		Proxys proxys = ProxysContext.get();
//		boolean isProxy = ProxysContext.isEnableProxy();
//		if(proxys != null && isProxy) {
//			proxy = proxys.getProxy();
//			if(proxy != null) {
//				log.debug("proxy:" + proxy.getHostName()+":"+proxy.getPort());
//				builder.setProxy(proxy);
//				builder.setConnectTimeout(1000);//如果走代理，连接超时时间固定为1s
//			}
//		}
		reqObj.setConfig(builder.build());
		//request and response
		try {
			for(Map.Entry<String, String> entry : request.getCookies().entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());
				cookie.setPath("/");
				cookie.setDomain(reqObj.getURI().getHost());
				cookieContext.getCookieStore().addCookie(cookie);
			}
			org.apache.http.HttpResponse response = httpClient.execute(reqObj, cookieContext);
			int status = response.getStatusLine().getStatusCode();
			HttpResponse resp = new HttpResponse();
			resp.setStatus(status);
			if(status == 302 || status == 301) {
				String redirectUrl = response.getFirstHeader("Location").getValue();
				resp.setContent(UrlUtils.relative2Absolute(request.getUrl(), redirectUrl));
			} else if(status == 200) {
				HttpEntity responseEntity = response.getEntity();
				ByteArrayInputStream raw = toByteInputStream(responseEntity.getContent());
				resp.setRaw(raw);
				String contentType = null;
				Header contentTypeHeader = responseEntity.getContentType();
				if(contentTypeHeader != null) {
					contentType = contentTypeHeader.getValue();
				}
				resp.setContentType(contentType);
				if(!isImage(contentType)) { 
					String charset = getCharset(request.getCharset(), contentType);
					resp.setCharset(charset);
					//String content = EntityUtils.toString(responseEntity, charset);
					String content = getContent(raw, responseEntity.getContentLength(), charset);
					resp.setContent(content);
				}
			} else {
				//404，500等
//				if(proxy != null) {
//					proxys.failure(proxy.getHostName(), proxy.getPort());
//				}
				throw new DownloadServerException("" + status);
			}
//			if(proxy != null) {
//				proxys.success(proxy.getHostName(), proxy.getPort());
//			}
			return resp;
		} catch (IOException e) {
			//超时等
//			if(proxy != null) {
//				proxys.failure(proxy.getHostName(), proxy.getPort());
//			}
			throw new DownloadException(e);
		} finally {
			reqObj.releaseConnection();
		}
	}
	
	@Override
	public void shutdown() {
		try {
			httpClient.close();
		} catch (IOException e) {
			httpClient = null;
		}
	}
	
	public String getContent(InputStream instream, long contentLength, String charset) throws IOException {
		try {
			if (instream == null) {
	            return null;
	        }
	        int i = (int)contentLength;
	        if (i < 0) {
	            i = 4096;
	        }
	        Reader reader = new InputStreamReader(instream, charset);
	        CharArrayBuffer buffer = new CharArrayBuffer(i);
	        char[] tmp = new char[1024];
	        int l;
	        while((l = reader.read(tmp)) != -1) {
	            buffer.append(tmp, 0, l);
	        }
	        return buffer.toString();
		} finally {
			if (instream!=null)
				instream.reset();
		}
        
    }
	
	private boolean isImage(String contentType) {
		if(contentType == null) {
			return false;
		}
		if(contentType.toLowerCase().startsWith("image")) {
			return true;
		}
		return false;
	}

	public HttpClientContext getCookieContext() {
		return cookieContext;
	}
}
