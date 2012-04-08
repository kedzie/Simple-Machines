package com.kedzie.lever;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Weight {
	public float Ftan;
	public float Frad;
	public float mass;
	public float position;
	
	private float []coords = {
			1f, 1f, 0f,
			1f, -1f, 0f,
			-1f, -1f, 0f,
			-1f, 1f, 0f
	};
	private FloatBuffer vB = LeverView.allocate(coords);
	private short []indices = { 0, 3, 2, 0, 2, 1 };
	private ShortBuffer indexBuffer = LeverView.allocate(indices);
	
	private static float []arrowCoords = {
			0f,0f,0f,
			1f,0f,0f,
			0f,0f,0f,
			0f,-1f,0f,
			1f,0f,0f,
			.8f,.1f,0f,
			1f,0f,0f,
			.8f,-.1f,0f,
			0f,-1f,0f,
			-.2f, -.8f, 0f,
			0f,-1f,0f,
			.2f, -.8f, 0f
	};
	private static FloatBuffer arrowVB = LeverView.allocate(arrowCoords);
	
	public Weight(float mass, float position) {
		this.mass=mass;
		this.position=position;
	}

	public void draw(GL10 gl) {
		//draw object
		gl.glPushMatrix();
		gl.glTranslatef(position, 0f, 0f);
		gl.glColor4f(1f, 1f, 0f, 1f);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] { 1f, 1f, 0f, 1.0f  }, 0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vB);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		//draw arrows
		gl.glColor4f(1f,1f,1f,1f);
		gl.glLineWidth(3f);
		gl.glScalef(position>0 ? Frad : -1*Frad, Ftan, 1f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, arrowVB);
		gl.glDrawArrays(GL10.GL_LINES, 0, arrowCoords.length/3);
		gl.glPopMatrix();
	}
	
	public float calculateAngularAcceleration(float angle) {
		Ftan = (float) (mass*Math.cos(angle));
		Frad = (float) (mass*Math.sin(angle));
		float r = Math.abs(position-0);
		float alpha = Ftan/(r*LeverView.LEVER_MASS);
		if(position<0) 
			alpha*=-1;
		else if(position==0)
			alpha=0;
		return alpha;
	}
}