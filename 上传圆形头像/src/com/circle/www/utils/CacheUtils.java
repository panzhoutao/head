package com.circle.www.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class CacheUtils {
	
	/**
	 * ȡ�ñ�Ӧ�õĻ���ͼƬλ��
	 * @param imagePath ����ͼƬ���ļ���
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
	 * �ж��ڲ��洢·��
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
	 * ȡ���ڲ��洢·��
	 * @return
	 */
	public static String getDataPath(Context context){
		
		return "/data/data/" + context.getPackageName();
		
	}
	
}
