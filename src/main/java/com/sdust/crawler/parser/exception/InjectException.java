package com.sdust.crawler.parser.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 注入bean的多个filed异常
 *
 * @author huchengyi
 */
public class InjectException extends Exception {

    private static Log log = LogFactory.getLog(InjectException.class);

    private static final long serialVersionUID = 5034687491589622988L;

    private Class clazz;

    public InjectException(String message, Class clazz) {
        super(message);
        this.clazz = clazz;
    }

    public InjectException(String message, Class clazz, FieldInjectException cause) {
        super(message, cause);
        this.clazz = clazz;
    }

    public Class getBeanClass() {
        return clazz;
    }

    public void setBeanClass(Class clazz) {
        this.clazz = clazz;
    }

    public static void log(String message, Class clazz, Throwable cause) {
        log.error(clazz.getName() + " render error : " + message);
        log.error(message, cause);
    }

    public static void log(String message, Class clazz) {
        log(message, clazz, null);
    }
}