package jp.pokkyunsoft.AirHockey;

import jp.ac.bemax.AirHockey.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

class Field implements SurfaceHolder.Callback, Runnable, OnTouchListener{
	private SurfaceHolder holder;
	private Handler handler;
	
	private Thread looper;
	int width, height, redLine, blueLine, gleft, gright;
	private Point point1,point2,point3,point4;
	private Pack pack;
	private boolean loop;
	private Paint paint;
	private Rect goal1, goal2;
	private Path path;
	private Team red, blue;
	private Mode mode;

	public Field(AirHockey act, Mode mode){
		
		SurfaceView sview = (SurfaceView)act.findViewById(R.id.field);
		
		handler = act.getHandler();
		
		holder = sview.getHolder();
		
		holder.addCallback(this);

		paint = new Paint();

		sview.setOnTouchListener(this);

		path = new Path();
		
		this.mode = mode;
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

		red.draw(canvas);
		blue.draw(canvas);
		pack.draw(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,	 int height) {
		if(this.holder == holder){
			this.width = width;
			this.height = height;
			redLine = (int)(height * 0.4);
			blueLine = (int)(height * 0.6);
			int players = 1;
			if(mode.equals(Mode.Duale)){
				players = 2;
			}
			red = new Team(Player.RED , players, this);
			blue = new Team(Player.BLUE , players, this);
			if(mode.compareTo(Mode.Duale)==0){
				gleft = (int)(width*0.22);
				gright = (int)(width*0.78);
			}else{
				gleft = (int)(width*0.3);
				gright = (int)(width*0.7);
			}
			red.setPad(new Pad(this,100,100,Player.RED,Level.NORMAL));
			red.setPad(new Pad(this,100,100,Player.RED,Level.NORMAL));
			blue.setPad(new Pad(this,100,700,Player.BLUE,Level.NORMAL));
			blue.setPad(new Pad(this,100,700,Player.BLUE,Level.NORMAL));
			pack = new Pack((int)(width/20),height/2,3,3,(int)(width/20));
			goal1 = new Rect(gleft,0,gright,(int)(height*0.03));
			goal2 = new Rect(gleft,(int)(height*0.97),gright,height);
			point1 = new Point(gleft,0,0,Color.argb(0,0,0,0));
			point2 = new Point(gright,0,0,Color.argb(0,0,0,0));
			point3 = new Point(gleft,height-1,0,Color.argb(0,0,0,0));
			point4 = new Point(gright,height-1,0,Color.argb(0,0,0,0));
			loop = true;
			looper.start();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		looper = new Thread(this);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
		looper = null;
	}

	public void run() {
		long st,et;
		while(loop){
			st = System.currentTimeMillis();
			red.hit(pack);
			blue.hit(pack);
			pack.hit(point1);
			pack.hit(point2);
			pack.hit(point3);
			pack.hit(point4);
			pack.move(this);
			red.move();
			blue.move();
			paint();
			et = System.currentTimeMillis() - st;
			if(et < 30){
				try{
					Thread.sleep(30-et);
				}catch(Exception e){}
			}
		}
		
		Message msg = new Message();
		msg.what = 2;
		
		if(red.getScore() == 7){
			msg.arg1 = 0;
			handler.sendMessage(msg);
		}else{
			msg.arg1 = 1;
			handler.sendMessage(msg);
		}
	}

	public boolean onTouch(View view, MotionEvent e) {
		int index, id;
		float x,y;
		Pad p = null;
		switch(e.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			index = e.getActionIndex();
			id = e.getPointerId(index);
			x = e.getX(index);
			y = e.getY(index);
			if(e.getY(index) < redLine){
				if((p = red.attachId(id))!=null){
					p.setTouchPoint(x, y, true);
					p.setOnField(true);
				}
			}else if(e.getY(index) > blueLine){
				if((p = blue.attachId(id))!=null){;
					p.setTouchPoint(x, y, true);
					p.setOnField(true);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			for(index=0; index<e.getPointerCount(); index++){
				x = e.getX(index);
				y = e.getY(index);
				id = e.getPointerId(index);
				if((p = red.searchPad(id))!=null){
					p.setTouchPoint(x, y, false);
				}else if((p = blue.searchPad(id))!=null){
					p.setTouchPoint(x, y, false);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				index = e.getActionIndex();
				id = e.getPointerId(index);
				if((p=red.searchPad(id))!=null){
					p.removeId();
					p.setOnField(false);
				}
				if((p=blue.searchPad(id))!=null){
					p.removeId();
					p.setOnField(false);
				}
			}
		return true;
	}

	void goal(int c){
		Paint paint = new Paint();
		Path path = new Path();
		if(c==1){
			blue.addScore(1);
			if(blue.getScore() == 7) loop = false;
		}else{
			red.addScore(1);
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
