package com.kedzie.machines.objects;

import rajawali.BaseObject3D;

public class Box extends BaseObject3D {
    /** length (x-axis)  */
    protected float mLength;
    /** width (z-axis) */
    protected float mWidth ;
    /** thickness (y-axis) */
    protected float mThickness;
	
	public Box(float length, float width, float thickness) {
		super();
		mLength=length;
        mWidth=width;
        mThickness=thickness;
		init();
	}
	
	private void init() {
	    float halfLength = mLength/2;
	    float halfWidth = mWidth/2;
	    
	    float []vertices = {
                //front face
                -halfLength, 0f, halfWidth,             //bottom-left
                halfLength,    0f, halfWidth,               //bottom-right
                halfLength,  mThickness, halfWidth, //top-right
                -halfLength, mThickness, halfWidth, //top-left
                //back face
                halfLength, 0f, -halfWidth,              //bottom-left
                -halfLength,    0f, -halfWidth             //bottom-right
                -halfLength, mThickness, -halfWidth,//top-right
                halfLength,  mThickness, -halfWidth,//top-left
              //right face
                halfLength, 0f, halfWidth,              //bottom-left
                halfLength,    0f, -halfWidth,             //bottom-right
                halfLength, mThickness, -halfWidth,//top-right
                halfLength,  mThickness, halfWidth,//top-left
              //left face
                -halfLength, 0f, -halfWidth,              //bottom-left
                -halfLength,    0f, halfWidth,             //bottom-right
                -halfLength, mThickness, halfWidth,//top-right
                -halfLength,  mThickness, -halfWidth,//top-left
              //bottom face
                -halfLength, 0f, -halfWidth,              //bottom-left
                halfLength,    0f, -halfWidth,             //bottom-right
                halfLength, 0f, halfWidth,//top-right
                -halfLength,  0f, halfWidth,//top-left
              //top face
                -halfLength, mThickness, halfWidth,              //bottom-left
                halfLength,   mThickness, halfWidth,             //bottom-right
                halfLength, mThickness, -halfWidth,//top-right
                -halfLength,  mThickness, -halfWidth,//top-left
        };
		
		float[] textureCoords = {
				0, 0,	  1, 0,  	1, 1, 	0, 1, // front
				0, 0,     1, 0,     1, 1,   0, 1, // back
				0, 0,     1, 0,     1, 1,   0, 1, // right
				0, 0,     1, 0,     1, 1,   0, 1, // left
				0, 0,     1, 0,     1, 1,   0, 1, // bottom
				0, 0,     1, 0,     1, 1,   0, 1, // top
		};
		
		float n = 1;
		
		float[] normals = {
				0, 0, n,   0, 0, n,   0, 0, n,   0, 0, n,     //front
                0, 0,-n,   0, 0,-n,   0, 0,-n,   0, 0,-n,     //back
                n, 0, 0,   n, 0, 0,   n, 0, 0,   n, 0, 0,     // right
                -n, 0, 0,  -n, 0, 0,  -n, 0, 0,  -n, 0, 0,     // left
                0,-n, 0,   0,-n, 0,   0,-n, 0,   0,-n, 0,     // bottom
                0, n, 0,   0, n, 0,   0, n, 0,   0, n, 0,     //  top                          
		};
		
		int[] indices = {
				0,1,2, 0,2,3
//				4,5,6, 4,6,7,
//                8,9,10, 8,10,11,
//                12,13,14, 12,14,15,
//                16,17,18, 16,18,19,
//                20,21,22, 20,22,23
		};
		setData(vertices, normals, textureCoords, null, indices);
	}
}
