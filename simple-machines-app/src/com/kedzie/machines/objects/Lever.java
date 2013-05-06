package com.kedzie.machines.objects;

import java.util.ArrayList;
import java.util.List;

import rajawali.materials.TextureManager;
import android.content.res.Resources;

/**
 * Physical simulation of a lever with vertical forces applied to it
 * 
 * @author Marek Kedzierski
 */
public class Lever extends Box {
    
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
	
	public Lever(float length, float width, float thickness, float height, float mass, Resources resources, TextureManager textureManager) {
	    super(length,width, thickness);
		mHeight=height;
		mValidAngleRange = (float)Math.toDegrees(Math.atan(mHeight/mLength/2));
		mMass=mass;
		setPosition(0f, mHeight, 0f);
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
		rotation = Math.min(Math.max(-mValidAngleRange, rotation), mValidAngleRange);
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
