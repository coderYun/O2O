package com.hly.o2o.service;

public interface CacheService {
	/**
	 * 依据Key前缀删除匹配该模式下的所有Key-value 如传入：shopcategory 则以shopcategory打头的如shopcategory_alllevle,
	 * shopcategory_firstlevel等的key-value都会被删除掉
	 * 
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);

}
