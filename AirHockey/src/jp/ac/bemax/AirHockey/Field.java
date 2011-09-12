package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

class Field implements SurfaceHolder.Callback, Runnable, OnTouchListener{
	private SurfaceHolder holder;
	private Thread looper;
	int width, height, redLine, blueLine;
	private Pad pad1, pad2;
	private Point point1,point2,point3,point4;
	private Pack pack;
	private boolean loop;
	private Paint paint;
	private Rect goal1, goal2;
	private Path path;
	private Team red, blue;

	public Field(SurfaceView sview){
		holder = sview.getHolder();
		holder.addCallback(this);
		paint = new Paint();
		sview.setOnTouchListener(this);
		path = new Path();
	}

	public void paint(){
		Canvas canvas = holder.lockCanvas();
		paint.setColor(Color.GRAY);
		canvas.drawRect(0, 0, width, height, paint);
		paint.setColor(Color.argb(50, 255, 100, 100));
		canvas.drawRect(0,0,width,redLine,paint);
		paint.setColor(Color.argb(50,100,100,255));
		canvas.drawRect(0,blueLine,width,height,paint);
		paint.setColor(Color.rgb(100, 100, 100));
		canvas.drawRect(goal1, paint);
		canvas.drawRect(goal2, paint);
		drawStar(canvas,red.getScore(),true);
		drawStar(canvas,blue.getScore(),false);

		pad1.draw(canvas);
		pad2.draw(canvas);
		pack.draw(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,	 int height) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.holder == holder){
			this.width = width;
			this.height = height;
			redLine = (int)(height * 0.4);
			blueLine = (int)(height * 0.6);
			red = new Team(Player.RED,1,this);
			blue = new Team(Player.BLUE,1,this);
			pad1 = new Pad(this,100,100,Player.RED,Level.EASY);
			pad2 = new Pad(this,100,700,Player.BLUE,Level.HARD);
			pack = new Pack((int)(width/20),height/2,3,3,(int)(width/20));
			goal1 = new Rect((int)(width*0.3),0,(int)(width*0.7),(int)(height*0.03));
			goal2 = new Rect((int)(width*0.3),(int)(height*0.97),(int)(width*0.7),height);
			point1 = new Point((int)(width*0.3),0,0,Color.argb(0,0,0,0));
			point2 = new Point((int)(width*0.7),0,0,Color.argb(0,0,0,0));
			point3 = new Point((int)(width*0.3),height-1,0,Color.argb(0,0,0,0));
			point4 = new Point((int)(width*0.7),height-1,0,Color.argb(0,0,0,0));
			loop = true;
			looper.start();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		looper = new Thread(this);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		loop = false;
		looper = null;
	}

	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		long st,et;
		while(loop){
			st = System.currentTimeMillis();
			pack.hit(pad1);
			pack.hit(pad2);
			pack.hit(point1);
			pack.hit(point2);
			pack.hit(point3);
			pack.hit(point4);
			pack.move(this);
			pad1.move(this);
			pad2.move(this);
			paint();
			et = System.currentTimeMillis() - st;
//Log.d("FPS",""+et);
			if(et < 30){
				try{
					Thread.sleep(30-et);
				}catch(Exception e){}
			}
		}
		if(red.getScore() == 7){
			//paint();
		}else{
			//paint();
		}
	}

	public boolean onTouch(View view, MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		int index;
		float x,y;
		switch(e.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			index = e.getActionIndex();
			x = e.getX(index);
			y = e.getY(index);
			if(e.getY(index) < redLine){
				if( pad1.attachId(e.getPointerId(index) )){
					pad1.setTouchPoint(x, y, true);
					pad1.setOnField(true);
				}
			}else if(e.getY(index) > blueLine){
				if( pad2.attachId(e.getPointerId(index))){;
					pad2.setTouchPoint(x, y, true);
					pad2.setOnField(true);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			for(index=0; index<e.getPointerCount(); index++){
				x = e.getX(index);
				y = e.getY(index);
				int pid = e.getPointerId(index);
				if(pad1.isId(pid)){
					pad1.setTouchPoint(x, y, false);
				}else if(pad2.isId(pid)){
					pad2.setTouchPoint(x, y, false);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				index = e.getActionIndex();
				if(pad1.isId(e.getPointerId(index))){
					pad1.removeId();
					pad1.setOnField(false);
				}
				if(pad2.isId(e.getPointerId(index))){
					pad2.removeId();
					pad2.setOnField(false);
				}
			}
		return true;
	}

	void goal(int c){
		Paint paint = new Paint();
		Path path = new Path();
		if(c==1){
			pad2.addScore(1);
			if(blue.getScore() == 7) loop = false;
		}else{
			pad1.addScore(1);
			if(red.getScore() == 7) loop = false;
		}
	}

	void drawStar(Canvas c, int sc, boolean d){
		if(d){
			paint.setColor(Color.RED);
		}else{
			paint.setColor(Color.BLUE);
		}
		float theta = (float)(Math.PI * 72 / 180);
		float r = 30f;
		for(int i=0; i<sc; i++){
			path.reset();
			float fx = d ? 35 : width - 35;
			float fy = d ? 50*(i+1): height - 50*(i+1);
			PointF center = new PointF(fx, fy);
			float dy = r;
			float dx1 = (float)(r*Math.sin(theta));
			float dx2 = (float)(r*Math.sin(2*theta));

			float dy1 = (float)(r*Math.cos(theta));
			float dy2 = (float)(r*Math.cos(2*theta));
			if(d){
				dy = -dy;
				dy1 = -dy1;
				dy2 = -dy2;
			}

			path.moveTo(center.x, center.y-dy);
			path.lineTo(center.x-dx2, center.y-dy2);
			path.lineTo(center.x+dx1, center.y-dy1);
			path.lineTo(center.x-dx1, center.y-dy1);
			path.lineTo(center.x+dx2, center.y-dy2);
			path.lineTo(center.x, center.y-dy);
			c.drawPath(path, paint);
		}
	}
}
