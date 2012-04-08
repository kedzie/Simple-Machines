package com.kedzie.lever.objects;

import rajawali.BaseObject3D;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.GouraudMaterial;
import rajawali.primitives.Cube;
import android.opengl.GLES20;


/**
 * Object applying torque
 * @author Marek Kedzierski
 */
public class Weight extends Cube {
	/** Tangental component of force */
	public float Ftan;
	/** Radial component of force */
	public float Frad;
	/** amount of force  */
	public float _force;
	private Arrows arrows;
	
	public Weight(float force) {
		super(force);
		_force=force;
		mMaterial = new GouraudMaterial();
		arrows = new Arrows();
		addChild(arrows);
	}
	
	public Weight(float force, float position) {
		this(force);
		setX(position);
	}
	
	public Weight(float mass, float position, int color) {
		this(mass, position);
		mMaterial.setUseColor(true);
		setColor(color);
	}
	
	/**
	 * Calculate angular acceleration applied to a lever
	 * @param lever  the {@link Lever}
	 * @return angular acceleration
	 */
	public float calculateAngularAcceleration(Lever lever) {
		Ftan = (float) (_force*Math.cos(lever.getRotZ()));
		Frad = (float) (_force*Math.sin(lever.getRotZ()));
		float r = Math.abs(getX());
		float alpha = Ftan/(r*lever.getMass());
		if(getX()>0) 
			alpha*=-1;
		else if(getX()==0)
			alpha=0;
		arrows.setScale(getX()>0 ? Frad : -1*Frad, Ftan, 1f);
		return alpha;
	}
	
	class Arrows extends BaseObject3D {

		public Arrows() {
			mMaterial = new DiffuseMaterial();
			mMaterial.setUseColor(true);
			setColor(0xffffffff);
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
			short []indices = { 0, 1, 0, 2, 1, 3, 1, 4, 2, 6, 2, 7};
			setData(vertices, normals, null, null, indices);
		}
	}
}
