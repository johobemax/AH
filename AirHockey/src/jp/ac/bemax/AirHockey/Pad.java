package jp.ac.bemax.AirHockey;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class Pad {
	private int x, y, dx, dy, r, id, mx, my, yGap;
	private Paint paint;
	private boolean onField, inField;
	private Path path;
	private int score, sx, sy, sd;
	Player player;

	/******* Setter *******/
	public synchronized void setOnField(boolean b){	onField = b;}

	/******* Getter *******/
	public int getX() {return x;}
	public int getY() {return y;}
	public int getR(){return r;}
	public int getDx(){return dx;}
	public int getDy(){return dy;}

	/** コンストラクタ */
	Pad(Field f, int x, int y, Player p, Level l){
		this.x = this.mx = x;
		this.y = this.my = y;
		this.dx = 0;
		this.dy = 0;
		this.r = f.width / 12;
		this.yGap = f.height / 100;
		this.player = p;
		paint = new Paint();
		if(p == Player.RED){
			paint.setColor(Color.RED);
		}else{
			paint.setColor(Color.BLUE);
			yGap = -yGap;
		}
		if(l  == Level.HARD){
			this.r = (int)(r * 0.8);
		}else if(l==Level.EASY){
			this.r = (int)(r*1.25);
		}
		onField = false;
		inField = false;
		id = -1;
		path = new Path();
		score = 0;
	}

	/**
	 * Padが描ける状態かどうか。
	 */
	public boolean drawable(){
		return onField & inField;
	}

	/**
	 * ポインタIDの実装を取り外す
	 */
	public void removeId(){
		id = -1;
	}

	/**
	 * ポインタIDを実装していない場合に、ポインタIDを取得する。
	 * @param id ポインタID
	 * @return ポインタIDが実装済みならばfalse
	 */
	public boolean attachId(int id){
		boolean result = false;
		if(this.id==-1){
			this.id = id;
			result = true;
		}
		return result;
	}

	/**
	 * PadのIDがidと等しいかどうか
	 * @param id 比較するポインタID
	 * @return true or false
	 */
	public boolean isId(int id){
		return this.id == id ? true: false;
	}

	/**
	 * タッチポイントのX座標、Y座標を取得する。
	 * @param x	X座標
	 * @param y	Y座標
	 * @param down trueなら、タッチの種類がdownとみなす。
	 */
	public void setTouchPoint(float x, float y, boolean down){
		this.mx = (int)x;
		this.my = (int)(y + yGap);
		if(down){
			this.x = this.mx;
			this.y = this.my;
		}
	}

	/**
	 * Padを動かす
	 * @param f フィールド
	 */
	synchronized void move(Field f){
		dx = mx - x;
		if(player.compareTo(Player.RED)==0){
			dy = my - y;
		}else{
			dy = my - y;
		}
		double len = Math.sqrt(dx*dx+dy*dy);
		if(len > r){
			this.dx = (int)(dx * r / len);
			this.dy = (int)(dy * r / len);
		}
		x += dx;
		y += dy;

		if(player == Player.RED){
			if(y <= f.redLine){
				inField = true;
			}else{
				inField = false;
			}
		}else{
			if(y >= f.blueLine){
				inField = true;
			}else{
				inField = false;
			}
		}
	}

	/**
	 * キャンバスにPadを描く
	 * @param c キャンバス
	 */
	void draw(Canvas c){
		if(drawable()){
			c.drawCircle(x, y, r, paint);
		}
	}

	/**
	 * 得点を加算する
	 * @param p 得点
	 */
	void addScore(int p){
		score += p;
	}
}
