package com.kedzie.lever.objects;

import rajawali.BaseObject3D;

public class Lever extends BaseObject3D {
	
	private float _length=4f;
	private float _width = .5f;
	private float _thickness = .2f;
	private float _height = 1f;
	
	private float _angularVelocity=0;
	
	private float halfLeverLength = _length/2;
	/** Angle when lever touches floor */
	private float _minAngle;
	private float _maxAngle;
}
