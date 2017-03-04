package org.wy.db;

import java.util.Vector;

import org.wy.entity.SdcardMusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class MusicListDao implements BaseColumns {
	//数据库对象
	private SQLiteDatabase mMusicListdb = null;
	//数据库名
	private final String LIST_DBNAME = "music_list";
	//表名
	private final String TBL_MUSIC = "tbl_music";
	//列名
	private final String TITLE = "tilte";
	private final String ALBUM = "album";
	private final String ARTIST = "artist";
	private final String URL = "url";
	private final String DURATION = "duration";
	private final String SIZE = "size";
	private final String MUSICNAME = "musicname";
	private final String STATUS = "status";//1表示存在 0表示删除2表示喜欢
	
	/**
	 * 创建数据库
	 * @param context 环境
	 */
	public MusicListDao(Context context) {
		// TODO Auto-generated constructor stub
		mMusicListdb = context.openOrCreateDatabase(LIST_DBNAME, Context.MODE_PRIVATE, null);
	}
	
	/**创建表*/
	public void Create_MusicTable(){
		mMusicListdb.execSQL("create table if not exists "
				+ TBL_MUSIC + " (" + _ID + " integer primary key AUTOINCREMENT,"
				+ TITLE + " text," + ALBUM + " text,"  
				+ ARTIST  + " text, " + URL +" text,"
				+ DURATION  + " text, " + SIZE + " text,"
				+MUSICNAME+" text," + STATUS + " text)");
	}
	
	/**
	 * 添加音乐数据
	 * @param sm SdcardMusic对象
	 */
	public void initData(SdcardMusic sm){
		ContentValues cv = new ContentValues();
		cv.put(TITLE, sm.getTilte());
		cv.put(ALBUM, sm.getAlbum());
		cv.put(ARTIST, sm.getArtist());
		cv.put(URL, sm.getUrl());
		cv.put(DURATION, sm.getDuration());
		cv.put(SIZE, sm.getSize());
		cv.put(MUSICNAME, sm.getMusicname());
		cv.put(STATUS, "1");
		mMusicListdb.insert(TBL_MUSIC, "novalue", cv);
		mMusicListdb.close();
	}

	/**
	 * 查询所有未被删除的音乐信息
	 * @return 音乐列表向量
	 */
	public Vector<SdcardMusic> findAllMusic(){
		Vector<SdcardMusic> vsm = new Vector<SdcardMusic>();
		Cursor c = mMusicListdb.rawQuery("select * from "+TBL_MUSIC +" where "+ STATUS +" <> ?", new String[]{"0"});
		if(c.getCount() != 0){
			c.moveToFirst();
			while(!c.isAfterLast()){
				SdcardMusic sm = new SdcardMusic();
				sm.setId(c.getInt(0));
				sm.setTilte(c.getString(1));
				sm.setAlbum(c.getString(2));
				sm.setArtist(c.getString(3));
				sm.setUrl(c.getString(4));
				sm.setDuration(c.getInt(5));
				sm.setSize(c.getInt(6));
				sm.setMusicname(c.getString(6));
				sm.setStatus(c.getInt(8));
				vsm.addElement(sm);
				c.moveToNext();
			}
		}
		
		return vsm;
	}
	/**
	 * 查询所有喜欢的音乐信息
	 * @return 音乐列表向量
	 */
	public Vector<SdcardMusic> findAllLoveMusic(){
		Vector<SdcardMusic> vsm = new Vector<SdcardMusic>();
		Cursor c = mMusicListdb.rawQuery("select * from "+TBL_MUSIC +" where "+ STATUS +" = ?", new String[]{"2"});
		c.moveToFirst();
		while(!c.isAfterLast()){
			SdcardMusic sm = new SdcardMusic();
			sm.setId(c.getInt(0));
			sm.setTilte(c.getString(1));
			sm.setAlbum(c.getString(2));
			sm.setArtist(c.getString(3));
			sm.setUrl(c.getString(4));
			sm.setDuration(c.getInt(5));
			sm.setSize(c.getInt(6));
			sm.setMusicname(c.getString(6));
			sm.setStatus(c.getInt(8));
			vsm.addElement(sm);
			c.moveToNext();
		}
		return vsm;
	}
	/**
	 * 删除音乐
	 * @param musicId 音乐itemid
	 * @return 删除成功与否
	 */
	public boolean deleteMusic(int musicId){
		ContentValues cv = new ContentValues();
		cv.put(STATUS, "0");
		long l = mMusicListdb.update(TBL_MUSIC, cv, _ID +" = ?", new String[]{musicId+""});
		if(l == 0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 喜欢音乐
	 * @param musicId 音乐itemid
	 * @return 删除成功与否
	 */
	public boolean likeMusic(int musicId){
		ContentValues cv = new ContentValues();
		cv.put(STATUS, "2");
		long l = mMusicListdb.update(TBL_MUSIC, cv, _ID +" = ?", new String[]{musicId+""});
		if(l == 0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 不喜欢音乐
	 * @param musicId 音乐itemid
	 * @return 设置成功与否
	 */
	public boolean unlikeMusic(int musicId){
		ContentValues cv = new ContentValues();
		cv.put(STATUS, "1");
		long l = mMusicListdb.update(TBL_MUSIC, cv, _ID +" = ?", new String[]{musicId+""});
		if(l == 0){
			return false;
		}else{
			return true;
		}
	}
	

	/**
	 * 修改歌曲名
	 * @param musicId 音乐itemid
	 * @param musicName 修改的歌曲名
	 * @return 修改成功与否
	 */
	public boolean modifyMusicName(int musicId, String musicName){
		ContentValues cv = new ContentValues();
		cv.put(MUSICNAME, musicName);
		long l = mMusicListdb.update(TBL_MUSIC, cv, _ID +" = ?", new String[]{musicId+""});
		if(l == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 修改歌手
	 * @param musicId 音乐itemid
	 * @param musicName 修改的歌手
	 * @return 修改成功与否
	 */
	public boolean modifySinger(int musicId, String singer){
		ContentValues cv = new ContentValues();
		cv.put(ARTIST, singer);
		long l = mMusicListdb.update(TBL_MUSIC, cv, _ID +" = ?", new String[]{musicId+""});
		if(l == 0){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	/**
	 * 清除所有音乐数据
	 */
	public void clearAllMusic(){
		mMusicListdb.execSQL("DELETE FROM "+ TBL_MUSIC);
	}
	
	
}
