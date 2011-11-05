package jp.pokkyunsoft.AirHockey;

import jp.ac.bemax.AirHockey.R;
import android.graphics.Color;
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
		text.setOnTouchListener(this);
		
		switch(winner){
		case 0:
			text.setTextColor(Color.RED);
			text.setText(R.string.red_side);

			break;
		case 1:
			text.setTextColor(Color.BLUE);
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
