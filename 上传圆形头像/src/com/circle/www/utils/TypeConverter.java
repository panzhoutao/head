package com.circle.www.utils;

import java.util.UUID;

public class TypeConverter {
	
	/**
	 * ��ȡΨһ��UUID
	 * @return
	 */
	public static String  getUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
		
	}
		
	
}
