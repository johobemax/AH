package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class Pad {
	int x, y, dx, dy, r, id;
	private Paint paint;
	boolean onField;
	private Path path;
	int score, sx, sy, sd;

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
		path = new Path();
		score = 0;
	}

	void move(int mx, int my){
		dx = mx - x;
		dy = my - y;
		double len = Math.sqrt(dx*dx+dy*dy);
		if(len > r){
			this.dx = (int)(dx * r / len);
			this.dy = (int)(dy * r / len);
		}
		x += dx;
		y += dy;
	}

	void draw(Canvas c){
		if(onField){
			c.drawCircle(x, y, r, paint);
		}
	}

	void addScore(int p){
		score += p;
	}
}
