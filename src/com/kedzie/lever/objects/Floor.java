package com.kedzie.lever.objects;

import rajawali.BaseObject3D;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.GouraudMaterial;

public class Floor extends BaseObject3D {

	private int _width;
	private int _length;
	
	public Floor(int width, int length) {
		_width=width;
		_length=length;
		
		mMaterial = new GouraudMaterial();
		
		float []vertices = {
				_width/2, 0f, _length/2,
				_width/2, 0f, -1*_length/2,
				-1*_width/2, 0f, -1*_length/2,
				-1*_width/2, 0f, _length/2
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
		short []indices = { 0, 1, 2, 0, 2, 3 };
		setData(vertices, normals, textureCoords, colors, indices);
	}
	
}
