package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class Pad {
	int x, y, dx, dy, r, id, mx, my;
	private Paint paint;
	private boolean onField;
	private Path path;
	int score, sx, sy, sd;
	Player p;

	public synchronized void setOnField(boolean b){
		onField = b;
	}

	public boolean getOnField(){
		return onField;
	}

	public synchronized void setMx(int mx){
		this.mx = mx;
	}

	public synchronized void setMy(int my){
		this.my = my;
	}

	Pad(int x, int y, int r, Player p){
		this.x = this.mx = x;
		this.y = this.my = y;
		this.dx = 0;
		this.dy = 0;
		this.r = r;
		this.p = p;
		paint = new Paint();
		if(p == Player.RED){
			paint.setColor(Color.RED);
		}else{
			paint.setColor(Color.BLUE);
		}
		onField = false;
		id = -1;
		path = new Path();
		score = 0;
	}

	synchronized void move(Field f){
		dx = mx - x;
		if(p == Player.RED){
			dy = (my + r) - y;
		}else if(p==Player.BLUE){
			dy = (my - r) - y;
		}
		double len = Math.sqrt(dx*dx+dy*dy);
		if(len > r){
			this.dx = (int)(dx * r / len);
			this.dy = (int)(dy * r / len);
		}
		x += dx;
		y += dy;

		if(p == Player.RED){
			if(y <= f.redLine && !onField){
				onField = true;
			}else if(y > f.redLine && onField){
				onField = false;
			}
		}
		if(p==Player.BLUE){
			if(y >= f.blueLine && !onField){
				onField = true;
			}else if(y < f.blueLine && onField){
				onField = false;
			}
		}
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
