package com.sdust.crawler.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuYuanZhe on 2018/5/3.
 */
@Component
public class ChaojiyingUtil {

    private static Logger logger = LoggerFactory.getLogger(ChaojiyingUtil.class);

    @Value("${chaojiying.username}")
    private String userName
            = "liuyuanzhe";
    @Value("${chaojiying.password}")
    private String password
            = "liuyuanzhe";
    @Value("${chaojiying.softid}")
    private String softId
            = "896544";
    @Value("${chaojiying.processingurl}")
    private String processingUrl
            = "http://upload.chaojiying.net/Upload/Processing.php";
    @Value("${chaojiying.reporterrorurl}")
    private String reporterrorUrl
            = "http://code.chaojiying.net/Upload/ReportError.php";
    @Value("${image.path}")
    private String imagePath
            = "/Users/rqw1991/Downloads/javaother/image";

    public static final String IMAG_PATH="/Users/rqw1991/Downloads/javaother/image";


    private static String innerUserName= "liuyuanzhe";;
    private static String innerPassword= "liuyuanzhe";;
    private static String innerSoftId= "896544";;
    private static String innerProcessingUrl = "http://upload.chaojiying.net/Upload/Processing.php";;
    private static String innerReporterrorUrl= "http://code.chaojiying.net/Upload/ReportError.php";;
    private static String innerImagePath= "/Users/rqw1991/Downloads/javaother/image";;

    @PostConstruct
    public void postConstruct() {
        innerUserName = userName;
        innerPassword = password;
        innerSoftId = softId;
        innerProcessingUrl = processingUrl;
        innerReporterrorUrl = reporterrorUrl;
        innerImagePath = imagePath+File.separator+"current";
    }

    /**
     * 报错返分
     * @param id		图片ID
     * @return			response
     */
    public static String ReportError(String id) {
        String param = String
                .format(
                        "user=%s&pass=%s&softid=%s&id=%s",
                        innerUserName, innerPassword, innerSoftId, id);
        String result = "";
        try {
            logger.info("---调用超级鹰报错返分接口，url : {}, param : {}",
                    innerReporterrorUrl, param);
            result = httpRequestData(
                    innerReporterrorUrl, param);
            logger.info("调用超级鹰成功，result={}", result);
        } catch (Exception e) {
            logger.info("调用超级鹰失败，result : {}", result);
            logger.error("", e);
            result = "未知问题";
        }
        return result;
    }

    /**
     * 识别图片_按图片文件路径
     * @param codetype	图片类型，必须字段
     * @param filePath	图片文件路径，必须字段
     * @return 错误返回-未知问题
     *          正确返回-{"err_no":0,"err_str":"OK","pic_id":"105426226809",
     *                    "pic_str":"qrtn","md5":"57f7002594c52fe8fb66d6aad9f3cda4",
     *                    "str_debug":"null"}
     */
    public static String PostPic(String codetype, String filePath) {
        return PostPic(codetype, null, null, null, filePath);
    }

    /**
     * 识别图片_按图片文件路径
     * @param codetype	图片类型，必须字段
     * @param len_min	最小位数
     * @param time_add	附加时间
     * @param str_debug	 开发者自定义信息
     * @param filePath	图片文件路径
     * @return 错误返回-未知问题
     *          正确返回-{"err_no":0,"err_str":"OK","pic_id":"105426226809",
     *                    "pic_str":"qrtn","md5":"57f7002594c52fe8fb66d6aad9f3cda4",
     *                    "str_debug":"null"}
     */
    public static String PostPic(String codetype, String len_min, String time_add, String str_debug,
                                 String filePath) {
        String result = "";
        String param = String
                .format(
                        "user=%s&pass=%s&softid=%s&codetype=%s&len_min=%s&time_add=%s&str_debug=%s",
                        innerUserName, innerPassword, innerSoftId, codetype, len_min, time_add, str_debug);
        File f = null;
        FileInputStream fis = null;
        try {
            f = new File(filePath);
            if (null != f) {
                int size = (int) f.length();
                byte[] data = new byte[size];
                fis = new FileInputStream(f);
                fis.read(data, 0, size);
                if(null != fis) fis.close();
                if (data.length > 0) {
                    try {
                        logger.info("---调用超级鹰图片识别接口，url : {}, imageCode : {}, imagePath : {}, param : {}",
                                innerProcessingUrl, codetype, filePath, param);
                        result = httpPostImage(innerProcessingUrl, param, data);
                        logger.info("调用超级鹰成功，result={}", result);
                    } catch(Exception e1) {
                        logger.info("调用超级鹰失败，result : {}", result);
                        logger.error("", e1);
                        result = "未知问题";
                    }
                    if (!"未知问题".equals(result)) {
                        Object obj = JSON.parse(result);
                        obj = JSONPath.eval(obj, "$.pic_str");
                        result = obj.toString();
                    }
                }
            }
        } catch(Exception ex) {
            logger.error("", ex);
            result = "未知问题";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {}
            }
        }
        return result;
    }

    /*
     * 核心上传函数
     * @param url 			请求URL
     * @param param			请求参数，如：username=test&password=1
     * @param data			图片二进制流
     * @return				response
     * @throws IOException
     */
    private static String httpPostImage(String url, String param,
                                       byte[] data) throws IOException {
        long time = (new Date()).getTime();
        URL u = null;
        HttpURLConnection con = null;
        String boundary = "----------" + MD5(String.valueOf(time));
        String boundarybytesString = "\r\n--" + boundary + "\r\n";
        OutputStream out = null;
        u = new URL(url);
        con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(60000);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(true);
        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        out = con.getOutputStream();
        for (String paramValue : param.split("[&]")) {
            out.write(boundarybytesString.getBytes("UTF-8"));
            String paramString = "Content-Disposition: form-data; name=\""
                    + paramValue.split("[=]")[0] + "\"\r\n\r\n" + paramValue.split("[=]")[1];
            out.write(paramString.getBytes("UTF-8"));
        }
        out.write(boundarybytesString.getBytes("UTF-8"));
        String paramString = "Content-Disposition: form-data; name=\"userfile\"; filename=\""
                + "chaojiying_java.gif" + "\"\r\nContent-Type: application/octet-stream\r\n\r\n";
        out.write(paramString.getBytes("UTF-8"));
        out.write(data);
        String tailer = "\r\n--" + boundary + "--\r\n";
        out.write(tailer.getBytes("UTF-8"));
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(con
                .getInputStream(), "UTF-8"));
        String temp;
        while ((temp = br.readLine()) != null) {
            buffer.append(temp);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /*
     * 字符串MD5加密
     * @param s 原始字符串
     * @return  加密后字符串
     */
    private final static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 通用POST方法
     * @param url 		请求URL
     * @param param 	请求参数，如：username=test&password=1
     */
    private static String httpRequestData(String url, String param) throws IOException {
        URL u;
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        StringBuffer buffer = new StringBuffer();
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(param);
            osw.flush();
            osw.close();
            br = new BufferedReader(new InputStreamReader(con
                    .getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (osw != null) {
                osw.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    /**
     * 将url指向的图片保存到本地，并返回本地地址
     *
     * @param imgUrl
     * @param result
     * @return 成功返回本地图片地址，失败返回-1
     */
    public static String putChaptaToLocal(String imgUrl,String result, String taskHashcode) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;

        String code = StringUtils.substringBefore(taskHashcode,"_");
        String localImagePath = innerImagePath + File.separator+code;
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+ taskHashcode + ".jpg";
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("Cookie", result);
//			System.out.println("cookie***"+result);
            httpUrl.setRequestProperty("Accept", "image/png, image/svg+xml, image/jxr, image/*; q=0.8, */*; q=0.5");
            httpUrl.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpUrl.setRequestProperty("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
            httpUrl.setRequestProperty("Connection", "Keep-Alive");
            httpUrl.setRequestProperty("Host", "authcode.jd.com");
            httpUrl.setRequestProperty("Referer", "https://passport.jd.com/new/login.aspx");
//			httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(imageUrl);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            return imageUrl;
        } catch (Exception e) {
//			e.printStackTrace();
            return "-1";
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 将url指向的图片保存到本地，并返回本地地址（header需指定）
     * @param imgUrl 图片地址
     * @param result cookies
     * @param headers 消息头信息
     * @param taskHashcode 用户手机号
     * @return 成功返回本地图片地址，失败返回-1
     */
    public static String putChaptaToLocal(String imgUrl, String result, Map<String, String> headers, String taskHashcode) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        String code = StringUtils.substringBefore(taskHashcode,"_");
        String localImagePath = innerImagePath + File.separator+code;
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+ taskHashcode + ".jpg";
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("Cookie", result);
            //add headers
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpUrl.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(imageUrl);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            return imageUrl;
        } catch (Exception e) {
            return "-1";
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 将url指向的图片保存到本地，并返回本地地址（header需指定）
     * @param imgUrl 图片地址
     * @param result cookies
     * @param headers 消息头信息
     * @return map
     *          key:imgPath - 图片路径
     *          key:cookies - Set-Cookie
     */
    public static Map<String, String> putChaptaToLocalReturnCookie(String imgUrl,
                                                                   String result,
                                                                   Map<String, String> headers
    ,String citycode) {
        Map<String, String> map = new HashMap() {{
            put("imgPath", "-1");
            put("cookies", "");
        }};
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        String localImagePath = innerImagePath + File.separator;
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+"123"+".jpg";
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("Cookie", result);
            //add headers
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpUrl.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpUrl.connect();
            if (httpUrl.getHeaderFields() != null && httpUrl.getHeaderFields().get("Set-Cookie") != null) {
                map.put("cookies", httpUrl.getHeaderFields().get("Set-Cookie").toString());
            }
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(imageUrl);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            map.put("imgPath", imageUrl);
            return map;
        } catch (Exception e) {
            logger.info("{}:验证码图片下载失败");
            logger.info("", e);
            return map;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 将url指向的图片保存到本地，并返回本地地址
     *
     * @param imgUrl
     * @param result
     * @return 成功返回本地图片地址，失败返回-1
     */
    public static String putBase64ChaptaToLocal(String imgUrl,String result, String taskHashcode) {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        String code = StringUtils.substringBefore(taskHashcode,"_");
        String localImagePath = innerImagePath + File.separator+code;
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+ taskHashcode + ".jpg";
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.setRequestProperty("Cookie", result);
//			System.out.println("cookie***"+result);
            httpUrl.setRequestProperty("Accept", "*/*");
            httpUrl.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            httpUrl.setRequestProperty("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
            httpUrl.setRequestProperty("Connection", "Keep-Alive");
            httpUrl.setRequestProperty("Host", "authcode.jd.com");
            httpUrl.setRequestProperty("Referer", "http://service.ah.10086.cn/pub-page/common/loginBox.html");
			httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            StringBuffer buffer = new StringBuffer();
            while ((bis.read(buf)) != -1) {
                buffer.append(buf);
            }
            replaceBase64Image(buffer.toString(),imageUrl);
            return imageUrl;
        } catch (Exception e) {
//			e.printStackTrace();
            return "-1";
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     * @param src
     * @param imagePath
     * @return
     */
    public static void replaceBase64Image(String src, String imagePath) {
        File resourceFilePath = new File(IMAG_PATH);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String ext = StringUtils.substringBetween(src, "data:image/", ";");
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        String base64ImgData = StringUtils.substringAfter(src, "base64,");//图片数据
        //data:image/gif;base64,base64编码的gif图片数据
        //data:image/png;base64,base64编码的png图片数据
        if ("jpeg".equalsIgnoreCase(ext)) {//data:image/jpeg;base64,base64编码的jpeg图片数据
            ext = "jpg";
        } else if ("x-icon".equalsIgnoreCase(ext)) {//data:image/x-icon;base64,base64编码的icon图片数据
            ext = "ico";
        }
        FileOutputStream os = null;
        try {
            BASE64Decoder d = new BASE64Decoder();
            byte[] bs = d.decodeBuffer(base64ImgData);
            os = new FileOutputStream(imagePath);
            os.write(bs);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null){
                try {os.close();} catch (IOException e) {}
            }
        }
    }


    public static String generateImageToLocal(String imgStr,String taskHashcode)
    {   //对字节数组字符串进行Base64解码并生成图片
        String code = StringUtils.substringBefore(taskHashcode,"_");
        String localImagePath = innerImagePath + File.separator+code;
        //路径不存在，新建路径
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+ taskHashcode + ".jpg";
        if (imgStr == null) //图像数据为空
            return imageUrl;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            String base64ImgData = StringUtils.substringAfter(imgStr, "base64,");
            byte[] b = decoder.decodeBuffer(base64ImgData);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(imageUrl);
            out.write(b);
            out.flush();
            out.close();
            return imageUrl;
        }
        catch (Exception e)
        {
            return imageUrl;
        }
    }

    /**
     * 根据对页面的验证码进行解析，返回验证码路径(截图方式)
     * @param driver
     * @param ele 验证码元素
     * @param taskHashcode
     * @return
     */
    public static String saveChaptaToLocal(WebDriver driver, WebElement ele, String taskHashcode) {
        String code = StringUtils.substringBefore(taskHashcode,"_");
        String localImagePath = innerImagePath + File.separator+code;
        File resourceFilePath = new File(localImagePath);
        if (!resourceFilePath.exists()) {
            resourceFilePath.mkdirs();
        }
        String imageUrl = localImagePath+File.separator+ taskHashcode + ".png";
        try {
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);
            Point point = ele.getLocation();
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();
            BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
                    eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "png", screenshot);
            File screenshotLocation = new File(imageUrl);
            FileUtils.copyFile(screenshot, screenshotLocation);
            return imageUrl;
        } catch (Exception e) {
            return "-1";
        } finally {
        }
    }
}
