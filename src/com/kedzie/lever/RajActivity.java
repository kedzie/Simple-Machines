package com.kedzie.lever;

import android.os.Bundle;
import rajawali.RajawaliActivity;

public class RajActivity extends RajawaliActivity {

	protected RajRenderer _renderer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_renderer = new RajRenderer(this);
		_renderer.setSurfaceView(mSurfaceView);
		setRenderer(_renderer);
	}

}
