package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Paint;

class Point {
	private int x, y, r;
	private Paint paint;

	Point(int x, int y, int r, int c){
		this.x = x;
		this.y = y;
		this.r = r;
		paint = new Paint();
		paint.setColor(c);
	}


	protected int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	protected int getY() {
		return y;
	}

	protected void setY(int y) {
		this.y = y;
	}

	protected int getR() {
		return r;
	}

	protected void setR(int r) {
		this.r = r;
	}

	void draw(Canvas c){
		c.drawCircle(x, y, r, paint);
	}
}
