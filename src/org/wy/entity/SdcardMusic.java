package org.wy.entity;

public class SdcardMusic {
	private int id;
	private String tilte;
	private String album;
	private String artist;
	private String url;
	private int duration;
	private long size;
	private String musicname;
	private int status;
	
	/**
	 * @return ����ID
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return ���������� 
	 */
	public String getTilte() {
		return tilte;
	}
	public void setTilte(String tilte) {
		this.tilte = tilte;
	}
	/**
	 * @return ������ר����
	 */
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	/**
	 * @return �����ĸ�����
	 */
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	/**
	 * @return �����ļ���·��
	 */
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return �������ܲ���ʱ�� 
	 */
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return �����ļ��Ĵ�С
	 */
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @return ������������
	 */
	public String getMusicname() {
		return musicname;
	}
	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}
	/**
	 * @return ״̬
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
