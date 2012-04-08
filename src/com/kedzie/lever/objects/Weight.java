package com.kedzie.lever.objects;

import rajawali.BaseObject3D;
import rajawali.primitives.Cube;
import android.opengl.GLES20;

import com.kedzie.lever.LeverView;

public class Weight extends Cube {
	public float Ftan;
	public float Frad;
	public float mass;
	public float position;
	private Arrows arrows;
	
	public Weight(float mass, float position) {
		super(1);
		this.mass=mass;
		this.position=position;
		
		arrows = new Arrows();
		addChild(arrows);
	}
	
	public float calculateAngularAcceleration(float angle) {
		Ftan = (float) (mass*Math.cos(angle));
		Frad = (float) (mass*Math.sin(angle));
		float r = Math.abs(position);
		float alpha = Ftan/(r*LeverView.LEVER_MASS);
		if(position<0) 
			alpha*=-1;
		else if(position==0)
			alpha=0;
		arrows.setScale(position>0 ? Frad : -1*Frad, Ftan, 1f);
		return alpha;
	}
	
	class Arrows extends BaseObject3D {

		public Arrows() {
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
			setDrawingMode(GLES20.GL_LINES);
		}
	}
}
