package jp.ac.bemax.AirHockey;

import jp.ac.bemax.AirHockey.R;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.widget.TextView;

public class AirHockeyHandler extends Handler {
	private AirHockey act;
	
	public AirHockeyHandler(AirHockey act){
		this.act = act;
	}
	
	@Override
	public void handleMessage(Message msg) {
		// メッセージを受けて、画面を切り替える
		switch(msg.what){
		case 0: // Title画面の処理
			
			// コンテンツビューをtitle.xmlにする
			act.setContentView(R.layout.title);
			
			// Title画面を操作するクラスをインスタンス化
			Title title = new Title(act);
			
			// プレイモードを選択するTextViewを代入
			TextView single = (TextView)act.findViewById(R.id.single_mode);
			TextView duale = (TextView)act.findViewById(R.id.duale_mode);
			
			single.setOnTouchListener(title);
			duale.setOnTouchListener(title);
			
			break;
		case 1: // ゲーム画面の処理
			
			// コンテンツビューをmain.xmlにする
			act.setContentView(R.layout.main);
			
			Field field = new Field(act, (Mode)msg.obj);
			
			break;
		case 2:
			act.setContentView(R.layout.end);
			
			GameSet gameset = new GameSet(act, msg.arg1);
			
			break;
		}
		
	}
}
