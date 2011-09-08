package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Pad {
	int x, y, dx, dy, r, id;
	private Paint paint;
	boolean onField;

	Pad(int x, int y, int dx, int dy, int r, int c){
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.r = r;
		paint = new Paint();
		paint.setColor(c);
		onField = false;
		id = -1;
	}

	void draw(Canvas c){
		if(onField){
			c.drawCircle(x, y, r, paint);
		}
	}
}
