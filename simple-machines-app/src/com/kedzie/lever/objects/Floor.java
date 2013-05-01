package com.kedzie.lever.objects;

import rajawali.BaseObject3D;
import rajawali.materials.GouraudMaterial;

public class Floor extends BaseObject3D {

	private int mWidth;
	private int mLength;
	
	public Floor(int width, int length) {
		mWidth=width;
		mLength=length;
		
		mMaterial = new GouraudMaterial();
		mMaterial.setUseColor(true);
		
		float []vertices = {
				mWidth/2, 0f, mLength/2,
				mWidth/2, 0f, -1*mLength/2,
				-1*mWidth/2, 0f, -1*mLength/2,
				-1*mWidth/2, 0f, mLength/2
		};
		float []normals = {
				0f, 1f, 10f,
				0f, 1f, -10f,
				0f, 1f, -10f,
				0f, 1f, 10f
		};
		float []textureCoords = {
			1f, 1f,
			1f, 0f,
			0f, 0f,
			0f, 1f
		};
		float []colors = {
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f
		};
		int []indices = { 0, 1, 2, 0, 2, 3 };
		setData(vertices, normals, textureCoords, colors, indices);
	}
	
}
