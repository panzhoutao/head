package com.circle.www.utils;

import java.util.UUID;

public class TypeConverter {
	
	/**
	 * 获取唯一的UUID
	 * @return
	 */
	public static String  getUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
		
	}
		
	
}
