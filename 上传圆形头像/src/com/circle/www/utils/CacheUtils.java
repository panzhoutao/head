package com.circle.www.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class CacheUtils {
	
	/**
	 * 取得本应用的缓存图片位置
	 * @param imagePath 缓存图片子文件夹
	 * @return
	 */
	public static String getImagePath(Context context,String imagePath){
		
		File dir = null;
		
		if(isSDCardExist()){
			
			dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + context.getPackageName() + "/" + imagePath);
			
		}else {
			
			dir = new File(getDataPath(context) + "/" + imagePath);
			
		}
		
		return dir.getPath();
		
	}
	/**
	 * 判断内部存储路径
	 * @return
	 */
	public static boolean isSDCardExist(){
		
		String state = Environment.getExternalStorageState();
		
		if(state.equals(Environment.MEDIA_MOUNTED))
			
			return true;
			
		else 
			
			return false;
	}
	/**
	 * 取得内部存储路径
	 * @return
	 */
	public static String getDataPath(Context context){
		
		return "/data/data/" + context.getPackageName();
		
	}
	
}
