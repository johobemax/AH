package jp.ac.bemax.AirHockey;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class GameSet implements OnTouchListener {
	private Handler handler;
	
	public GameSet(AirHockey act, int winner){
		handler = act.getHandler();
		
		TextView text = (TextView)act.findViewById(R.id.winner_text);
		
		switch(winner){
		case 0:
			text.setText(R.string.red_side);
			break;
		case 1:
			text.setText(R.string.blue_side);
			break;
		default:
		}
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			handler.sendEmptyMessage(0);
		}
		return false;
	}

}
