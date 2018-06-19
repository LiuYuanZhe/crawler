package com.sdust.crawler.parser.conversion;

public interface TypeHandle<T> {
	
	public T getValue(Object src) throws Exception;

}
