package com.kedzie.lever;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D;
import rajawali.animation.RotateAroundAnimation3D;
import rajawali.animation.RotateAroundAnimation3D.Axis;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.materials.GouraudMaterial;
import rajawali.math.Number3D;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.kedzie.lever.objects.Floor;
import com.kedzie.lever.objects.Lever;
import com.kedzie.lever.objects.Weight;

public class RajRenderer extends RajawaliRenderer implements OnObjectPickedListener {
	private static final String TAG = RajRenderer.class.getSimpleName();
	private static final float ROTATION_SCALE_FACTOR = 10f;
	/** Is the scene initialized yet */
	private boolean mSceneInitialized;
	/** Object object selection */
	private ObjectColorPicker mPicker;
	
	/** Spotlight */
	private ALight mLight;
	/** Floor of the viewpoint */
	private Floor mFloor;
	/** Lever */
	private Lever _lever;
	/** Fulcrum */
	private BaseObject3D _fulcrum;
	private  BaseObject3D _lever3D;
	
	private float previousX;
	private float previousY;
	private float sceneYRot;
	private float sceneXRot;
	
	public RajRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		if(!mSceneInitialized) {
			mPicker = new ObjectColorPicker(this);
			mPicker.setOnObjectPickedListener(this);
			
			mLight = new DirectionalLight(0.1f, 0.2f, -1.0f); // set the direction
			mLight.setColor(0f, 0f, 1.0f);
			mLight.setPosition(.5f, 0, -2);
			
			mFloor = new Floor(10, 10);
			mFloor.setLight(mLight);
			addChild(mFloor);
			
			_lever = new Lever(4f, .5f, .2f, .5f, 20f, mContext.getResources(), mTextureManager);
			_lever.setLight(mLight);
			addChild(_lever);
			mPicker.registerObject(_lever);
//			_lever3D = AndroidUtils.loadObject(mContext.getResources(), mTextureManager, R.raw.lever);
//			_lever3D.setScale(_lever.getLength(), _lever.getThickness(), _lever.getWidth());
//			_lever3D.setPosition(0f, _lever.getHeight(), 0f);
//			_lever3D.setLight(mLight);
//			addChild(_lever3D);
//			mPicker.registerObject(_lever3D);
			
			Weight w = new Weight(2f, -2f, 0xffffff00);
			w.setLight(mLight);
			mPicker.registerObject(w);
			_lever.addWeight( w );
			w.setMaterial(new GouraudMaterial());
			w.getMaterial().setUseColor(true);
			w.setColor(0xff00ffff);
			
//			Weight w2 = new Weight(1f, 2f, 0xffff00ff);
//			mPicker.registerObject(w2);
//			_lever.addWeight(w2);
			
			_fulcrum = AndroidUtils.loadObject(mContext.getResources(), mTextureManager, R.raw.fulcrum);
			_fulcrum.setLight(mLight);
			_fulcrum.setScale(_lever.getHeight(), _lever.getHeight(), _lever.getHeight());
			addChild(_fulcrum);
			mPicker.registerObject(_fulcrum);

			mCamera.setPosition(0f, 5f, -10f);
			mCamera.setRotX(-15f);
			
			startRendering();
			
			mSceneInitialized=true;
		}
		mFloor.setMaterial(new GouraudMaterial());
		mFloor.getMaterial().setUseColor(true);
		mFloor.setColor(0xffffffff);
		
		_fulcrum.setMaterial(new GouraudMaterial());
		_fulcrum.getMaterial().setUseColor(true);
		_fulcrum.setColor(0xffff0000);
		
		_lever.setMaterial(new GouraudMaterial());
		_lever.getMaterial().setUseColor(true);
		_lever.setColor(0xff00ff00);
		
		_lever.setRotY(sceneYRot);
		_lever.setRotX(sceneXRot);
		
//		_lever3D.setMaterial(new GouraudMaterial());
//		_lever3D.getMaterial().setUseColor(true);
//		_lever3D.setColor(0xff00ff00);
		
//		Animation3D cameraAnim = new RotateAroundAnimation3D(new Number3D(), Axis.Y, 18);
//		cameraAnim.setDuration(8000);
//		cameraAnim.setRepeatCount(Animation3D.INFINITE);
//		cameraAnim.setTransformable3D(mCamera);
//		cameraAnim.start();
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		
		_lever.update();
	}
	
	@Override
	public void onObjectPicked(BaseObject3D object) {
		Log.i(TAG, "onObjectPicked(" + object + ")");
		object.setZ(object.getZ() == 0 ? 2 : 0);
		object.setColor(0xffff0000);
		AndroidUtils.toast(getContext(), "Object Picked: " + object);
	}
	
	public void touch(float x, float y) { 
		mPicker.getObjectAt(x,y); 
	}
	
	public void move(float x, float y) {
		if(previousX==0) previousX=x;
		if(previousY==0) previousY=y;
		sceneYRot +=  (x-previousX)/ROTATION_SCALE_FACTOR;
		sceneXRot +=  (y-previousY)/ROTATION_SCALE_FACTOR;
		previousX=x; previousY=y;
	}
}
