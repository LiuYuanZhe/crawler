package com.sdust.crawler.parser.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeccoClass {

	Class<?>[] value();
}
