package com.kedzie.lever.objects;

import java.util.ArrayList;
import java.util.List;

import rajawali.BaseObject3D;
import rajawali.materials.GouraudMaterial;
import rajawali.materials.TextureManager;
import android.content.res.Resources;

/**
 * Physical simulation of a lever with vertical forces applied to it
 * 
 * @author Marek Kedzierski
 */
public class Lever extends BaseObject3D {
	/** length (x-axis)  */
	private float _length;
	/** width (z-axis) */
	private float _width ;
	/** thickness (y-axis) */
	private float _thickness;
	/** Height above ground (y-axis) */
	private float _height;
	/** Mass of lever */
	private float _mass;
	/** Angular Velocity of lever (ω) */
	private float _angularVelocity;
	/** Valid range of θ before  lever touches floor */
	private float _validAngleRange;
	
	/** timestamp of last update */
	private long timestamp;
	
	private List<Weight> _weights = new ArrayList<Weight>();
	private BaseObject3D seatA;
	private BaseObject3D seatB;
	
	public Lever(float length, float width, float thickness, float height, float mass, Resources resources, TextureManager textureManager) {
		_length=length;
		_width=width;
		_thickness=thickness;
		_height=height;
		float halfLeverLength = _length/2;
		_validAngleRange = (float)Math.atan(_height/halfLeverLength);
		_mass=mass;
		
		//Load seat meshes
//		seatA = AndroidUtils.loadObject(resources, textureManager, R.id.lever_seat);
//		seatA.setPosition(-1*halfLeverLength, _thickness, 0f);
//		addChild(seatA);
//		seatB = AndroidUtils.loadObject(resources, textureManager, R.id.lever_seat);
//		seatB.setPosition(halfLeverLength-(seatA.getBoundingBox().getMax().x-seatA.getBoundingBox().getMin().x), _thickness, 0f);
//		seatB.setScale(-1f, 1f, 1f);
//		addChild(seatB);
		
		setPosition(0f, _height, 0f);
		
		mMaterial = new GouraudMaterial();
		
		float []vertices = {
				//front face
				-halfLeverLength, 0f, 0,				//bottom-left
				halfLeverLength,	0f, 0,				//bottom-right
				halfLeverLength,  _thickness, 0,	//top-right
				-halfLeverLength, _thickness, 0,	//top-left
				//back face
				-halfLeverLength, 0f, -1*_width,				//bottom-left
				-halfLeverLength, _thickness, -1*_width,//top-left
				halfLeverLength,  _thickness, -1*_width,//top-right
				halfLeverLength,	0f, -1*_width				//bottom-right
		};
		float []normals = {
				0f, 0f, 1f,
				0f, 0f, 1f, 
				0f, 0f, 1f, 
				0f, 0f, 1f,
				0f, 0f, -1f,
				0f, 0f, -1f,
				0f, 0f, -1f,
				0f, 0f, -1f,
		};
		float [] colors = {
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
				.6f, .7f, .2f,
		};
		short []indices = { 
				0,2,3,0,1,2,	//front
				3,6,5,3,2,6,	//top
				 0, 5, 4, 0, 3, 5,	//left
					//right
					//back
				0,4,7,0,7,1		//bottom
			};
		setData(vertices, normals, null, colors, indices);
	}
	
	/**
	 * Update angular velocity & rotation for given time interval
	 */
	public void update() {
		long current = System.currentTimeMillis();
		if(timestamp==0) timestamp=current;
		float interval = (current-timestamp)/1000f;
		
		//update angular velocity
		float angularAcceleration = 0;
		for(Weight w : _weights) 
			angularAcceleration += w.calculateAngularAcceleration(this);
		_angularVelocity += angularAcceleration*interval;
		//calculate new rotation, limited by valid range
		float rotation = getRotZ() + _angularVelocity*interval;
		rotation = Math.max(-1*_validAngleRange, rotation);
		rotation = Math.min(rotation, _validAngleRange);
		setRotZ(rotation);
		
		timestamp = current;
	}
	
	/**
	 * Add weight applying force to lever
	 * @param w the {@link Weight}
	 */
	public void addWeight(Weight w) {
		_weights.add(w);
		w.setY(_thickness);
		addChild(w);
	}
	
	/**
	 * Get {@link Weight}s applying force to lever
	 * @return the weights applying force
	 */
	public List<Weight> getWeights() {
		return _weights;
	}
	
	public float getLength() {
		return _length;
	}
	public void setLength(float length) {
		_length = length;
	}
	public float getWidth() {
		return _width;
	}
	public void setWidth(float width) {
		_width = width;
	}
	public float getThickness() {
		return _thickness;
	}
	public void setThickness(float thickness) {
		_thickness = thickness;
	}
	public float getHeight() {
		return _height;
	}
	public void setHeight(float height) {
		_height = height;
	}
	public float getMass() {
		return _mass;
	}
}
