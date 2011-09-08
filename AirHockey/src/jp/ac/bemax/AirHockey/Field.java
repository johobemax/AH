package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

class Field implements SurfaceHolder.Callback, Runnable, OnTouchListener{
	private SurfaceHolder holder;
	private Thread looper;
	int width, height, aLine, bLine;
	private Pad pad1, pad2;
	private Point point1,point2,point3,point4;
	private Pack pack;
	private boolean loop;
	private Paint paint;
	private Rect goal1, goal2;

	public Field(SurfaceView sview){
		holder = sview.getHolder();
		holder.addCallback(this);
		paint = new Paint();
		sview.setOnTouchListener(this);
	}

	public void paint(){
		Canvas canvas = holder.lockCanvas();
		paint.setColor(Color.GRAY);
		canvas.drawRect(0, 0, width, height, paint);
		paint.setColor(Color.argb(50, 255, 100, 100));
		canvas.drawRect(0,0,width,aLine,paint);
		canvas.drawRect(goal2, paint);
		paint.setColor(Color.argb(50,100,100,255));
		canvas.drawRect(0,bLine,width,height,paint);
		canvas.drawRect(goal1, paint);
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
			aLine = (int)(height * 0.4);
			bLine = (int)(height * 0.6);
			pad1 = new Pad(100,100,0,0,width/10,Color.RED);
			pad2 = new Pad(100,700,0,0,width/10,Color.BLUE);
			pack = new Pack((int)(width/20),height/2,3,3,(int)(width/20));
			goal1 = new Rect((int)(width*0.3),0,(int)(width*0.7),(int)(height*0.05));
			goal2 = new Rect((int)(width*0.3),(int)(height*0.95),(int)(width*0.7),height);
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
		while(loop){
			pack.move(this);
			pack.hit(pad1);
			pack.hit(pad2);
			pack.hit(point1);
			pack.hit(point2);
			pack.hit(point3);
			pack.hit(point4);
			paint();
		}
	}

	public boolean onTouch(View view, MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
			int index = e.getActionIndex();
			switch(e.getActionMasked()){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				if(e.getY(index) < aLine && pad1.id < 0){
					pad1.id = e.getPointerId(e.getActionIndex());
					pad1.onField = true;
					pad1.x = (int)e.getX(index);
					pad1.y = (int)e.getY(index) + pad1.r;
				}
				if(e.getY(index) > bLine && pad2.id < 0){
					pad2.id = e.getPointerId(e.getActionIndex());
					pad2.onField = true;
					pad2.x = (int)e.getX(index);
					pad2.y = (int)e.getY(index) - pad2.r;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				for(int i=0; i<e.getPointerCount(); i++){
					int pid = e.getPointerId(i);
					if(pid == pad1.id){
						pad1.dx = (int)e.getX(i) - pad1.x;
						pad1.dy = (int)e.getY(i) - pad1.y + pad1.r;
						pad1.x += pad1.dx;
						pad1.y += pad1.dy;
						if(e.getY(i) < aLine - pad1.r){
							pad1.onField = true;
						}else{
							pad1.onField = false;
						}
					}
					if(pid == pad2.id){
						pad2.dx = (int)e.getX(i) - pad2.x;
						pad2.dy = (int)e.getY(i) - pad2.y - pad2.r;
						pad2.x += pad2.dx;
						pad2.y += pad2.dy;
						if(e.getY(i) > bLine + pad2.r){
							pad2.onField = true;
						}else{
							pad2.onField = false;
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if(pad1.id == e.getPointerId(index)){
					pad1.id = -1;
					pad1.onField = false;
				}
				if(pad2.id == e.getPointerId(index)){
					pad2.id = -1;
					pad2.onField = false;
				}
			}
		return true;
	}

}
