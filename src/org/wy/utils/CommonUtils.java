package org.wy.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.wy.entity.SdcardMusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class CommonUtils {
	
	
	/**
	 * ��ȡsdcard���������ļ�
	 * @return �����б�����
	 */
	public static Vector<SdcardMusic> getMusicFromSdCard(Context context){
		Vector<SdcardMusic> als = new Vector<SdcardMusic>();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			//����ID��MediaStore.Audio.Media._ID
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));  
			//���������� ��MediaStore.Audio.Media.TITLE 
			String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  
			//������ר������MediaStore.Audio.Media.ALBUM 
			String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  
			//�����ĸ������� MediaStore.Audio.Media.ARTIST 
			String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));  
			//�����ļ���·�� ��MediaStore.Audio.Media.DATA 
			String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));  
			//�������ܲ���ʱ�� ��MediaStore.Audio.Media.DURATION
			int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));  
			//�����ļ��Ĵ�С ��MediaStore.Audio.Media.SIZE 
			long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
			//�����ļ�����ʱ�����0
			if(duration > 0){
				SdcardMusic sm = new SdcardMusic();
				sm.setId(id);
				sm.setTilte(tilte);
				sm.setAlbum(album);
				sm.setArtist(artist);
				sm.setUrl(url);
				sm.setDuration(duration);
				sm.setSize(size);
				als.addElement(sm);
			}
			cursor.moveToNext();
		}
		return als;
	}
	
	/**
	 * 
	 * @param groupPath  ��������ȡSDcard���������mp3�ļ������sdcard·��
	 * �õ��ǵݹ�ķ�ʽ��ȡ
	 */
	List<String> mp3List = new ArrayList<String>();
	public void getSDcardFile(File groupPath){
		//ѭ����ȡsdcardĿ¼�����Ŀ¼���ļ�
		for(int i=0; i< groupPath.listFiles().length; i++){
			File childFile = groupPath.listFiles()[i];
			
			//������Ŀ¼�Ļ��ͼ�������getSDcardFile������childFile��Ϊ�������ݵķ�������
			if(childFile.isDirectory()){
				getSDcardFile(childFile);
			}else{
				//������ļ��Ļ����ж��ǲ�����.mp3��β���Ǿͼ��뵽List����
				if(childFile.toString().endsWith(".mp3")){
					mp3List.add(childFile.getName());
					
					//��ӡ�ļ����ļ���
					System.out.println(childFile.getName());
					//��ӡ�ļ���·��
					System.out.println(childFile.getAbsolutePath());
				}
			}
		}
	}

// File SdcardFile = Environment.getExternalStorageDirectory();

}
