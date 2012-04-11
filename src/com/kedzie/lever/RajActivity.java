package com.kedzie.lever;

import rajawali.animation.TimerManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
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
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion <  0x20000)
        	throw new Error("OpenGL ES 2.0 is not supported by this device");
       
        LayoutParams textLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        _topText = new TextView(this);
        _topText.setTextSize(14f);
        _topText.setText("Top Text");
        
        LayoutParams glLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        glLayoutParams.weight=1f;
        _surfaceView = new GLSurfaceView(this);
        _surfaceView.setEGLContextClientVersion(2);
        
        _bottomText = new TextView(this);
        _bottomText.setTextSize(10f);
        _bottomText .setText("Bottom Text");
        
        _layout = new LinearLayout(this);
        _layout.setOrientation(LinearLayout.VERTICAL);
        _layout.addView(_topText, textLayoutParams);
        _layout.addView(_surfaceView, glLayoutParams);
        _layout.addView(_bottomText, textLayoutParams);
        setContentView(_layout);
        
        _renderer = new RajRenderer(this);
		_renderer.setSurfaceView(_surfaceView);
		_surfaceView.setRenderer(_renderer);
		
		_surfaceView.setOnTouchListener(this);
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN) 
			_renderer.touch(event.getX(), event.getY());
		if(event.getAction()==MotionEvent.ACTION_MOVE) 
			_renderer.move(event.getX(), event.getY());
		return super.onTouchEvent(event);
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
    	TimerManager.getInstance().clear();
    	_surfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	_renderer.onSurfaceDestroyed();
        unbindDrawables(_layout);
        System.gc();
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
	public boolean onTouch(View arg0, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN) 
			_renderer.touch(event.getX(), event.getY());
		if(event.getAction()==MotionEvent.ACTION_MOVE) 
			_renderer.move(event.getX(), event.getY());
		return true;
	}
}
