package com.hly.o2o.util;

public class PathUtil {
	private static String separator=System.getProperty("file.separator");
	
	//返回项目的根路径
	public static String getImageBasePath(){
		String os=System.getProperty("os.name");
		String basePath="";
		if(os.toLowerCase().startsWith("win")){
			basePath="F:/projectdev/image";
		}else{
			basePath="/home/hly/image";
		}
		basePath=basePath.replace("/",separator);
		return basePath;
		
	}
	//各自店铺图片存放的位置
	public static String getShopImagePath(long shopId){
		String imagePath="/upload/item/shop/"+shopId+"/";
		return imagePath.replace("/",separator);
		
	}

}
