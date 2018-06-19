package com.sdust.crawler.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiuYuanZhe on 18/5/22.
 */
public class CrawlerAspect {
    private Logger logger = LoggerFactory.getLogger(CrawlerAspect.class);
//    @Around()
    public void writeLog(ProceedingJoinPoint pjp){
        try {
            logger.info("进入切面 对象名"+pjp.getSignature().getDeclaringType().getSimpleName()
                    + "   #方法名:" + pjp.getSignature().getName());
            Object[] objs = pjp.getArgs();
//            logger.getMethodName().set(joinPoint.getSignature().getDeclaringType().getSimpleName()
//                    + "#" + joinPoint.getSignature().getName());
        } catch (Exception ex) {
//            获取方法名称失败
//            logger.getMethodName().set("");

        }
    }
}
