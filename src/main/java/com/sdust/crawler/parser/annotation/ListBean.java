package com.sdust.crawler.parser.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListBean {

    boolean list() default false;

    String parent() default "";
}
