package org.wy.entity;

import android.graphics.Bitmap;

public class PicturePos 
{
	private int x = 0;
	private int y = 0;
	private Bitmap bitmap = null;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
}
