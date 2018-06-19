package com.sdust.crawler.parser.annotation;

import java.lang.annotation.*;

/**
 * fastjson,jsonpath语法
 * 
 * @author huchengyi
 *
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
	
	/**
	 * jsonpath
	 * 
	 * @return jsonpath
	 */
	String value();
	
}
