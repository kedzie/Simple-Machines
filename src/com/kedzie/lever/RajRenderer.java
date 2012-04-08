package com.kedzie.lever;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.primitives.Sphere;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.graphics.BitmapFactory;

public class RajRenderer extends RajawaliRenderer {

	private ALight mLight;
	private Sphere mSphere;
	
	public RajRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		mLight = new DirectionalLight(0.1f, 0.2f, -1.0f); // set the direction
		mLight.setColor(0, 0, 1.0f);
		mLight.setPosition(.5f, 0, -2);
		
		mSphere = new Sphere(1, 12, 12);
		mSphere.setLight(mLight);
		mSphere.addTexture(mTextureManager.addTexture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.selera_sari)));
		addChild(mSphere);
		
		mCamera.setZ(-4.2f);
		
		startRendering();
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		
		mSphere.setRotY(mSphere.getRotY() + 1);
	}
	
	public void touch(int x, int y) {
		
	}
}
