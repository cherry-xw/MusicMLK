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
	 * 获取sdcard所有音乐文件
	 * @return 音乐列表数据
	 */
	public static Vector<SdcardMusic> getMusicFromSdCard(Context context){
		Vector<SdcardMusic> als = new Vector<SdcardMusic>();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			//歌曲ID：MediaStore.Audio.Media._ID
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));  
			//歌曲的名称 ：MediaStore.Audio.Media.TITLE 
			String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  
			//歌曲的专辑名：MediaStore.Audio.Media.ALBUM 
			String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  
			//歌曲的歌手名： MediaStore.Audio.Media.ARTIST 
			String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));  
			//歌曲文件的路径 ：MediaStore.Audio.Media.DATA 
			String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));  
			//歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
			int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));  
			//歌曲文件的大小 ：MediaStore.Audio.Media.SIZE 
			long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
			//音乐文件播放时间大于0
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
	 * @param groupPath  如果你想获取SDcard下面的所以mp3文件你就填sdcard路径
	 * 用的是递归的方式获取
	 */
	List<String> mp3List = new ArrayList<String>();
	public void getSDcardFile(File groupPath){
		//循环获取sdcard目录下面的目录和文件
		for(int i=0; i< groupPath.listFiles().length; i++){
			File childFile = groupPath.listFiles()[i];
			
			//假如是目录的话就继续调用getSDcardFile（）将childFile作为参数传递的方法里面
			if(childFile.isDirectory()){
				getSDcardFile(childFile);
			}else{
				//如果是文件的话，判断是不是以.mp3结尾，是就加入到List里面
				if(childFile.toString().endsWith(".mp3")){
					mp3List.add(childFile.getName());
					
					//打印文件的文件名
					System.out.println(childFile.getName());
					//打印文件的路径
					System.out.println(childFile.getAbsolutePath());
				}
			}
		}
	}

// File SdcardFile = Environment.getExternalStorageDirectory();

}
