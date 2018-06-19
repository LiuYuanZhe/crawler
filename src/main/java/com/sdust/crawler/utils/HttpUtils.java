package com.sdust.crawler.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by xuejingwen on 2018/4/8.
 */
public class HttpUtils {

  public static String get(String url, Map<String, String> headers) {
    BufferedReader in = null;
    try {
      URL realUrl = new URL(url);
      // 打开和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 设置通用的请求属性
      if (headers != null && !headers.isEmpty()) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
          connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
      } else {
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      }
      connection.setConnectTimeout(20000);
      connection.setReadTimeout(20000);
      // 建立实际的连接
      connection.connect();
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      return sb.toString();
    } catch (Exception e) {
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
