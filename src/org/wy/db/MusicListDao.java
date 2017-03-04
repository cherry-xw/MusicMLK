package org.wy.db;

import java.util.Vector;

import org.wy.entity.SdcardMusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class MusicListDao implements BaseColumns {
	//���ݿ����
	private SQLiteDatabase mMusicListdb = null;
	//���ݿ���
	private final String LIST_DBNAME = "music_list";
	//����
	private final String TBL_MUSIC = "tbl_music";
	//����
	private final String TITLE = "tilte";
	private final String ALBUM = "album";
	private final String ARTIST = "artist";
	private final String URL = "url";
	private final String DURATION = "duration";
	private final String SIZE = "size";
	private final String MUSICNAME = "musicname";
	private final String STATUS = "status";//1��ʾ���� 0��ʾɾ��2��ʾϲ��
	
	/**
	 * �������ݿ�
	 * @param context ����
	 */
	public MusicListDao(Context context) {
		// TODO Auto-generated constructor stub
		mMusicListdb = context.openOrCreateDatabase(LIST_DBNAME, Context.MODE_PRIVATE, null);
	}
	
	/**������*/
	public void Create_MusicTable(){
		mMusicListdb.execSQL("create table if not exists "
				+ TBL_MUSIC + " (" + _ID + " integer primary key AUTOINCREMENT,"
				+ TITLE + " text," + ALBUM + " text,"  
				+ ARTIST  + " text, " + URL +" text,"
				+ DURATION  + " text, " + SIZE + " text,"
				+MUSICNAME+" text," + STATUS + " text)");
	}
	
	/**
	 * �����������
	 * @param sm SdcardMusic����
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
	 * ��ѯ����δ��ɾ����������Ϣ
	 * @return �����б�����
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
	 * ��ѯ����ϲ����������Ϣ
	 * @return �����б�����
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
	 * ɾ������
	 * @param musicId ����itemid
	 * @return ɾ���ɹ����
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
	 * ϲ������
	 * @param musicId ����itemid
	 * @return ɾ���ɹ����
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
	 * ��ϲ������
	 * @param musicId ����itemid
	 * @return ���óɹ����
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
	 * �޸ĸ�����
	 * @param musicId ����itemid
	 * @param musicName �޸ĵĸ�����
	 * @return �޸ĳɹ����
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
	 * �޸ĸ���
	 * @param musicId ����itemid
	 * @param musicName �޸ĵĸ���
	 * @return �޸ĳɹ����
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
	 * ���������������
	 */
	public void clearAllMusic(){
		mMusicListdb.execSQL("DELETE FROM "+ TBL_MUSIC);
	}
	
	
}
