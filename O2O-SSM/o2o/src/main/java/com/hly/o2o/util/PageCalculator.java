package com.hly.o2o.util;

public class PageCalculator {
	/**
	 * 
	 * @param pageIndex:页码
	 * @param pageSize：一页的行数
	 * @return
	 */
	public static int calculatorRowIndex(int pageIndex,int pageSize){
		return (pageIndex>0)?(pageIndex-1)*pageSize:0;
	}

}
