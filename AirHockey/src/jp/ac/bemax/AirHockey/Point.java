package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Paint;

class Point {
	int x, y, r;
	private Paint paint;

	Point(int x, int y, int r, int c){
		this.x = x;
		this.y = y;
		this.r = r;
		paint = new Paint();
		paint.setColor(c);
	}

	void draw(Canvas c){
		c.drawCircle(x, y, r, paint);
	}
}
