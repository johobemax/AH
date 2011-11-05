package jp.pokkyunsoft.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Pack {
	int x, y, dx, dy, r;
	private Paint paint;
	private boolean hiting;

	Pack(int x, int y, int dx, int dy, int r){
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.r = r;
		paint = new Paint();
		paint.setColor(Color.WHITE);
		hiting = false;
	}

	void move(Field f){
		x += dx;
		y += dy;

		if(x < r){
			x = r;
			dx = -(int)(dx*0.8);
		}
		if(x > f.width - r){
			x = f.width - r;
			dx = -(int)(dx*0.8);
		}
		if(y < r && (x < f.gleft || x > f.gright)){
			y = r;
			dy = -(int)(dy*0.8);
		}
		if(y > f.height - r && (x < f.gleft || x > f.gright)){
			y = f.height - r;
			dy = -(int)(dy*0.8);
		}

		if(y < -r || y > f.height + r){
			if(y < -r){
				x = f.width - r;
				dx = -3;
				dy = -3;
				f.goal(1);
			}else{
				x = r;
				dx = 3;
				dy = 3;
				f.goal(2);
			}
			y = f.height/2;
		}
	}

	void draw(Canvas c){
		c.drawCircle(x, y, r, paint);
	}

	void hit(Pad p){
		if(!p.drawable()) return;
		int xx = p.getX() - x;
		int yy = p.getY() - y;
		double len = Math.sqrt(xx*xx+yy*yy);

		if(len <= p.getR() + r){
			double deg = Math.acos(xx/len);
			if(Math.asin(yy/len)<0){
				deg = -deg;
			}

			double ddx = dx*Math.cos(-deg) - dy*Math.sin(-deg);
			if(ddx>=0){
				ddx = -ddx * 0.8;
			}
			double ddy = dx*Math.sin(-deg) + dy*Math.cos(-deg);
			ddx += p.getDx()*Math.cos(-deg) - p.getDy()*Math.sin(-deg);

			dx = (int)(ddx*Math.cos(deg)-ddy*Math.sin(deg));
			dy = (int)(ddx*Math.sin(deg)+ddy*Math.cos(deg));

			len = Math.sqrt(dx*dx+dy*dy);
			if(len > r*2){
				dx = (int)(dx * r*2 / len);
				dy = (int)(dy * r*2 / len);
			}
		}
	}

	void hit(Point p){
		int xx = p.getX() - x;
		int yy = p.getY() - y;
		double len = Math.sqrt(xx*xx+yy*yy);

		if(hiting){
			if(len <= p.getR() + r){

			}else{
				hiting = false;
			}
		}else if(len <= p.getR() + r){
			if(hiting){

			}
			double deg = Math.acos(xx/len);
			if(Math.asin(yy/len)<0){
				deg = -deg;
			}

			double ddx = dx*Math.cos(-deg) - dy*Math.sin(-deg);
			if(ddx>=0){
				ddx = -ddx * 0.8;
			}

			double ddy = dx*Math.sin(-deg) + dy*Math.cos(-deg);

			dx = (int)(ddx*Math.cos(deg)-ddy*Math.sin(deg));
			dy = (int)(ddx*Math.sin(deg)+ddy*Math.cos(deg));
		}
	}
}
