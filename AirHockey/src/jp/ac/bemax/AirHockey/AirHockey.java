package jp.ac.bemax.AirHockey;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class AirHockey extends Activity {
	private AirHockeyHandler handler;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        
        // Handler により、
        handler = new AirHockeyHandler(this);
        
        handler.sendEmptyMessage(0);
    }

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}
	
	public Handler getHandler(){
		return handler;
	}
}