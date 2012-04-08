package com.kedzie.lever;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class LeverActivity extends Activity {
	private LeverView glSurfaceView;
	private TextView topText;
	private TextView bottomText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        glSurfaceView = (LeverView)findViewById(R.id.glSurfaceView);
        topText = (TextView)findViewById(R.id.topText);
        bottomText = (TextView)findViewById(R.id.bottomText);
        glSurfaceView.setHandler(new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.getData().getString("top")!=null)
    				topText.setText(msg.getData().getString("top"));
    			if(msg.getData().getString("bottom")!=null)
    				bottomText.setText(msg.getData().getString("bottom"));
    		}
    	});
    }

	@Override
	protected void onPause() {
		glSurfaceView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		glSurfaceView.onResume();
		super.onResume();
	}
}