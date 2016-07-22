package com.circle.www.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;


public class FileUtils {
	/**
	 * �����ļ�
	 */
	public static boolean mkdirs(String file){
		
		String path = file.substring(0, file.lastIndexOf("/") + 1);
		
		File fi = new File(path);
		
		if(!fi.exists()){
			
			return fi.mkdir();
			
		}else{
			
			return true;
		}
		
	}
	
	/*
	 * ��byte����д���ļ�
	 */
	public static void writeFileByByteArray(byte [] array,String path) throws IOException{
	
		File file = new File(path);
		
		File p = new File(path.substring(0, path.lastIndexOf("/")+1));
		
		if(!p.exists()){
			
			System.out.println(p.mkdirs());
			
		}
		
		
		FileOutputStream fos = new FileOutputStream(file);
		
		fos.write(array,0,array.length);
		
		fos.close();
		
	}
	/**
	 * ��ȡ������ȡ��ͼƬ��·��
	 * @return
	 */
	public static String getPictureSelectedPath(Intent intent,Activity activity){
		Uri uri = intent.getData();
		Cursor cursor = activity.managedQuery(uri, new String [] {MediaStore.Images.Media.DATA}, null, null, null);
		if(cursor == null){
			return "";
		}
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToNext();
		String path = cursor.getString(index);
		
		return path;
	}
	
	/**
	 * ����������Bitmap����ѹ������д������Ŀ¼���ٽ�ѹ����ͼƬ�������ֽ�����
	 * @param bitmap ������ͼƬ
	 * @throws IOException
	 */
	public static byte[] compressAndWriteFile(Bitmap bitmap,Context context,String path) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
		FileUtils.writeFileByByteArray(baos.toByteArray(),path);
		return baos.toByteArray();
	}
	
}