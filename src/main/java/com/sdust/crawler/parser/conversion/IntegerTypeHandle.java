package com.sdust.crawler.parser.conversion;

public class IntegerTypeHandle implements TypeHandle<Integer> {
	
	@Override
	public Integer getValue(Object src) throws Exception {
		return Integer.valueOf(src.toString());
	}

	public static void main(String[] args) throws Exception {
		IntegerTypeHandle handle = new IntegerTypeHandle();
		System.out.println(handle.getValue("18"));
	}

}
