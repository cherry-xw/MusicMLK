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
	 * @return 歌曲ID
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return 歌曲的名称 
	 */
	public String getTilte() {
		return tilte;
	}
	public void setTilte(String tilte) {
		this.tilte = tilte;
	}
	/**
	 * @return 歌曲的专辑名
	 */
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	/**
	 * @return 歌曲的歌手名
	 */
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	/**
	 * @return 歌曲文件的路径
	 */
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return 歌曲的总播放时长 
	 */
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return 歌曲文件的大小
	 */
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @return 重命名歌曲名
	 */
	public String getMusicname() {
		return musicname;
	}
	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}
	/**
	 * @return 状态
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
