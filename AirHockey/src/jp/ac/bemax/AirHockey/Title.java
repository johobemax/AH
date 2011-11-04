package jp.ac.bemax.AirHockey;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Title implements OnTouchListener{
	private Handler handler;
	
	public Title(AirHockey act){
		handler = act.getHandler();
	}
	
	public boolean onTouch(View view, MotionEvent event) {

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Message msg = new Message();
			msg.what = 1;
			
			switch(view.getId()){
			case R.id.single_mode:
				msg.obj = Mode.Single;
				break;
			case R.id.duale_mode:
				msg.obj = Mode.Duale;
				break;
			default:
			}
			
			handler.sendMessage(msg);
		}
		return false;
	}
	
}
