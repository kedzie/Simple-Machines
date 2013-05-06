package com.kedzie.machines;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RajActivity extends Activity implements OnTouchListener {

	protected GLSurfaceView _surfaceView;
	protected TextView _topText;
	protected TextView _bottomText;
	protected LinearLayout  _layout;
	private RajRenderer _renderer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(am.getDeviceConfigurationInfo().reqGlEsVersion <  0x20000)
        	throw new Error("OpenGL ES 2.0 is not supported by this device");
       
        _topText = new TextView(this);
        _topText.setTextSize(14f);
        _topText.setText("Top Text");
        
        _surfaceView = new GLSurfaceView(this);
        _surfaceView.setEGLContextClientVersion(2);
        
        _bottomText = new TextView(this);
        _bottomText.setTextSize(10f);
        _bottomText .setText("Bottom Text");
        
        _layout = new LinearLayout(this);
        _layout.setOrientation(LinearLayout.VERTICAL);
        _layout.addView(_topText, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        _layout.addView(_surfaceView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
        _layout.addView(_bottomText, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setContentView(_layout);
        
        _renderer = new RajRenderer(this);
		_renderer.setSurfaceView(_surfaceView);
		_surfaceView.setRenderer(_renderer);
		
		_surfaceView.setOnTouchListener(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	_surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    	_surfaceView.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	_surfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	_renderer.onSurfaceDestroyed();
        unbindDrawables(_layout);
        System.gc();
    }
    
    public void setTopText(String text) {
        _topText.setText(text);
    }
    
    public void setBottomText(String text) {
        _bottomText.setText(text);
    }
    
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) 
            view.getBackground().setCallback(null);
        
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) 
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            ((ViewGroup) view).removeAllViews();
        }
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN) 
			_renderer.touch(event.getX(), event.getY());
		if(event.getAction()==MotionEvent.ACTION_MOVE) 
			_renderer.move(event.getX(), event.getY());
		return true;
	}
}
