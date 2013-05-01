package com.kedzie.lever.objects;

import rajawali.BaseObject3D;
import rajawali.primitives.Cube;
import android.graphics.Color;
import android.opengl.GLES20;


/**
 * Object applying torque
 * @author Marek Kędzierski
 */
public class Weight extends Cube {
    
	/** Tangental component of force */
	public float mFtan;
	
	/** Radial component of force */
	public float mFrad;
	
	/** amount of force  */
	public float mForce;
	
	private Arrows mArrows;
	
	public Weight(float force) {
		super(force);
		mForce=force;
		mArrows = new Arrows();
		addChild(mArrows);
	}
	
	public Weight(float force, float position) {
		this(force);
		setX(position);
	}
	
	public Weight(float mass, float position, int color) {
		this(mass, position);
		mMaterial = new ColoredGouraudMaterial();
		setColor(color);
	}
	
	/**
	 * Calculate angular acceleration applied to a lever
	 * @param lever  the {@link Lever}
	 * @return angular acceleration
	 */
	public float calculateAngularAcceleration(Lever lever) {
		mFtan = (float) (mForce*Math.cos(lever.getRotZ()));
		mFrad = (float) (mForce*Math.sin(lever.getRotZ()));
		float r = Math.abs(getX());
		float alpha = mFtan/(r*lever.getMass());
		if(getX()>0) 
			alpha*=-1;
		else if(getX()==0)
			alpha=0;
		mArrows.setScale(getX()>0 ? mFrad : -1*mFrad, mFtan, 1f);
		return alpha;
	}
	
	class Arrows extends BaseObject3D {

		public Arrows() {
			mMaterial = new ColoredDiffuseMaterial();
			setColor(Color.WHITE);
			setDrawingMode(GLES20.GL_LINES);
			float []vertices = {
					0f,0f,0f,
					1f,0f,0f,
					0f,-1f,0f,
					.8f,.1f,0f,
					.8f,-.1f,0f,
					-.2f, -.8f, 0f,
					.2f, -.8f, 0f
			};
			float []normals = {
					0f,0f,1f,
					0f,0f,1f,
					0f,0f,1f,
					0f,0f,1f,
					0f,0f,1f,
					0f,0f,1f,
					0f,0f,1f
			};
			int []indices = { 0, 1, 0, 2, 1, 3, 1, 4, 2, 6, 2, 7};
			setData(vertices, normals, null, null, indices);
		}
	}
}
