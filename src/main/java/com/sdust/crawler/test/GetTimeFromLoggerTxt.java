package com.sdust.crawler.test;

import com.sdust.crawler.dto.CitycodeDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiuYuanZhe on 18/5/15.
 */
@Component
public class GetTimeFromLoggerTxt {

//    public static void main(String[] args) {
//        //匹配次数
//        int matchTime = 0;
//        //存匹配上的字符串
////        List<String> strs = new ArrayList<>();
//        List<CitycodeDto> cityList = new ArrayList();
//        try
//        {
//            //编码格式
//            String encoding = "UTF-8";
//            //文件路径
////                File file = new File("F:\\svn\\work\\Redis.txt");
//            File file = new File("/Users/rqw1991/Downloads/city.txt");
//            if (file.isFile() && file.exists()){ // 判断文件是否存在
//                //输入流
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file), encoding);// 考虑到编码格
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//                //读取一行
//                while ((lineTxt = bufferedReader.readLine()) != null)
//                {
//                    //正则表达式
//                    new GetTimeFromLoggerTxt().getMatch(matchTime, cityList, lineTxt);
//
//                }
//                System.out.println("size:"+cityList.size());
////                System.out.println(cityMap);
//                read.close();
//            }
//            else
//            {
//                System.out.println("找不到指定的文件");
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//    }

    /**
     * /Users/rqw1991/Downloads
     * 获取cityMap
     * @throws FileNotFoundException
     */
    public List<CitycodeDto> getCityList(){
        //匹配次数
        int matchTime = 0;
        //存匹配上的字符串
//        List<String> strs = new ArrayList<>();
        List<CitycodeDto> cityList = new ArrayList();
        try
        {
            //编码格式
            String encoding = "UTF-8";
            //文件路径
//                File file = new File("F:\\svn\\work\\Redis.txt");
            File file = new File("/Users/rqw1991/Downloads/city.txt");
            if (file.isFile() && file.exists()){ // 判断文件是否存在
                //输入流
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                //读取一行
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    //正则表达式
                    getMatch(matchTime, cityList, lineTxt);

                }
                System.out.println("size:"+cityList.size());
//                System.out.println(cityMap);
                read.close();
            }
            else
            {
                System.out.println("找不到指定的文件");
            }
            return cityList;
        }
        catch (Exception e)
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return null;
        }
    }

    private void getMatch(int matchTime, List list, String lineTxt) {
//        Pattern p = Pattern.compile("data-val=\"[0-9]*\"");
//        Matcher m = p.matcher(lineTxt);
//        boolean result = m.find();
//        String find_result = null;
//        if (result)
//        {
//            matchTime++;
//            find_result = m.group(0);
//            strs.add(find_result);
//        }
        Pattern p = Pattern.compile("data-val=\"[0-9]*\"");
        Matcher m = p.matcher(lineTxt);
        String regtx =  "[一-龥][\\u4E00-\\u9FA5]";
        Pattern p1 = Pattern.compile(regtx);
        Matcher m1 = p1.matcher(lineTxt);
        boolean res = m.find();
        boolean res1 = m1.find();
        if (res&&res1){
            String code = m.group(0);
            System.out.println(StringUtils.substring(code,10,-1));
            String city = m1.group();
            System.out.println(city);
            CitycodeDto citycodeDto = new CitycodeDto();
            citycodeDto.setCityname(city);
            citycodeDto.setCitycode(StringUtils.substring(code,10,-1));
            list.add(citycodeDto);
        }
    }

    private static List<Integer> getSum(List<String> strs) {
        List<Integer> nums = new ArrayList<>();
        for(String str : strs){
            String s = str.replace("ms","");
            Integer a = Integer.valueOf(s);
            nums.add(a);
        }
        return nums;
    }

    private static double getAvgTime(List<Integer> nums, int matchTime) {
        double sum = 0;
        double avg ;
        for(Integer num : nums){
            sum+=num;
        }
        avg = sum/matchTime;
        return avg;
    }
}
