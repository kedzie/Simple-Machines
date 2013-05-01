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
@SuppressWarnings("unused")
public class Lever extends BaseObject3D {
	/** length (x-axis)  */
	private float mLength;
	/** width (z-axis) */
	private float mWidth ;
	/** thickness (y-axis) */
	private float mThickness;
	/** Height above ground (y-axis) */
	private float mHeight;
	/** Mass of lever */
	private float mMass;
	/** Angular Velocity of lever (ω) */
	private float mAngularVelocity;
	/** Valid range of θ before  lever touches floor */
	private float mValidAngleRange;
	/** timestamp of last update */
	private long mTimestamp;
	
	private List<Weight> mWeights = new ArrayList<Weight>();
	
	private BaseObject3D mSeatA;
	private BaseObject3D mSeatB;
	
	public Lever(float length, float width, float thickness, float height, float mass, Resources resources, TextureManager textureManager) {
		mLength=length;
		mWidth=width;
		mThickness=thickness;
		mHeight=height;
		float halfLeverLength = mLength/2;
		mValidAngleRange = (float)Math.atan(mHeight/halfLeverLength);
		mMass=mass;
		
		//Load seat meshes
//		seatA = AndroidUtils.loadObject(resources, textureManager, R.id.lever_seat);
//		seatA.setPosition(-1*halfLeverLength, _thickness, 0f);
//		addChild(seatA);
//		seatB = AndroidUtils.loadObject(resources, textureManager, R.id.lever_seat);
//		seatB.setPosition(halfLeverLength-(seatA.getBoundingBox().getMax().x-seatA.getBoundingBox().getMin().x), _thickness, 0f);
//		seatB.setScale(-1f, 1f, 1f);
//		addChild(seatB);
		
		setPosition(0f, mHeight, 0f);
		
		mMaterial = new GouraudMaterial();
		mMaterial.setUseColor(true);
		
		float []vertices = {
				//front face
				-halfLeverLength, 0f, mWidth/2,				//bottom-left
				halfLeverLength,	0f, mWidth/2,				//bottom-right
				halfLeverLength,  mThickness, mWidth/2,	//top-right
				-halfLeverLength, mThickness, mWidth/2,	//top-left
				//back face
				-halfLeverLength, 0f, -1*mWidth/2,				//bottom-left
				-halfLeverLength, mThickness, -1*mWidth/2,//top-left
				halfLeverLength,  mThickness, -1*mWidth/2,//top-right
				halfLeverLength,	0f, -1*mWidth/2				//bottom-right
		};
		float []normals = {
				0f, 0f, -1f,
				0f, 0f, -1f, 
				0f, 0f, -1f, 
				0f, 0f, -1f,
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f,
				0f, 0f, 1f
		};
		int []indices = { 
				0,2,3,0,1,2,	//front
				3,6,5,3,2,6,	//top
				 0, 5, 4, 0, 3, 5,	//left
				1, 7, 6, 1, 7, 2, 	//right
				7, 4, 6, 7, 5, 6, 	//back
				0,4,7,0,7,1		//bottom
			};
		setData(vertices, normals, null, null, indices);
	}
	
	/**
	 * Update angular velocity & rotation for given time interval
	 */
	public void update() {
		long current = System.currentTimeMillis();
		if(mTimestamp==0) mTimestamp=current;
		float interval = (current-mTimestamp)/1000f;
		
		//update angular velocity
		float angularAcceleration = 0;
		for(Weight w : mWeights) 
			angularAcceleration += w.calculateAngularAcceleration(this);
		mAngularVelocity += Math.toDegrees(angularAcceleration)*interval;
		//calculate new rotation, limited by valid range
		float rotation = getRotZ() + mAngularVelocity*interval;
		rotation = Math.max(-1*mValidAngleRange, rotation);
		rotation = Math.min(rotation, mValidAngleRange);
		setRotZ(rotation);
		
		mTimestamp = current;
	}
	
	/**
	 * Add weight applying force to lever
	 * @param w the {@link Weight}
	 */
	public void addWeight(Weight w) {
		mWeights.add(w);
		w.setY(mThickness);
		addChild(w);
	}
	
	/**
	 * Get {@link Weight}s applying force to lever
	 * @return the weights applying force
	 */
	public List<Weight> getWeights() {
		return mWeights;
	}
	
	public float getLength() {
		return mLength;
	}
	public float getWidth() {
		return mWidth;
	}
	public float getThickness() {
		return mThickness;
	}
	public float getHeight() {
		return mHeight;
	}
	public float getMass() {
		return mMass;
	}
}
