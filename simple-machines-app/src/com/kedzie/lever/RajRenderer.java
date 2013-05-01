package com.kedzie.lever;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.lights.PointLight;
import rajawali.parser.AParser.ParsingException;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLU;
import android.util.Log;

import com.kedzie.lever.objects.ColoredGouraudMaterial;
import com.kedzie.lever.objects.Floor;
import com.kedzie.lever.objects.Lever;
import com.kedzie.lever.objects.Weight;

public class RajRenderer extends RajawaliRenderer {
    private static final String TAG = "RajRenderer";

    private static final float ROTATION_SCALE_FACTOR = 10f;
    private static final int FRAME_RATE = 60;

    private ALight mSpotLight;
    private ALight mPointLight;
    private Floor mFloor;
    private Lever mLever;
    private BaseObject3D mFulcrum;

    private float previousX;
    private float previousY;
    private float sceneYRot;
    private float sceneXRot;

    public RajRenderer(Context context) {
        super(context);
        setFrameRate(FRAME_RATE);
    }

    @Override
    protected void initScene() {
        mPointLight = new PointLight();
        mPointLight.setPosition(0, 10, -6);
        mPointLight.setPower(3);

        mSpotLight = new DirectionalLight(0.1f, 0.2f, 1.0f); // set the direction
        mSpotLight.setColor(Color.GREEN);
        mSpotLight.setPosition(.5f, 0, -2);

        mCamera.setLookAt(0, 0, 0);
        mCamera.setZ(-6);
        mCamera.setY(5);

        mFloor = new Floor(10, 10);
        mFloor.setMaterial(new ColoredGouraudMaterial());
        mFloor.setColor(Color.DKGRAY);
        addChild(mFloor);

        mLever = new Lever(4f, .5f, .2f, .5f, 20f, mContext.getResources(), mTextureManager);
        mLever.setMaterial(new ColoredGouraudMaterial());
        mLever.setColor(Color.GREEN);
        addChild(mLever);

        Weight w = new Weight(2f, -2f, 0xffffff00);
        w.addLight(mSpotLight);
        w.addLight(mPointLight);
        mLever.addWeight( w );

        Weight w2 = new Weight(1f, 2f, 0xffff00ff);
        w.addLight(mSpotLight);
        w.addLight(mPointLight);
        mLever.addWeight(w2);

        try {
            mFulcrum = AndroidUtils.loadObject(mContext.getResources(), mTextureManager, R.raw.fulcrum);
            mFulcrum.setScale(mLever.getHeight(), mLever.getHeight(), mLever.getHeight());
            mFulcrum.setMaterial(new ColoredGouraudMaterial());
            mFulcrum.setColor(Color.GREEN);
            addChild(mFulcrum);
        } catch (ParsingException e) {
            Log.e(TAG, "Error", e);
        }
    }

    @Override
    public void addChild(BaseObject3D obj) {
        obj.addLight(mSpotLight);
        obj.addLight(mPointLight);
        super.addChild(obj);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        mLever.update();
        mLever.setRotY(sceneYRot);
        mLever.setRotX(sceneXRot);
    }

    public void touch(float x, float y) { 
        int []viewport = { 0, 0, getViewportWidth(), getViewportHeight() };
        float []near = new float[4];
        float []far = new float[4];
        GLU.gluUnProject(x, getViewportHeight()-y, 0f, mVMatrix, 0, mPMatrix, 0, viewport, 0, near, 0);
        GLU.gluUnProject(x, getViewportHeight()-y, 1f, mVMatrix, 0, mPMatrix, 0, viewport, 0, far, 0);
        near[0]=near[0]/near[3];
        near[1]=near[1]/near[3];
        near[2]=near[2]/near[3];
        far[0]=far[0]/near[3];
        far[1]=far[1]/near[3];
        far[2]=far[2]/near[3];
        Log.d(TAG, String.format("Near: %1$f, %2$f, %3$f", near[0], near[1], near[2]));
        Log.d(TAG, String.format("Far: %1$f, %2$f, %3$f", far[0], far[1], far[2]));
    }

    public void move(float x, float y) {
        if(previousX==0) previousX=x;
        if(previousY==0) previousY=y;
        sceneYRot +=  (x-previousX)/ROTATION_SCALE_FACTOR;
        sceneXRot +=  (y-previousY)/ROTATION_SCALE_FACTOR;
        previousX=x; previousY=y;
    }
}
