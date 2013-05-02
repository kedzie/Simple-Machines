package com.kedzie.machines.objects;

import rajawali.BaseObject3D;
import rajawali.materials.DiffuseMaterial;

public class Floor extends BaseObject3D {

	private int mWidth;
	private int mLength;
	
	public Floor(int width, int length) {
		mWidth=width;
		mLength=length;
		
		setMaterial(new DiffuseMaterial());
		
		
		float halfWidth = mWidth/2;
		float halfLength = mLength/2;
		float []vertices = {
		        halfWidth, 0f, halfLength,
				halfWidth, 0f, -halfLength,
				-halfWidth, 0f, -halfLength,
				-halfWidth, 0f, halfLength
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
		int []indices = { 0, 1, 2, 0, 2, 3 };
		setData(vertices, normals, textureCoords, null, indices);
	}
	
}
